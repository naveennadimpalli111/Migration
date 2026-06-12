package com.its255.viewer;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.its255.constants.FileViewerConstants;
import com.its255.schema.FieldSpec;
import com.its255.schema.FieldType;
import com.its255.schema.RecordType;
import com.its255.schema.SchemaRegistry;
import com.its255.util.FieldValueNormalizer;

import jakarta.servlet.http.HttpSession;

/**
 * Renders ONE 255-byte record as an HTML table using the active copybook
 * schema. - Supports ALPHA / NUMERIC_TEXT via Cp037 slicing + trim of EBCDIC
 * spaces (0x40) - Supports PACKED_DECIMAL and BINARY via Fixed255Parser
 * (reflection)
 *
 * This class depends on the host app providing: com.its255.schema.{Schemas,
 * RecordType, FieldSpec} com.its255.io.Fixed255Parser with methods:
 * decodeComp3ToString(byte[], int start, int len, int scale)
 * decodeBinary(byte[], int start, int len, int scale)
 */
public class SchemaHtmlRenderer {
	private static final byte EBCDIC_SPACE = (byte) 0x40;

	private final Object store; // must expose: readRecordBytes(int), readType(int)
	private final Charset cs;
	private final String transactionType;
	private final boolean editMode;
	private final Map<Integer, Map<String, String>> editOverlay;

	public SchemaHtmlRenderer(Object store, Charset cs, String transactionType, boolean editMode,
			Map<Integer, Map<String, String>> editOverlay) {
		this.store = store;
		this.cs = cs;
		this.transactionType = transactionType;
		this.editMode = editMode;
		this.editOverlay = editOverlay;
	}

	/**
	 * simple vertical render that delagets to the existing `render(...)
	 * `implementation.
	 */

	public String renderVertical(int recordNo) {
		return render(recordNo);
	}

	public String renderVerticalPage(HttpSession session, String selectedType, int offset, int limit) {

		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");

		List<?> records = vs.lastFiltered;

		if (records == null || records.isEmpty()) {
			return "";
		}

		int total = records.size();
		int end = Math.min(offset + limit, total);
		if (offset >= total) {
			return "";
		}

		StringBuilder sb = new StringBuilder(8192);

		for (int i = offset; i < end; i++) {

			int recordNo = Integer.parseInt(records.get(i).toString());

			sb.append(renderVertical(recordNo));
		}

		return sb.toString();
	}

	public String renderHorizontal(HttpSession session, String selectedType, List<?> filteredRecords) {
		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");

		StringBuilder sb = new StringBuilder(8192);

		/* sb.append("<div style='max-height:700px;overflow:auto;'>"); */
		sb.append("<table class='table table-sm table-striped table-bordered table-hover' ")
				.append("style='min-width:2200px; border-top:3px solid black;'>");

		sb.append("<thead class='sticky-head'><tr>");
		sb.append("<th style='min-width:180px; border-top:3px;'>Record No</th>");
		sb.append("<th style='min-width:180px; border-top:3px;'>SCCF ID</th>");
		sb.append("<th style='min-width:120px; border-top:3px;'>Type</th>");
		List<FieldSpec> headerLayout = SchemaRegistry.getSchema(transactionType,
				(selectedType != null && !selectedType.isEmpty()) ? selectedType : "05");

		if (headerLayout != null) {
			for (FieldSpec f : headerLayout) {
				if ("SCCF".equalsIgnoreCase(f.name) || "REC_TYPE".equalsIgnoreCase(f.name)) {
					continue;
				}

				sb.append("<th>").append(escape(f.name)).append("</th>");

			}
		}

		sb.append("</tr></thead><tbody id='horizontalTbody'>");

		if (filteredRecords == null || filteredRecords.isEmpty()) {
			sb.append("<tr>").append("<td colspan='10'>No data found</td>").append("</tr>");
		} else {
			for (Object obj : filteredRecords) {
				int recordNo = Integer.parseInt(obj.toString());

				byte[] rec = readRecordBytes(recordNo);
				String type = readType(recordNo).trim(); // Original form type - ALWAYS use for SCCF prefix

				Map<String, String> recordOverlay = editOverlay != null ? editOverlay.get(recordNo) : null;
				String displayType = type; // For display (may be overridden)

				RecordType rt = RecordType.from(type);
				List<FieldSpec> rowLayout = SchemaRegistry.getSchema(transactionType, rt.code);

				sb.append("<tr>");
				sb.append("<td>").append(recordNo).append("</td>");

				String sccf = sliceTrim(rec, 0, 15); // adjust if needed

				if (recordOverlay != null) {
					sccf = resolveOverlaySccf(sccf, type, recordOverlay);
					displayType = resolveOverlayRecordType(displayType, recordOverlay);
				}

				sb.append("<td>").append(escape(sccf)).append("</td>");

				sb.append("<td>").append(escape(displayType)).append("</td>");

				if (rowLayout != null) {
					for (FieldSpec f : rowLayout) {

						if ("SCCF".equalsIgnoreCase(f.name) || "REC_TYPE".equalsIgnoreCase(f.name)) {
							continue;
						}

						int start = f.start1Based - 1;
						int len = f.lengthBytes;

						String val = "";

						switch (f.type) {
							case ALPHA:
							case NUMERIC_TEXT:
								val = sliceTrim(rec, start, len);
								break;
							case PACKED_DECIMAL:
								val = invokeFixed("decodeComp3ToString", rec, start, len, f.scale);
								break;
							case BINARY:
								val = invokeFixed("decodeBinary", rec, start, len, f.scale);
								break;
							default:
								val = "";
						}

						if (recordOverlay != null && recordOverlay.containsKey(f.name)) {
							val = recordOverlay.get(f.name);
						}
						val = FieldValueNormalizer.normalize(f, val);
						// Normalize special placeholder '{' to '0' for display
						val = normalizeValue(val);
						boolean editable = editMode && isEditableField(f);

						sb.append("<td>");
						if (editable) {
							appendEditableInput(sb, recordNo, f, val);
							sb.append("<div class='invalid-feedback' style='display:none;'></div>");
						} else {
							sb.append(escape(val));
						}

						sb.append("</td>");

					}
				}

				sb.append("</tr>");
			}
		}

		sb.append("</tbody></table>");

		return sb.toString();
	}

	public String renderHorizontalPage(HttpSession session, String selectedType, int offset, int limit, int page) {

		ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");

		List<?> records = vs.lastFiltered;

		if (records == null || records.isEmpty()) {
			return "";
		}

		int total = records.size();

		if (offset >= total) {
			return "";
		}

		int end = Math.min(offset + limit, total);

		StringBuilder sb = new StringBuilder();

		sb.append("<table class='table table-sm table-striped table-bordered table-hover' ")
				.append("style='min-width:2200px; border-top:3px solid black;'>");

		sb.append("<thead class='sticky-head'><tr>");

		sb.append("<th style='min-width:180px;'>Record No</th>");
		sb.append("<th style='min-width:160px;'>Actions</th>");
		sb.append("<th style='min-width:180px;'>SCCF ID</th>");
		sb.append("<th style='min-width:120px;'>Type</th>");

		List<FieldSpec> headerLayout = SchemaRegistry.getSchema(transactionType,
				(selectedType != null && !selectedType.isEmpty()) ? selectedType : "05");

		if (headerLayout != null) {

			for (FieldSpec f : headerLayout) {

				if ("SCCF".equalsIgnoreCase(f.name) || "REC_TYPE".equalsIgnoreCase(f.name)) {
					continue;
				}

				sb.append("<th>").append(escape(f.name)).append("</th>");
			}
		}

		sb.append("</tr></thead><tbody id='horizontalTbody'>");

		for (int i = offset; i < end; i++) {

			int recordNo = Integer.parseInt(records.get(i).toString());

			byte[] rec = readRecordBytes(recordNo);

			String type = readType(recordNo).trim(); // Original form type (05, 10, 15, etc.) - ALWAYS use this for SCCF prefix

			Map<String, String> recordOverlay = editOverlay != null ? editOverlay.get(recordNo) : null;
			String displayType = type; // Type to display (may be overridden by REC_TYPE, *-REC-TYPE, or *-CLM-TYPE)

			RecordType rt = RecordType.from(type);

			List<FieldSpec> layout = SchemaRegistry.getSchema(transactionType, rt.code);

			boolean isEditingRecord = editMode && vs.selectedEditRecord != null && vs.selectedEditRecord == recordNo;

			// sb.append("<tr>");
			sb.append("<tr data-recordno='").append(recordNo).append("'>");

			sb.append("<td>").append(recordNo).append("</td>");

			// Actions column with Edit/Delete or Save/Cancel for the active edit row
			sb.append("<td>");
			if (isEditingRecord) {
				sb.append("<button type='submit' form='saveForm' class='btn btn-sm btn-primary me-1'>");
				sb.append("<i class='bi bi-save'></i> Save");
				sb.append("</button>");
				sb.append("<button type='button' class='btn btn-sm btn-secondary' onclick=\"window.location.href='/viewer/view?page=").append(page).append("'\">");
				sb.append("<i class='bi bi-x-circle'></i> Cancel");
				sb.append("</button>");
			} else {
				sb.append("<button type='button' class='btn btn-sm btn-outline-primary me-1' onclick='editHorizontalRecord(").append(recordNo).append(")'>");
				sb.append("<i class='bi bi-pencil'></i> Edit");
				sb.append("</button>");
				sb.append("<button type='button' class='btn btn-sm btn-outline-danger' onclick='deleteHorizontalRecord(").append(recordNo).append(")'>");
				sb.append("<i class='bi bi-trash'></i> Delete");
				sb.append("</button>");
			}
			sb.append("</td>");

			String sccf = sliceTrim(rec, 0, 15);

			if (recordOverlay != null) {
				sccf = resolveOverlaySccf(sccf, type, recordOverlay);
				displayType = resolveOverlayRecordType(displayType, recordOverlay);
			}

			// Normalize special placeholder '{' to '0' for display
			sccf = normalizeValue(sccf);
			sb.append("<td>").append(escape(sccf)).append("</td>");

			sb.append("<td>").append(escape(displayType)).append("</td>");
			if (layout != null) {
				for (FieldSpec f : layout) {
					if ("SCCF".equalsIgnoreCase(f.name) || "REC_TYPE".equalsIgnoreCase(f.name)) {
						continue;
					}

					int start = f.start1Based - 1;
					int len = f.lengthBytes;

					String val = switch (f.type) {

						case ALPHA, NUMERIC_TEXT -> sliceTrim(rec, start, len);

						case PACKED_DECIMAL -> invokeFixed("decodeComp3ToString", rec, start, len, f.scale);

						case BINARY -> invokeFixed("decodeBinary", rec, start, len, f.scale);

						default -> "";
					};

					if (recordOverlay != null && recordOverlay.containsKey(f.name)) {
						val = recordOverlay.get(f.name);
					}

					val = FieldValueNormalizer.normalize(f, val);
					// Normalize special placeholder '{' to '0' for display
					val = normalizeValue(val);

					boolean editable = isEditingRecord && isEditableField(f);

					sb.append("<td>");
					if (editable) {
						appendEditableInput(sb, recordNo, f, val);
						sb.append("<div class='invalid-feedback' style='display:none;'></div>");
					} else {
						sb.append(escape(val));
					}

					sb.append("</td>");
				}
			}

			sb.append("</tr>");
		}

		sb.append("</tbody></table>");

		return sb.toString();
	}

	private int getRecordCount() {
		try {
			return (Integer) store.getClass().getMethod("recordCount").invoke(store);
		} catch (Exception e) {
			return 0;
		}
	}

	private String row(String name, String value, int recordNo, FieldSpec fieldSpec) {
		// Normalize before display: treat '{' as '0'
		value = normalizeValue(value);
		boolean editable = editMode && isEditableField(fieldSpec);
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append("<th scope='row' style='white-space:nowrap'>").append(escape(name)).append("</th>");
		sb.append("<td>");
		if (editable) {
			appendEditableInput(sb, recordNo, fieldSpec, value);
			sb.append("<div class='invalid-feedback' style='display:none;'></div>");
		} else {
			sb.append("<pre style='margin:0'>").append(escape(value)).append("</pre>");
		}
		sb.append("</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	public String render(int recordNumber1Based) {
		String type = readType(recordNumber1Based).trim();
		RecordType rt = RecordType.from(type);
		List<FieldSpec> layout = SchemaRegistry.getSchema(transactionType, rt.code);
		byte[] rec = readRecordBytes(recordNumber1Based);

		StringBuilder sb = new StringBuilder(8_192);
		int totalFields = (layout == null || layout.isEmpty()) ? 2 : layout.size();
		sb.append("<div class='table-responsive'>\n" + "<table id='verticalFieldsTable' data-total-fields='"
				+ totalFields + "' " + "aria-labelledby='record-context' "
				+ "class='table table-sm table-striped table-bordered'>\n");

		sb.append(
				"<thead><tr><th scope='col' style='white-space:nowrap'>Field</th><th scope='col'>Value</th></tr></thead><tbody>\n");

		if (layout == null || layout.isEmpty()) {
			String recTypeValue = type;
			String byteLenValue = String.valueOf(rec.length);
			Map<String, String> recordOverlay = editOverlay != null ? editOverlay.get(recordNumber1Based) : null;
			if (recordOverlay != null) {
				recTypeValue = resolveOverlayRecordType(recTypeValue, recordOverlay);
				byteLenValue = recordOverlay.getOrDefault("BYTE_LEN", byteLenValue);
			}
			sb.append(row("REC_TYPE", recTypeValue, recordNumber1Based, null));
			sb.append(row("BYTE_LEN", byteLenValue, recordNumber1Based, null));

		} else {
			for (FieldSpec f : layout) {
				int start = f.start1Based - 1;
				int len = f.lengthBytes;
				String val = null;
				switch (f.type) {
					case ALPHA:
						val = sliceTrim(rec, start, len);
						break;
					case NUMERIC_TEXT:
						val = sliceTrim(rec, start, len);
						break;
					case PACKED_DECIMAL:
						val = invokeFixed("decodeComp3ToString", rec, start, len, f.scale);
						break;
					case BINARY:
						val = invokeFixed("decodeBinary", rec, start, len, f.scale);
						break;
					default:
						val = "";
				}
				Map<String, String> recordOverlay = editOverlay != null ? editOverlay.get(recordNumber1Based) : null;

				if (recordOverlay != null && recordOverlay.containsKey(f.name)) {

					val = recordOverlay.get(f.name);
				}
				val = FieldValueNormalizer.normalize(f, val);
				// Normalize before rendering (treat '{' as '0')
				val = normalizeValue(val);
				sb.append(row(f.name, val, recordNumber1Based, f));
			}
		}

		sb.append("</tbody></table></div>\n");
		return sb.toString();
	}

	private String row(String name, String value) {
		return new StringBuilder().append("<tr><th scope='row' style='white-space:nowrap'>").append(escape(name))
				.append("</th><td><pre style='margin:0'>").append(value).append("</pre></td></tr>\n").toString();
	}

	private String sliceTrim(byte[] a, int off, int len) {
		int end = Math.min(a.length, off + len);
		int i = end - 1;
		while (i >= off && a[i] == EBCDIC_SPACE)
			i--;
		int newLen = (i < off) ? 0 : (i - off + 1);
		return new String(a, off, Math.max(0, newLen), cs);
	}

	private String invokeFixed(String method, byte[] rec, int start, int len, int scale) {
		try {
			Class<?> cls = Class.forName("com.its255.io.Fixed255Parser");
			Method m = cls.getDeclaredMethod(method, byte[].class, int.class, int.class, int.class);
			// It's private – make it accessible
			m.setAccessible(true);

			Object val = m.invoke(null, rec, start, len, scale);// Catching Exception for Binary
			return (val == null) ? "" : String.valueOf(val);
		} catch (Exception ex) {
			// ex.printStackTrace();
			return "";
		}
	}

	private byte[] readRecordBytes(int rn) {
		try {
			return (byte[]) store.getClass().getMethod("readRecordBytes", int.class).invoke(store, rn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String readType(int rn) {
		if (transactionType.equals(FileViewerConstants.CBFBD)) {
			return FileViewerConstants.CBFBD;
		} else {
			try {
				return String.valueOf(store.getClass().getMethod("readType", int.class).invoke(store, rn));
			} catch (Exception e) {
				return "";
			}
		}
	}

	private static String escape(String s) {
		if (s == null)
			return "";
		return s.replace("&", "&amp;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("'", "&#39;");
	}

	private static String escapeAttribute(String s) {
		if (s == null)
			return "";
		return s.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("'", "&#39;")
				.replace("\"", "&quot;");
	}

	/**
	 * Normalize values before display: treat single '{' as '0'.
	 */
	private static String normalizeValue(String s) {
		if (s == null) return "";
		if (s.equals("{")) return "0";
		return s;
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
			if (upper.endsWith("REC_TYPE") || upper.endsWith("REC_TYPE")) {
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
			String localPlan = normalizeValue(overlay.getOrDefault(prefix + "LOCAL-PLAN", ""));
			String cc = normalizeValue(overlay.getOrDefault(prefix + "JULDT-CC", ""));
			String yy = normalizeValue(overlay.getOrDefault(prefix + "JULDT-YY", ""));
			String ddd = normalizeValue(overlay.getOrDefault(prefix + "JULDT-DDD", ""));
			String sequence = normalizeValue(overlay.getOrDefault(prefix + "SEQUENCE", ""));
			String suffix = normalizeValue(overlay.getOrDefault(prefix + "SUFFIX", ""));

			String rebuilt = localPlan + cc + yy + ddd + sequence + suffix;
			if (!rebuilt.trim().isEmpty()) {
				return rebuilt;
			}
		}

		return currentSccf;
	}

	private static Integer parseOverpunchInt(String s) {
		if (s == null || s.isEmpty())
			return null;
		char last = s.charAt(s.length() - 1);
		String body = s.substring(0, s.length() - 1);

		if (last >= '0' && last <= '9') {
			return Integer.parseInt(body + last);
		}

		Integer d;
		boolean neg = false;
		if ((d = POS.get(last)) != null) {
			neg = false;
		} else if ((d = NEG.get(last)) != null) {
			neg = true;
		} else {
			System.err.println("Invalid overpunch char: " + last);
			return null;
		}
		int value = Integer.parseInt(body + d);
		return neg ? -value : value;
	}

	private static boolean isEditableField(FieldSpec fieldSpec) {
		if (fieldSpec == null) {
			return false;
		}
		return fieldSpec.type != FieldType.BINARY || isEditableBinaryField(fieldSpec);
	}

	private static boolean isEditableBinaryField(FieldSpec fieldSpec) {
		return fieldSpec != null && ("FM1A5-SEQ-NUM".equalsIgnoreCase(fieldSpec.name)
				|| "FM1F0-SEQ-NUM".equalsIgnoreCase(fieldSpec.name));
	}

	private static int maxInputLength(FieldSpec fieldSpec) {
		if (fieldSpec != null && fieldSpec.type == FieldType.BINARY) {
			return String.valueOf(maxSignedBinaryValue(fieldSpec.lengthBytes)).length();
		}
		return fieldSpec != null ? fieldSpec.lengthBytes : 0;
	}

	private static long maxSignedBinaryValue(int lengthBytes) {
		if (lengthBytes <= 0) {
			return 0L;
		}
		if (lengthBytes >= Long.BYTES) {
			return Long.MAX_VALUE;
		}
		return (1L << (lengthBytes * 8 - 1)) - 1L;
	}

	private static void appendEditableInput(StringBuilder sb, int recordNo, FieldSpec fieldSpec, String value) {
		String encodedFieldName = URLEncoder.encode(fieldSpec.name, StandardCharsets.UTF_8).replace("+", "%20");
		int maxLength = maxInputLength(fieldSpec);
		sb.append("<input type=\"text\"")
			.append(" class=\"form-control form-control-sm editable-field\" ")
			.append("name=\"field_").append(recordNo).append("_").append(encodedFieldName).append("\" ")
			.append("value=\"").append(escapeAttribute(value)).append("\" ")
			.append("maxlength=\"").append(maxLength).append("\" ")
			.append("data-ftype=\"").append(escapeAttribute(fieldSpec.type.name())).append("\" ")
			.append("data-flen=\"").append(maxLength).append("\" ")
			.append("data-fscale=\"").append(fieldSpec.scale).append("\" ");
		if (fieldSpec.type == FieldType.BINARY) {
			sb.append("data-binary-editable=\"true\" ")
				.append("data-binary-max=\"").append(maxSignedBinaryValue(fieldSpec.lengthBytes)).append("\" ")
				.append("inputmode=\"numeric\" ");
		} else if (fieldSpec.type == FieldType.NUMERIC_TEXT) {
			sb.append("inputmode=\"numeric\" ");
		}
		sb.append("/>");
	}

	/** Safer overpunch decoder that returns null for invalid inputs. */
	private static Integer parseOverpunchIntSafe(String s) {
		if (s == null || s.isEmpty())
			return null;
		char last = s.charAt(s.length() - 1);
		String body = s.substring(0, s.length() - 1);
		if (last >= '0' && last <= '9') {
			try {
				return Integer.parseInt(body + last);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		Integer d = POS.get(last);
		boolean neg = false;
		if (d == null) {
			d = NEG.get(last);
			if (d != null)
				neg = true;
		}
		if (d == null)
			return null;
		try {
			int value = Integer.parseInt(body + d);
			return neg ? -value : value;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String decodeZonedDecimal(byte[] rec, int off, int len) {
		boolean negative = false;
		StringBuilder digits = new StringBuilder(len);

		for (int i = 0; i < len; i++) {
			int b = rec[off + i] & 0xFF;
			int hi = (b >>> 4) & 0x0F;
			int lo = b & 0x0F;

			if (i == len - 1) {
				// Sign comes from high nibble
				if (hi == 0xD)
					negative = true;
				digits.append(lo);
			} else {
				digits.append(lo);
			}
		}

		String val = digits.toString();
		// Strip leading zeros but keep "0"
		val = val.replaceFirst("^0+(?!$)", "");

		return negative ? "-" + val : val;
	}

	static final Map<Character, Integer> POS = Map.ofEntries(Map.entry('{', 0), Map.entry('A', 1), Map.entry('B', 2),
			Map.entry('C', 3), Map.entry('D', 4), Map.entry('E', 5), Map.entry('F', 6), Map.entry('G', 7),
			Map.entry('H', 8), Map.entry('I', 9), Map.entry('X', 0)// Checking on this char
	);

	static final Map<Character, Integer> NEG = Map.ofEntries(Map.entry('}', 0), Map.entry('J', 1), Map.entry('K', 2),
			Map.entry('L', 3), Map.entry('M', 4), Map.entry('N', 5), Map.entry('O', 6), Map.entry('P', 7),
			Map.entry('Q', 8), Map.entry('R', 9), Map.entry('X', 0)// Checking on this char
	);

}
