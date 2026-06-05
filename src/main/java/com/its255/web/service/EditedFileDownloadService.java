package com.its255.web.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.SchemaRegistry;

/**
* Service that produces a downloadable edited file by streaming records
* one at a time from the original file, applying in-memory overlay edits,
* and encoding values to CP037/COMP-3/Binary as appropriate.
*
* Streaming approach:
* - Reads one record (255 bytes) at a time from disk
* - Applies edits if present for that record
* - Writes directly to the output stream
* - Memory usage: constant ~512 bytes regardless of file size
* - Supports files of any size (no 2 GB array limit)
*
* The original file is never modified.
*/
public class EditedFileDownloadService {

private static final Charset CP037 = Charset.forName("Cp037");
private static final byte EBCDIC_SPACE = (byte) 0x40;

public void streamEditedFile(Path originalFile,
Map<Integer, Map<String, String>> editOverlay,
String transactionType,
OutputStream out) throws IOException {

int recordLength = SchemaRegistry.getRecordLength(transactionType);
long fileSize = Files.size(originalFile);
int totalRecords = (int) (fileSize / recordLength);

byte[] recordBuffer = new byte[recordLength];
BufferedOutputStream bufferedOut = new BufferedOutputStream(out, 8192);

try (RandomAccessFile raf = new RandomAccessFile(originalFile.toFile(), "r")) {
for (int recNum = 1; recNum <= totalRecords; recNum++) {
raf.readFully(recordBuffer);

Map<String, String> edits = editOverlay.get(recNum);
if (edits != null && !edits.isEmpty()) {
applyEditsToRecord(recordBuffer, edits, transactionType, recordLength);
}

bufferedOut.write(recordBuffer, 0, recordLength);
}
}

bufferedOut.flush();
}

public long getEditedFileSize(Path originalFile) throws IOException {
return Files.size(originalFile);
}

private void applyEditsToRecord(byte[] record, Map<String, String> edits,
String transactionType, int recordLength) {
String type = readTypeFromRecord(record, transactionType);
RecordType rt = RecordType.from(type.trim());
List<FieldSpec> layout = SchemaRegistry.getSchema(transactionType, rt.code);

// First: handle SCCF reconstruction and REC_TYPE overrides so they are present
// before per-field writes (per-field writes may overwrite components).
try {
// Rebuild SCCF: prefer direct SCCF key (case-insensitive), else keys containing SCCF,
// else assemble from prefix components (FM1<type>-SER-NUM-...)
String sccf = null;
for (String k : edits.keySet()) {
if (k != null && k.equalsIgnoreCase("SCCF")) { sccf = edits.get(k); break; }
}
if (sccf == null) {
for (String k : edits.keySet()) {
if (k != null && k.toUpperCase().contains("SCCF")) { sccf = edits.get(k); break; }
}
}
if (sccf == null) {
String prefix = "FM1" + type.trim() + "-SER-NUM-";
String localPlan = edits.getOrDefault(prefix + "LOCAL-PLAN", "");
String cc = edits.getOrDefault(prefix + "JULDT-CC", "");
String yy = edits.getOrDefault(prefix + "JULDT-YY", "");
String ddd = edits.getOrDefault(prefix + "JULDT-DDD", "");
String sequence = edits.getOrDefault(prefix + "SEQUENCE", "");
String suffix = edits.getOrDefault(prefix + "SUFFIX", "");
StringBuilder sb = new StringBuilder();
if (localPlan != null) sb.append(localPlan);
if (cc != null) sb.append(cc);
if (yy != null) sb.append(yy);
if (ddd != null) sb.append(ddd);
if (sequence != null) sb.append(sequence);
if (suffix != null) sb.append(suffix);
if (sb.length() > 0) sccf = sb.toString();
}
if (sccf != null) {
if (sccf.equals("{")) sccf = "0";
byte[] enc = (sccf == null) ? new byte[15] : encodeAlpha(sccf, 15);
System.arraycopy(enc, 0, record, 0, Math.min(enc.length, 15));
}
} catch (Exception ignore) {
}

try {
// REC_TYPE override: look for REC_TYPE or keys ending with REC-TYPE/REC_TYPE
String recKey = null;
for (String k : edits.keySet()) {
if (k == null) continue;
String up = k.toUpperCase();
if (up.equals("REC_TYPE") || up.equals("REC-TYPE") || up.endsWith("REC-TYPE") || up.endsWith("REC_TYPE")) { recKey = k; break; }
}
if (recKey != null) {
String recVal = edits.get(recKey);
if (recVal == null) recVal = "";
if (recVal.equals("{")) recVal = "0";
int typeOffset;
switch (transactionType) {
case "PPU": case "PPA": typeOffset = 2; break;
default: typeOffset = 21; break;
}
byte[] enc = encodeAlpha(recVal, 2);
if (typeOffset + 2 <= record.length) System.arraycopy(enc, 0, record, typeOffset, 2);
}
} catch (Exception ignore) {
}

if (layout == null || layout.isEmpty()) return;

Map<String, FieldSpec> fieldMap = new LinkedHashMap<>();
for (FieldSpec f : layout) fieldMap.put(f.name, f);

for (Map.Entry<String, String> edit : edits.entrySet()) {
FieldSpec f = fieldMap.get(edit.getKey());
if (f == null) continue;

byte[] encoded = encodeField(edit.getValue(), f);
System.arraycopy(encoded, 0, record, f.start1Based - 1, f.lengthBytes);
}
}

private String readTypeFromRecord(byte[] record, String transactionType) {
if ("CBFBD".equals(transactionType)) return "CBFBD";
int typeOffset;
switch (transactionType) {
case "PPU": case "PPA": typeOffset = 2; break;
default: typeOffset = 21; break;
}
int typeLen = 2;
if (typeOffset + typeLen > record.length) return "";
return new String(record, typeOffset, typeLen, CP037).trim();
}

private byte[] encodeField(String value, FieldSpec f) {
switch (f.type) {
case ALPHA: return encodeAlpha(value, f.lengthBytes);
case NUMERIC_TEXT: return encodeNumericText(value, f.lengthBytes);
case PACKED_DECIMAL: return encodeComp3(value, f.lengthBytes, f.scale);
case BINARY: return encodeBinary(value, f.lengthBytes);
default:
byte[] empty = new byte[f.lengthBytes];
Arrays.fill(empty, EBCDIC_SPACE);
return empty;
}
}

private byte[] encodeAlpha(String value, int lengthBytes) {
byte[] out = new byte[lengthBytes];
Arrays.fill(out, EBCDIC_SPACE);
if (value == null || value.isEmpty()) return out;
byte[] encoded = value.getBytes(CP037);
System.arraycopy(encoded, 0, out, 0, Math.min(encoded.length, lengthBytes));
return out;
}

private byte[] encodeNumericText(String value, int lengthBytes) {
String v = (value == null) ? "" : value.trim();
boolean negative = v.startsWith("-");
if (negative) v = v.substring(1);
v = v.replaceAll("[^0-9]", "");
while (v.length() < lengthBytes) v = "0" + v;
if (v.length() > lengthBytes) v = v.substring(v.length() - lengthBytes);

byte[] out = new byte[lengthBytes];
for (int i = 0; i < lengthBytes; i++) {
int digit = v.charAt(i) - '0';
if (i == lengthBytes - 1) {
out[i] = (byte) (((negative ? 0x0D : 0x0C) << 4) | digit);
} else {
out[i] = (byte) (0xF0 | digit);
}
}
return out;
}

private byte[] encodeComp3(String value, int lengthBytes, int scale) {
BigDecimal bd;
try {
bd = (value == null || value.isBlank()) ? BigDecimal.ZERO : new BigDecimal(value.trim());
} catch (NumberFormatException e) { bd = BigDecimal.ZERO; }

boolean negative = bd.signum() < 0;
BigDecimal abs = bd.abs().setScale(scale, java.math.RoundingMode.HALF_UP);
String digitsStr = abs.movePointRight(scale).toBigInteger().toString();

int maxDigits = lengthBytes * 2 - 1;
if (digitsStr.length() > maxDigits) digitsStr = digitsStr.substring(digitsStr.length() - maxDigits);
while (digitsStr.length() < maxDigits) digitsStr = "0" + digitsStr;

byte[] out = new byte[lengthBytes];
int nibbleIdx = 0;
for (int i = 0; i < lengthBytes - 1; i++) {
int hi = digitsStr.charAt(nibbleIdx++) - '0';
int lo = digitsStr.charAt(nibbleIdx++) - '0';
out[i] = (byte) ((hi << 4) | lo);
}
int lastHi = digitsStr.charAt(nibbleIdx) - '0';
out[lengthBytes - 1] = (byte) ((lastHi << 4) | (negative ? 0x0D : 0x0C));
return out;
}

private byte[] encodeBinary(String value, int lengthBytes) {
long v;
try { v = (value == null || value.isBlank()) ? 0L : Long.parseLong(value.trim()); }
catch (NumberFormatException e) { v = 0L; }

byte[] out = new byte[lengthBytes];
for (int i = lengthBytes - 1; i >= 0; i--) { out[i] = (byte) (v & 0xFF); v >>= 8; }
return out;
}
}