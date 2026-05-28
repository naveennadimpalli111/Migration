package com.its255.web.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/viewer")
public class FastViewerController {
	private static final int HORIZONTAL_PAGE_SIZE = 5;

	private final ViewerConfig cfg;
	private final CleanupScheduler cleanupScheduler;
	private final EditedFileDownloadService editedFileDownloadService;

	public FastViewerController(ViewerConfig cfg, CleanupScheduler cleanupScheduler) {
		this.cfg = cfg;
		this.cleanupScheduler = cleanupScheduler;
		this.editedFileDownloadService = new EditedFileDownloadService();
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
		model.addAttribute("html", "");
		model.addAttribute("hasEdits", !vs.editOverlay.isEmpty());

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

		// save paths in session
		vs.sessionDir = sessionDir;
		vs.filePath = uploadedFile;
		vs.originalFilename = file.getOriginalFilename();
		vs.progress = 0.0;

		long windowBytes = Math.max(1, cfg.getWindowSizeMB()) * 1024L * 1024L;

		int recordLength = SchemaRegistry.getRecordLength(transactionType);

		vs.store = new ChunkedMMapRecordStore(uploadedFile, cs(), recordLength, windowBytes, transactionType);

		// TEMP debug print (remove later)
		LoggingUtil.debug("Viewer upload: sessionDir=" + sessionDir + ", filePath=" + uploadedFile);

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

		if (!vs.hasFile() || vs.nav == null || vs.nav.size() == 0) {
			model.addAttribute("html", "<div class='alert alert-info'>No results. Upload a file and search.</div>");
			return page(model, session);
		}

		int rn = vs.nav.currentRecordNumber();

		// Base values
		String type = safeInvoke(vs.store, "readType", rn);

		boolean recordHasSccf = hasSccf(vs.transactionType, type.trim());

		String sccf = "";
		if (recordHasSccf) {
			sccf = safeInvoke(vs.store, "readSccf", rn);
		}

		String txn = safeInvoke(vs.store, "readTxn", rn);

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

			// TYPE override
			type = overlay.getOrDefault("REC_TYPE", type);
			type = overlay.getOrDefault("FM105-REC-TYPE", type);

			// SCCF logic
			if (recordHasSccf) {

				// direct value
				sccf = overlay.getOrDefault("SCCF", sccf);

				// prefix-based rebuild (your logic)
				String prefix = "FM1" + type.trim() + "-SER-NUM-";

				String localPlan = overlay.getOrDefault(prefix + "LOCAL-PLAN", "");
				String cc = overlay.getOrDefault(prefix + "JULDT-CC", "");
				String yy = overlay.getOrDefault(prefix + "JULDT-YY", "");
				String ddd = overlay.getOrDefault(prefix + "JULDT-DDD", "");
				String sequence = overlay.getOrDefault(prefix + "SEQUENCE", "");
				String suffix = overlay.getOrDefault(prefix + "SUFFIX", "");

				String rebuilt = localPlan + cc + yy + ddd + sequence + suffix;

				if (!rebuilt.trim().isEmpty()) {
					sccf = rebuilt;
				}
			}
		}

		String displaySccf = sccf;

		// SAFE HEADER (FIXED)
		StringBuilder headerBuilder = new StringBuilder();

		headerBuilder.append("<h5 id='record-context'>").append("Record #").append(rn);

		if (recordHasSccf && displaySccf != null && !displaySccf.isBlank()) {
			headerBuilder.append(" SCCF=").append(displaySccf);
		}

		headerBuilder.append(" Type=").append(type.trim()).append("</h5>");

		String dynamicHeader = headerBuilder.toString();

		// Vertical HTML
		String dynamicVerticalHtml = dynamicHeader + renderer.renderVertical(rn);

		// Model attributes (unchanged)
		model.addAttribute("hasFile", true);
		model.addAttribute("filename", vs.originalFilename);
		model.addAttribute("count", vs.nav.size());
		model.addAttribute("position", vs.nav.position());
		model.addAttribute("hasPrev", vs.nav.hasPrev());
		model.addAttribute("hasNext", vs.nav.hasNext());

		model.addAttribute("html", dynamicHeader + tableHtml);

		model.addAttribute("editMode", vs.editMode);
		model.addAttribute("hasEdits", !vs.editOverlay.isEmpty());

		model.addAttribute("verticalHtml", dynamicVerticalHtml);

		model.addAttribute("horizontalHtml",
				renderer.renderHorizontalPage(session, vs.selectedRecordType, horizontalOffset, HORIZONTAL_PAGE_SIZE));

		model.addAttribute("horizontalPage", page);
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

	private ViewerSession getSession(HttpSession session) {
		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");
		if (vs == null) {
			vs = new ViewerSession();
			session.setAttribute("VIEWER_SESSION", vs);
		}
		return vs;
	}

	private String safeInvoke(Object store, String method, int rn) {
		try {
			return String.valueOf(store.getClass().getMethod(method, int.class).invoke(store, rn));
		} catch (Exception e) {
			return "";
		}
	}

	@GetMapping("/edit")
	public String enableEdit(HttpSession session) {
		ViewerSession vs = getSession(session);
		vs.editMode = true;
		return "redirect:/viewer/current";
	}

	@PostMapping("/save")

	public String save(HttpServletRequest request, HttpSession session, Model model) {
		ViewerSession vs = getSession(session);
		String viewMode = request.getParameter("viewMode");
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
					if ("horizontal".equalsIgnoreCase(viewMode)) {
						for (int i = value.length - 1; i >= 0; i--) {
							if (value[i] != null && !value[i].isEmpty()) {
								newValue = value[i];
								break;
							}
						}
					} else {
						for (int i = 0; i < value.length; i++) {
							if (value[i] != null && !value[i].isEmpty()) {
								newValue = value[i];
								break;
							}
						}
					}
					if (newValue.isEmpty()) {
						newValue = value[0];
					}
				}
				Map<String, String> recordEdits = vs.editOverlay.computeIfAbsent(recordNo, k -> new HashMap<>());
				recordEdits.put(fieldName, newValue);
				System.out.println("OVERLAY SAVED: viewMode=" + viewMode + ", record=" + recordNo + ", field=" + fieldName + ", value=" + newValue + " values=" + Arrays.toString(value));
			} catch (NumberFormatException ex) {
				// ignore malformed field parameters
			}
		});
		vs.editMode = false;
		try {

			Path outputPath = Paths.get("C:/edited-files/edited-file.dat");
			Files.createDirectories(outputPath.getParent());

			byte[] fileBytes = Files.readAllBytes(vs.filePath);

			int recordLength = SchemaRegistry.getRecordLength(vs.transactionType);

			Charset ebcdic = Charset.forName(cfg.getCharset()); // your config charset

			for (Map.Entry<Integer, Map<String, String>> entry : vs.editOverlay.entrySet()) {

				int recordNo = entry.getKey();
				Map<String, String> fields = entry.getValue();

				int recordOffset = (recordNo - 1) * recordLength;

				String type = new String(fileBytes, recordOffset, 2, ebcdic).trim();
				List<FieldSpec> layout = SchemaRegistry.getSchema(vs.transactionType, type);

				if (layout == null)
					continue;

				for (FieldSpec f : layout) {

					if (!fields.containsKey(f.name))
						continue;

					String newValue = fields.get(f.name);
					int start = recordOffset + (f.start1Based - 1);
					int len = f.lengthBytes;
					byte[] ebcdicBytes = newValue.getBytes(ebcdic);
					byte[] finalBytes = new byte[len];
					Arrays.fill(finalBytes, (byte) 0x40); // EBCDIC space

					System.arraycopy(ebcdicBytes, 0, finalBytes, 0, Math.min(len, ebcdicBytes.length));

					System.arraycopy(finalBytes, 0, fileBytes, start, len);
				}
			}

			Files.write(outputPath, fileBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@GetMapping("/view")
	public String disableEdit(HttpSession session) {
		ViewerSession vs = getSession(session);
		vs.editMode = false;
		return "redirect:/viewer/current";
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

		if (!vs.hasFile() || vs.editOverlay.isEmpty()) {
			resp.sendError(400, "No file loaded or no edits to download.");
			return;
		}

		String originalName = (vs.originalFilename != null && !vs.originalFilename.isBlank()) ? vs.originalFilename
				: "edited-file.dat";
		String filename = buildTimestampedFilename(originalName);

		long fileSize = editedFileDownloadService.getEditedFileSize(vs.filePath);

		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		resp.setContentLengthLong(fileSize);

		editedFileDownloadService.streamEditedFile(vs.filePath, vs.editOverlay, vs.transactionType,
				resp.getOutputStream());
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