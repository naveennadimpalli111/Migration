package com.its255.web.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.its255.schema.FieldSpec;
import com.its255.schema.FieldType;
import com.its255.schema.SchemaRegistry;
import com.its255.util.FieldValueNormalizer;
import com.its255.util.LoggingUtil;
import com.its255.viewer.ChunkedMMapRecordStore;
import com.its255.viewer.FastRecordFilter;
import com.its255.viewer.ParallelPrefixIndexBuilder;
import com.its255.viewer.RecordNavigator;
import com.its255.viewer.RecordQuery;
import com.its255.viewer.SchemaHtmlRenderer;
import com.its255.viewer.ViewerConfig;
import com.its255.viewer.ViewerSession;
import com.its255.web.cleanup.CleanupScheduler;
import com.its255.web.service.CsvExportService;
import com.its255.web.service.EditedFileDownloadService;
import com.its255.web.storage.S3FileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/viewer")
public class FastViewerController {
	private static final int HORIZONTAL_PAGE_SIZE = 250;

	private final ViewerConfig cfg;
	private final CleanupScheduler cleanupScheduler;
	private final EditedFileDownloadService editedFileDownloadService;
	private final S3FileStorageService s3FileStorageService;

	public FastViewerController(ViewerConfig cfg, CleanupScheduler cleanupScheduler,
			S3FileStorageService s3FileStorageService) {
		this.cfg = cfg;
		this.cleanupScheduler = cleanupScheduler;
		this.editedFileDownloadService = new EditedFileDownloadService();
		this.s3FileStorageService = s3FileStorageService;
	}

	private Charset cs() {
		return Charset.forName(cfg.getCharset());
	}

	@GetMapping
	public String page(Model model, HttpSession session) {
		ViewerSession vs = getSession(session);

		model.addAttribute("hasFile", vs.hasFile());
		model.addAttribute("count", (vs.nav != null) ? vs.nav.size() : 0);
		model.addAttribute("position", (vs.nav != null) ? vs.nav.position() : 0);
		model.addAttribute("hasPrev", vs.nav != null && vs.nav.hasPrev());
		model.addAttribute("hasNext", vs.nav != null && vs.nav.hasNext());
		model.addAttribute("progress", vs.progress);

		// NEW: show uploaded filename
		model.addAttribute("filename", vs.originalFilename);
		model.addAttribute("s3Enabled", vs.s3Enabled);
		model.addAttribute("s3Key", vs.s3Key);
		model.addAttribute("html", "");
		model.addAttribute("hasEdits", hasPendingChanges(vs));

		return "viewer";
	}

	@ResponseBody
	@GetMapping("/progress")
	public Map<String, Object> progress(HttpSession session) {

		ViewerSession vs = getSession(session);
		return Map.of("progress", vs.progress);

	}

	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file,
			@RequestParam("transactionType") String transactionType, Model model, HttpSession session)
			throws Exception {
		if (file.isEmpty()) {
			model.addAttribute("error", "Please select a file.");
			return page(model, session);
		}
		if (transactionType == null || transactionType.isBlank()) {
			throw new IllegalStateException("Transaction type missing in ViewerSession. Upload required.");
		}
		ViewerSession vs = getSession(session);
		vs.transactionType = transactionType;
		vs.selectedRecordType = null;
		vs.editOverlay.clear();
		vs.deletedRecords.clear();
		vs.hasCommittedChanges = false;
		vs.s3Enabled = s3FileStorageService.isEnabled();
		vs.s3Key = null;

		// clean old store if present
		if (vs.store instanceof AutoCloseable ac) {
			try {
				ac.close();
			} catch (Exception ignore) {
			}
		}

		// create app-specific temp root
		Path appTmpRoot = getAppTempRoot();

		// create per-session directory
		Path sessionDir = Files.createTempDirectory(appTmpRoot, "session_");

		// store uploaded file inside session directory
		Path uploadedFile = sessionDir.resolve("uploaded.dat");

		file.transferTo(uploadedFile.toFile());
		String s3Key = null;
		if (s3FileStorageService.isEnabled()) {
			s3FileStorageService.verifyConnection();
			s3Key = s3FileStorageService.uploadNewFile(uploadedFile, file.getOriginalFilename());
			s3FileStorageService.downloadToFile(s3Key, uploadedFile);
		}

		// save paths in session
		vs.sessionDir = sessionDir;
		vs.filePath = uploadedFile;
		vs.originalFilename = file.getOriginalFilename();
		vs.s3Enabled = s3FileStorageService.isEnabled();
		vs.s3Key = s3Key;
		vs.progress = 0.0;

		long windowBytes = Math.max(1, cfg.getWindowSizeMB()) * 1024L * 1024L;

		int recordLength = SchemaRegistry.getRecordLength(transactionType);

		vs.store = new ChunkedMMapRecordStore(uploadedFile, cs(), recordLength, windowBytes, transactionType);

		LoggingUtil.debug("Viewer upload: sessionDir=" + sessionDir + ", filePath=" + uploadedFile
				+ ", s3Enabled=" + vs.s3Enabled + ", s3Key=" + vs.s3Key);

		int workers = Math.min(cfg.getIndexWorkers(), (int) getRecordCount(vs.store));
		ParallelPrefixIndexBuilder builder = new ParallelPrefixIndexBuilder(vs.store, cfg.getPrefixIndexLength(),
				workers, cfg.getProgressStep(), p -> vs.progress = p);

		// If you still build index synchronously:
		vs.pidx = builder.build();
		vs.filter = new FastRecordFilter(vs.store, vs.pidx);
		vs.nav = new RecordNavigator(List.of(), -1);
		vs.lastFiltered = List.of();

		return "redirect:/viewer";
	}

	@PostMapping("/search")
	public String search(@RequestParam(name = "sccf", required = false) String sccf,
			@RequestParam(name = "recordTypeCode", required = false) String type,
			@RequestParam(name = "recNo", required = false) Integer recNo, Model model, HttpSession session) {
		ViewerSession vs = getSession(session);

		if (!vs.hasFile()) {
			model.addAttribute("error", "Upload a file first.");
			return page(model, session);
		}

		var q = new RecordQuery(Optional.ofNullable((sccf != null && !sccf.isBlank()) ? sccf.trim() : null),
				Optional.ofNullable((type != null && !type.isBlank()) ? type.trim() : null),
				Optional.ofNullable(recNo));

		List<Integer> filtered = vs.filter.apply(q);
		if (vs.deletedRecords != null && !vs.deletedRecords.isEmpty()) {
			filtered = new ArrayList<>(filtered);
			filtered.removeIf(vs.deletedRecords::contains);
		}
		vs.selectedRecordType = type;
		int startRn = q.recordNumber().orElse(-1);

		vs.nav = new RecordNavigator(filtered, startRn);
		vs.lastFiltered = filtered;

		return "redirect:/viewer/current";

	}

	@GetMapping("/current")
	public String current(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
			HttpSession session) {

		ViewerSession vs = getSession(session);

		String viewMode = (String) session.getAttribute("viewMode");
		session.setAttribute("viewMode", viewMode);
		model.addAttribute("viewMode", viewMode);

		model.addAttribute("editMode", Boolean.TRUE.equals(vs.editMode));
		model.addAttribute("selectedEditRecord", vs.selectedEditRecord);

		if (!vs.hasFile() || vs.nav == null || vs.nav.size() == 0) {
			model.addAttribute("html", "<div class='alert alert-info'>No results. Upload a file and search.</div>");
			return page(model, session);
		}

		int rn = vs.nav.currentRecordNumber();

		// Base values
		String type = safeInvoke(vs.store, "readType", rn);
		String displayType = type;

		boolean recordHasSccf = hasSccf(vs.transactionType, type.trim());

		String sccf = "";
		if (recordHasSccf) {
			sccf = safeInvoke(vs.store, "readSccf", rn);
		}

		//String txn = safeInvoke(vs.store, "readTxn", rn);

		// Renderer
		SchemaHtmlRenderer renderer = new SchemaHtmlRenderer(vs.store, cs(), vs.transactionType, vs.editMode,
				vs.editOverlay);

		String tableHtml = renderer.render(rn);

		// PAGINATION (IMPORTANT)
		int horizontalTotal = vs.lastFiltered != null ? vs.lastFiltered.size() : 0;

		int horizontalTotalPages = Math.max(1, (horizontalTotal + HORIZONTAL_PAGE_SIZE - 1) / HORIZONTAL_PAGE_SIZE);

		page = Math.max(1, Math.min(page, horizontalTotalPages));

		int horizontalOffset = (page - 1) * HORIZONTAL_PAGE_SIZE;

		// SINGLE CLEAN OVERLAY LOGIC
		Map<String, String> overlay = vs.editOverlay.get(rn);

		if (overlay != null) {
			// TYPE display override (for UI only, doesn't affect SCCF prefix)
			displayType = resolveOverlayRecordType(type, overlay);

			// SCCF logic (MUST use ORIGINAL form type for prefix, not overlay-overridden type)
			if (recordHasSccf) {
				sccf = resolveOverlaySccf(sccf, type, overlay);
			}
		}

		String displaySccf = sccf;
		// Treat any '{' characters in SCCF as '0'
		if (displaySccf != null) {
			displaySccf = displaySccf.replace("{", "0");
		}

		// SAFE HEADER (FIXED)
		StringBuilder headerBuilder = new StringBuilder();

		headerBuilder.append("<h5 id='record-context'>").append("Record #").append(rn);

		if (recordHasSccf && displaySccf != null && !displaySccf.isBlank()) {
			headerBuilder.append(" SCCF=").append(displaySccf);
		}

		headerBuilder.append(" Type=").append(displayType.trim()).append("</h5>");

		String dynamicHeader = headerBuilder.toString();

		// Vertical HTML
		String dynamicVerticalHtml = dynamicHeader + renderer.renderVertical(rn);

		// Model attributes (unchanged)
		model.addAttribute("hasFile", true);
		model.addAttribute("filename", vs.originalFilename);
		model.addAttribute("s3Enabled", vs.s3Enabled);
		model.addAttribute("s3Key", vs.s3Key);
		model.addAttribute("count", vs.nav.size());
		model.addAttribute("position", vs.nav.position());
		model.addAttribute("hasPrev", vs.nav.hasPrev());
		model.addAttribute("hasNext", vs.nav.hasNext());

		model.addAttribute("html", dynamicHeader + tableHtml);

		model.addAttribute("editMode", vs.editMode);
		model.addAttribute("hasEdits", hasPendingChanges(vs));

		model.addAttribute("verticalHtml", dynamicVerticalHtml);

		model.addAttribute("horizontalHtml",
				renderer.renderHorizontalPage(session, vs.selectedRecordType, horizontalOffset, HORIZONTAL_PAGE_SIZE, page));

		model.addAttribute("horizontalPage", page);
		model.addAttribute("currentRecordNo", rn);
		model.addAttribute("horizontalTotalPages", horizontalTotalPages);
		model.addAttribute("horizontalHasPrev", page > 1);
		model.addAttribute("horizontalHasNext", page < horizontalTotalPages);
		model.addAttribute("horizontalTotalCount", horizontalTotal);

		return "viewer";
	}

	@PostMapping("/prev")
	public String prev(HttpSession session) {
		ViewerSession vs = getSession(session);
		if (vs.nav != null && vs.nav.hasPrev())
			vs.nav.prev();
		return "redirect:/viewer/current";

	}

	@PostMapping("/next")
	public String next(HttpSession session) {
		ViewerSession vs = getSession(session);
		if (vs.nav != null && vs.nav.hasNext())
			vs.nav.next();
		return "redirect:/viewer/current";
	}

	private String resolveOverlayRecordType(String currentType, Map<String, String> overlay) {
		if (overlay == null || overlay.isEmpty()) {
			return currentType;
		}

		String override = overlay.get("REC_TYPE");
		if (override != null && !override.isBlank()) {
			return override;
		}

		for (String key : overlay.keySet()) {
			String upper = key.toUpperCase();
			if (upper.endsWith("REC-TYPE") || upper.endsWith("REC-TYPE")) {
				String value = overlay.get(key);
				if (value != null && !value.isBlank()) {
					return value;
				}
			}
		}

		return currentType;
	}

	private String resolveOverlaySccf(String currentSccf, String type, Map<String, String> overlay) {
		if (overlay == null || overlay.isEmpty()) {
			return currentSccf;
		}

		String direct = overlay.get("SCCF");
		if (direct != null && !direct.isBlank()) {
			return direct;
		}

		for (String key : overlay.keySet()) {
			if (key != null && key.toUpperCase().contains("SCCF")) {
				String value = overlay.get(key);
				if (value != null && !value.isBlank()) {
					return value;
				}
			}
		}

		String prefix = null;
		for (String key : overlay.keySet()) {
			if (key == null) continue;
			String upper = key.toUpperCase();
			int idx = upper.indexOf("-SER-NUM-");
			if (idx >= 0) {
				prefix = key.substring(0, idx + 9);
				break;
			}
		}

		if (prefix != null) {
			String localPlan = normalizeOverlayValue(overlay.getOrDefault(prefix + "LOCAL-PLAN", ""));
			String cc = normalizeOverlayValue(overlay.getOrDefault(prefix + "JULDT-CC", ""));
			String yy = normalizeOverlayValue(overlay.getOrDefault(prefix + "JULDT-YY", ""));
			String ddd = normalizeOverlayValue(overlay.getOrDefault(prefix + "JULDT-DDD", ""));
			String sequence = normalizeOverlayValue(overlay.getOrDefault(prefix + "SEQUENCE", ""));
			String suffix = normalizeOverlayValue(overlay.getOrDefault(prefix + "SUFFIX", ""));

			String rebuilt = localPlan + cc + yy + ddd + sequence + suffix;
			if (!rebuilt.trim().isEmpty()) {
				return rebuilt;
			}
		}

		return currentSccf;
	}

	private static String normalizeOverlayValue(String s) {
		if (s == null) return "";
		if (s.equals("{")) return "0";
		return s;
	}

	@GetMapping("/export")
	public void exportZip(jakarta.servlet.http.HttpServletResponse resp, HttpSession session) throws Exception {
		ViewerSession vs = getSession(session);
		if (!vs.hasFile() || vs.lastFiltered == null || vs.lastFiltered.isEmpty())
			return;

		resp.setContentType("application/zip");
		resp.setHeader("Content-Disposition", "attachment; filename=its255_scope_export.zip");

		CsvExportService svc = new CsvExportService(vs.store, cs(), cfg.getExportBufferSize(), vs.transactionType);
		svc.exportPerTypeZip(vs.lastFiltered, resp.getOutputStream());

	}

	@ResponseBody
	@GetMapping("/storage/status")
	public Map<String, Object> storageStatus() {
		Map<String, Object> result = new HashMap<>();
		result.put("enabled", s3FileStorageService.isEnabled());
		result.put("bucket", s3FileStorageService.bucketName());
		if (!s3FileStorageService.isEnabled()) {
			result.put("connected", false);
			result.put("message", "AWS S3 storage is disabled.");
			return result;
		}
		try {
			s3FileStorageService.verifyConnection();
			result.put("connected", true);
			result.put("message", "AWS S3 bucket connection verified.");
		} catch (RuntimeException ex) {
			result.put("connected", false);
			result.put("message", ex.getMessage());
		}
		return result;
	}

	private ViewerSession getSession(HttpSession session) {
		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");
		if (vs == null) {
			vs = new ViewerSession();
			session.setAttribute("VIEWER_SESSION", vs);
		}
		return vs;
	}

	private boolean hasPendingChanges(ViewerSession vs) {
		return vs != null && (vs.hasCommittedChanges || !vs.editOverlay.isEmpty() || !vs.deletedRecords.isEmpty());
	}

	private String safeInvoke(Object store, String method, int rn) {
		try {
			return String.valueOf(store.getClass().getMethod(method, int.class).invoke(store, rn));
		} catch (Exception e) {
			return "";
		}
	}

	@GetMapping("/edit")
	public String enableEdit(
			@RequestParam(name = "recordNo", required = false) Integer recordNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "viewMode", required = false) String viewMode,
			HttpSession session) {
		ViewerSession vs = getSession(session);
		vs.editMode = true;
		if (viewMode != null && !viewMode.isBlank()) {
			session.setAttribute("viewMode", viewMode);
		}
		if (recordNo != null && vs.nav != null) {
			vs.selectedEditRecord = recordNo;
			vs.nav.setCurrent(recordNo);
		}
		return "redirect:/viewer/current?page=" + page;
	}

	@PostMapping("/save")

	public String save(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		ViewerSession vs = getSession(session);
		String viewMode = request.getParameter("viewMode");
	if (viewMode != null && !viewMode.isBlank()) {
		session.setAttribute("viewMode", viewMode);
	}
		request.getParameterMap().forEach((key, value) -> {
			if (!key.startsWith("field_")) {
				return;
			}
			int firstUnderscore = key.indexOf('_');
			int secondUnderscore = key.indexOf('_', firstUnderscore + 1);
			if (firstUnderscore < 0 || secondUnderscore < 0 || secondUnderscore + 1 >= key.length()) {
				return;
			}
			try {
				int recordNo = Integer.parseInt(key.substring(firstUnderscore + 1, secondUnderscore));
				String encodedFieldName = key.substring(secondUnderscore + 1);
				String fieldName = URLDecoder.decode(encodedFieldName, StandardCharsets.UTF_8);
				String newValue = "";
				if (value != null && value.length > 0) {
					newValue = selectFormFieldValue(vs, recordNo, fieldName, viewMode, value);
				}
				Map<String, String> recordEdits = vs.editOverlay.computeIfAbsent(recordNo, k -> new HashMap<>());
				// Normalize placeholder '{' to '0' before saving into overlay
				if (newValue != null && newValue.equals("{")) {
					newValue = "0";
				}
				newValue = FieldValueNormalizer.normalize(fieldName, newValue);
				newValue = preserveOriginalFieldLength(vs, recordNo, fieldName, newValue);
				recordEdits.put(fieldName, newValue);
				System.out.println("OVERLAY SAVED: viewMode=" + viewMode + ", record=" + recordNo + ", field=" + fieldName + ", value=" + newValue + " values=" + Arrays.toString(value));
			} catch (NumberFormatException ex) {
				// ignore malformed field parameters
			}
		});
		vs.editMode = false;
		vs.selectedEditRecord = null;
		Path outputPath = buildEditedFileSnapshot(vs);
		persistEditedFileSnapshot(vs, outputPath);
		commitUpdatedFile(vs, outputPath);
		String redirectViewMode = request.getParameter("viewMode");
		if (redirectViewMode != null) {
			session.setAttribute("viewMode", redirectViewMode);
		}
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (Exception ignore) {
		}
		return "redirect:/viewer/current?page=" + page;
		/* return current(model,session); */
	}

	private Path buildEditedFileSnapshot(ViewerSession vs) throws IOException {
		Path outputPath = vs.sessionDir != null
				? Files.createTempFile(vs.sessionDir, "updated_", ".dat")
				: Files.createTempFile(getAppTempRoot(), "updated_", ".dat");

		try (OutputStream out = Files.newOutputStream(outputPath)) {
			editedFileDownloadService.streamEditedFile(vs.filePath, vs.editOverlay, vs.deletedRecords,
				vs.transactionType, out);
		}
		return outputPath;
	}

	private void writeLegacyEditedFileCopy(Path updatedFile) throws IOException {
		Path legacyOutputPath = Paths.get("C:/edited-files/edited-file.dat");
		Files.createDirectories(legacyOutputPath.getParent());
		Files.copy(updatedFile, legacyOutputPath, StandardCopyOption.REPLACE_EXISTING);
	}

	private void persistEditedFileSnapshot(ViewerSession vs, Path snapshot) throws IOException {
		if (vs != null && vs.s3Enabled) {
			s3FileStorageService.uploadUpdatedFile(snapshot, vs.s3Key);
			return;
		}
		writeLegacyEditedFileCopy(snapshot);
	}

	private void commitUpdatedFile(ViewerSession vs, Path updatedFile) throws Exception {
		if (vs == null || updatedFile == null || vs.transactionType == null || vs.transactionType.isBlank()) {
			return;
		}

		Set<Integer> deletedRecords = vs.deletedRecords != null ? Set.copyOf(vs.deletedRecords) : Set.of();
		int currentRecord = vs.nav != null ? vs.nav.currentRecordNumber() : -1;
		List<Integer> filteredBeforeCommit = vs.lastFiltered != null ? new ArrayList<>(vs.lastFiltered) : List.of();

		if (vs.store instanceof AutoCloseable ac) {
			try {
				ac.close();
			} catch (Exception ignore) {
			}
		}

		long windowBytes = Math.max(1, cfg.getWindowSizeMB()) * 1024L * 1024L;
		int recordLength = SchemaRegistry.getRecordLength(vs.transactionType);
		vs.filePath = updatedFile;
		vs.s3Enabled = s3FileStorageService.isEnabled();
		vs.store = new ChunkedMMapRecordStore(updatedFile, cs(), recordLength, windowBytes, vs.transactionType);

		int workers = Math.min(cfg.getIndexWorkers(), (int) getRecordCount(vs.store));
		vs.pidx = new ParallelPrefixIndexBuilder(vs.store, cfg.getPrefixIndexLength(),
				workers, cfg.getProgressStep(), p -> vs.progress = p).build();
		vs.filter = new FastRecordFilter(vs.store, vs.pidx);

		List<Integer> filteredAfterCommit = renumberAfterDeletes(filteredBeforeCommit, deletedRecords);
		vs.lastFiltered = filteredAfterCommit;

		int newCurrentRecord = renumberAfterDeletes(currentRecord, deletedRecords);
		if (newCurrentRecord <= 0 && !filteredAfterCommit.isEmpty()) {
			newCurrentRecord = filteredAfterCommit.get(0);
		}
		vs.nav = new RecordNavigator(filteredAfterCommit, newCurrentRecord);

		vs.editOverlay.clear();
		vs.deletedRecords.clear();
		vs.hasCommittedChanges = true;
		vs.progress = 1.0;
	}

	private List<Integer> renumberAfterDeletes(List<Integer> recordNumbers, Set<Integer> deletedRecords) {
		if (recordNumbers == null || recordNumbers.isEmpty()) {
			return List.of();
		}
		List<Integer> renumbered = new ArrayList<>(recordNumbers.size());
		for (Integer recordNo : recordNumbers) {
			int newRecordNo = renumberAfterDeletes(recordNo != null ? recordNo : -1, deletedRecords);
			if (newRecordNo > 0) {
				renumbered.add(newRecordNo);
			}
		}
		return renumbered;
	}

	private int renumberAfterDeletes(int recordNo, Set<Integer> deletedRecords) {
		if (recordNo <= 0) {
			return -1;
		}
		if (deletedRecords != null && deletedRecords.contains(recordNo)) {
			return -1;
		}
		int deletedBefore = 0;
		if (deletedRecords != null) {
			for (Integer deletedRecord : deletedRecords) {
				if (deletedRecord != null && deletedRecord > 0 && deletedRecord < recordNo) {
					deletedBefore++;
				}
			}
		}
		return recordNo - deletedBefore;
	}

	@GetMapping("/view")
	public String disableEdit(HttpSession session,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		ViewerSession vs = getSession(session);
		vs.editMode = false;
		vs.selectedEditRecord = null;
		return "redirect:/viewer/current?page=" + page;
	}
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteRecord(@RequestParam("recordNo") int recordNo,
			@RequestParam(name = "viewMode", required = false) String viewMode,
			HttpSession session) throws IOException {
		ViewerSession vs = getSession(session);
		if (vs.deletedRecords == null) {
			vs.deletedRecords = new java.util.HashSet<>();
		}
		vs.deletedRecords.add(recordNo);
		vs.editOverlay.remove(recordNo);
		int nextRecord = updateNavigatorAfterDelete(vs, recordNo);
		vs.selectedEditRecord = null;
		refreshLegacyEditedFileCopy(vs);

		Map<String, Object> result = new HashMap<>();
		result.put("deletedRecord", recordNo);
		result.put("remainingCount", vs.lastFiltered != null ? vs.lastFiltered.size() : 0);
		result.put("position", vs.nav != null ? vs.nav.position() : 0);
		result.put("count", vs.nav != null ? vs.nav.size() : 0);
		result.put("hasPrev", vs.nav != null && vs.nav.hasPrev());
		result.put("hasNext", vs.nav != null && vs.nav.hasNext());
		result.put("currentRecordNo", nextRecord);

		if ("vertical".equals(viewMode)) {
			String html;
			if (nextRecord > 0) {
				SchemaHtmlRenderer renderer = new SchemaHtmlRenderer(vs.store, cs(), vs.transactionType,
					vs.editMode, vs.editOverlay);
				String type = safeInvoke(vs.store, "readType", nextRecord);
				String displayType = type;
				boolean recordHasSccf = hasSccf(vs.transactionType, type.trim());
				String sccf = "";
				if (recordHasSccf) {
					sccf = safeInvoke(vs.store, "readSccf", nextRecord);
				}
				Map<String, String> overlay = vs.editOverlay.get(nextRecord);
				if (overlay != null) {
					displayType = resolveOverlayRecordType(type, overlay);
					if (recordHasSccf) {
						sccf = resolveOverlaySccf(sccf, type, overlay);
					}
				}
				String displaySccf = sccf;
				if (displaySccf != null) {
					displaySccf = displaySccf.replace("{", "0");
				}
				StringBuilder headerBuilder = new StringBuilder();
				headerBuilder.append("<h5 id='record-context'>").append("Record #").append(nextRecord);
				if (recordHasSccf && displaySccf != null && !displaySccf.isBlank()) {
					headerBuilder.append(" SCCF=").append(displaySccf);
				}
				headerBuilder.append(" Type=").append(displayType.trim()).append("</h5>");
				html = headerBuilder.toString() + renderer.renderVertical(nextRecord);
			} else {
				html = "<div class='text-center text-muted p-5'><p>No Records Found</p></div>";
			}
			result.put("verticalHtml", html);
		}

		return result;
	}

	private void refreshLegacyEditedFileCopy(ViewerSession vs) {
		if (vs == null || !vs.hasFile() || vs.transactionType == null || vs.transactionType.isBlank()) {
			return;
		}
		try {
			Path snapshot = buildEditedFileSnapshot(vs);
			persistEditedFileSnapshot(vs, snapshot);
		} catch (IOException ex) {
			LoggingUtil.error(ex);
		}
	}

	private int updateNavigatorAfterDelete(ViewerSession vs, int deletedRecordNo) {
		if (vs.lastFiltered == null) {
			vs.lastFiltered = List.of();
			vs.nav = new RecordNavigator(List.of(), -1);
			return -1;
		}

		List<Integer> newFiltered = new ArrayList<>(vs.lastFiltered);
		int currentIndex = 0;
		if (vs.nav != null) {
			currentIndex = Math.max(0, vs.nav.position() - 1);
		}
		int removedIndex = newFiltered.indexOf(deletedRecordNo);
		if (removedIndex >= 0) {
			newFiltered.remove(removedIndex);
			if (currentIndex > removedIndex) {
				currentIndex--;
			}
		}
		vs.lastFiltered = newFiltered;
		if (currentIndex < 0) {
			currentIndex = 0;
		}
		if (newFiltered.isEmpty()) {
			vs.nav = new RecordNavigator(List.of(), -1);
			return -1;
		}
		if (currentIndex >= newFiltered.size()) {
			currentIndex = newFiltered.size() - 1;
		}
		int nextRecord = newFiltered.get(currentIndex);
		vs.nav = new RecordNavigator(newFiltered, nextRecord);
		return nextRecord;
	}
	@PostMapping("/clear")
	public String clear(HttpSession session) throws IOException {
		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");
		session.removeAttribute("VIEWER_SESSION");
		if (vs != null) {
			LoggingUtil.debug("Clear cache invoked. sessionDir=" + (vs != null ? vs.sessionDir : null));
			try {
				vs.close();
			} catch (Exception ignore) {
				LoggingUtil.error(ignore);
			}

			// 2. Explicitly break references (important on Windows)
			vs.store = null;
			vs.filter = null;
			vs.pidx = null;
			vs.nav = null;
			vs.lastFiltered = null;

			if (vs.sessionDir != null) {
				LoggingUtil.debug("PHASE2_CLEAR: deleting sessionDir=" + vs.sessionDir);

				Path sessionDir = vs.sessionDir;
				vs = null;
				cleanupScheduler.scheduleCleanup(sessionDir);
			}

		}

		return "redirect:/viewer";
	}

	private String preserveOriginalFieldLength(ViewerSession vs, int recordNo, String fieldName, String newValue) {
		if (newValue == null || newValue.isBlank() || fieldName == null || fieldName.isBlank() || vs == null)
			return newValue;

		String recordType = safeInvoke(vs.store, "readType", recordNo).trim();
		List<FieldSpec> layout = SchemaRegistry.getSchema(vs.transactionType, recordType);
		if (layout == null || layout.isEmpty()) {
			return newValue;
		}

		for (FieldSpec f : layout) {
			if (!f.name.equalsIgnoreCase(fieldName)) {
				continue;
			}

			if (newValue.matches("\\d+")) {
				if (isPreserveExactNumericTextField(f)) {
					return newValue;
				}
				if (f.type == FieldType.NUMERIC_TEXT || f.type == FieldType.ALPHA) {
					int length = f.lengthBytes;
					if (newValue.length() < length) {
						return "0".repeat(length - newValue.length()) + newValue;
					}
				}
			}
			break;
		}

		return newValue;
	}

	private boolean isPreserveExactNumericTextField(FieldSpec f) {
		return f != null && f.type == FieldType.NUMERIC_TEXT
				&& f.name != null && f.name.matches("FM3(5A|6A|6B|7A|7B|9A)-SEQ-NUM");
	}

	private String selectFormFieldValue(ViewerSession vs, int recordNo, String fieldName, String viewMode,
			String[] values) {
		if (values == null || values.length == 0) {
			return "";
		}

		FieldSpec fieldSpec = getFieldSpecFromSchema(vs, recordNo, fieldName);
		if (fieldSpec != null && fieldSpec.type == FieldType.BINARY) {
			return selectBinaryFormFieldValue(viewMode, values);
		}

		String longest = "";
		int fieldLength = fieldSpec != null ? fieldSpec.lengthBytes : -1;

		for (String candidate : values) {
			if (candidate == null || candidate.isEmpty()) {
				continue;
			}

			if (fieldLength > 0 && candidate.length() == fieldLength) {
				return candidate;
			}

			if (candidate.length() > longest.length()) {
				longest = candidate;
			}
		}

		if (!longest.isEmpty()) {
			return longest;
		}

		return values[0] != null ? values[0] : "";
	}

	private String selectBinaryFormFieldValue(String viewMode, String[] values) {
		if ("horizontal".equalsIgnoreCase(viewMode)) {
			for (int i = values.length - 1; i >= 0; i--) {
				if (values[i] != null && !values[i].isBlank()) {
					return values[i].trim();
				}
			}
		}

		for (String candidate : values) {
			if (candidate != null && !candidate.isBlank()) {
				return candidate.trim();
			}
		}

		return values[0] != null ? values[0].trim() : "";
	}

	private FieldSpec getFieldSpecFromSchema(ViewerSession vs, int recordNo, String fieldName) {
		if (vs == null || fieldName == null || fieldName.isBlank()) {
			return null;
		}

		String recordType = safeInvoke(vs.store, "readType", recordNo).trim();
		List<FieldSpec> layout = SchemaRegistry.getSchema(vs.transactionType, recordType);
		if (layout == null || layout.isEmpty()) {
			return null;
		}

		for (FieldSpec f : layout) {
			if (f.name.equalsIgnoreCase(fieldName)) {
				return f;
			}
		}

		return null;
	}

	private long getRecordCount(Object store) {
		try {
			return (long) store.getClass().getMethod("getRecordCount").invoke(store);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/download")
	public void downloadEditedFile(jakarta.servlet.http.HttpServletResponse resp, HttpSession session)
			throws Exception {
		ViewerSession vs = getSession(session);

		if (!vs.hasFile() || !hasPendingChanges(vs)) {
			resp.sendError(400, "No file loaded or no edits/deletions to download.");
			return;
		}

		String originalName = (vs.originalFilename != null && !vs.originalFilename.isBlank()) ? vs.originalFilename
				: "edited-file.dat";
		String filename = buildTimestampedFilename(originalName);

		boolean hasPendingChanges = !vs.editOverlay.isEmpty() || !vs.deletedRecords.isEmpty();
		Path downloadPath = hasPendingChanges ? buildEditedFileSnapshot(vs) : vs.filePath;
		if (vs.s3Enabled) {
			if (hasPendingChanges) {
				s3FileStorageService.uploadUpdatedFile(downloadPath, vs.s3Key);
			}
			Path s3DownloadPath = vs.sessionDir != null
					? Files.createTempFile(vs.sessionDir, "s3_download_", ".dat")
					: Files.createTempFile(getAppTempRoot(), "s3_download_", ".dat");
			s3FileStorageService.downloadToFile(vs.s3Key, s3DownloadPath);
			downloadPath = s3DownloadPath;
		} else {
			writeLegacyEditedFileCopy(downloadPath);
		}

		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		resp.setContentLengthLong(Files.size(downloadPath));

		Files.copy(downloadPath, resp.getOutputStream());
	}

	private String buildTimestampedFilename(String originalName) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String baseName = originalName;
		String extension = "";
		int dotIdx = originalName.lastIndexOf('.');
		if (dotIdx > 0) {
			baseName = originalName.substring(0, dotIdx);
			extension = originalName.substring(dotIdx);
		}
		if (baseName.matches(".*_20\\d{6}_[012]\\d[0-5]\\d[0-5]\\d$")) {
			baseName = baseName.substring(0, baseName.length() - 16);
		} else if (baseName.matches(".*_\\d{8}_\\d{6}$")) {
			baseName = baseName.substring(0, baseName.length() - 16);
		}
		return baseName + "_" + timestamp + extension;
	}

	private Path getAppTempRoot() throws IOException {
		Path root = Paths.get(System.getProperty("java.io.tmpdir"), "its255Files");
		Files.createDirectories(root);
		return root;
	}

	/**
	 * Determines whether a given record type has SCCF fields for the transaction
	 * type.
	 */
	private boolean hasSccf(String transactionType, String recordTypeCode) {
		switch (transactionType) {
			case "PPU":
			case "PPA":
				return false;
			case "CBF":
				return "7A".equals(recordTypeCode) || "7B".equals(recordTypeCode);
			case "SF":
			case "SFI":
				return !"9D".equals(recordTypeCode);
			default: // SFP, DF, RF, CBFBD
				return true;
		}
	}
}
