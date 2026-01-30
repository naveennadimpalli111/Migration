package com.its255.web.service;

import com.its255.io.Fixed255Parser;
import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.Schemas;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

@Service
public class ViewerService {

    public static final int DEFAULT_MAX_ROWS = 200;

    public enum SortBy { NONE, JULIAN, SERIAL }
    public enum SortDir { ASC, DESC }

    public record TableResult(
            RecordType recordType,
            String charsetName,
            List<String> headers,
            List<List<String>> rows,
            int displayed,
            int totalMatched,
            Integer recordNumber,       // if single-record mode, else null
            SortBy sortBy,
            SortDir sortDir,
            String filter
    ) {}

    /** Expose supported types (code -> label) for the UI. */
    public Map<String, String> supportedTypes() {
        Map<String, String> out = new LinkedHashMap<>();
        out.put("05", "FM105");
        out.put("10", "FM110");
        out.put("15", "FM115");
        out.put("20", "FM120");
        out.put("30", "FM130");
        out.put("31", "FM131");
        out.put("32", "FM132");
        out.put("33", "FM133");
        out.put("40", "FM140");
        out.put("41", "FM141");
        out.put("42", "FM142");
        out.put("43", "FM143");
        out.put("44", "FM144");
        out.put("45", "FM145");
        out.put("46", "FM146");
        out.put("47", "FM147");
        out.put("50", "FM150");
        out.put("60", "FM160");
        out.put("65", "FM165");
        out.put("66", "FM166");
        out.put("71", "FM171");
        out.put("72", "FM172");
        out.put("73", "FM173");
        out.put("74", "FM174");
        out.put("80", "FM180");
        out.put("90", "FM190");
        out.put("9D", "FM9D");
        return out;
    }

    /** Human label for a RecordType (used in preview title). */
    public String labelOf(RecordType rt) {
        if (rt == null) return "Unknown";
        return switch (rt) {
        	case RT_05 -> "FM105";
            case RT_10 -> "FM110";
            case RT_15 -> "FM115";
            case RT_20 -> "FM120";
            case RT_30 -> "FM130";
            case RT_31 -> "FM131";
            case RT_32 -> "FM132";
            case RT_33 -> "FM133";
            case RT_40 -> "FM140";
            case RT_41 -> "FM141";
            case RT_42 -> "FM142";
            case RT_43 -> "FM143";
            case RT_44 -> "FM144";
            case RT_45 -> "FM145";
            case RT_46 -> "FM146";
            case RT_47 -> "FM147";
            case RT_50 -> "FM150";
            case RT_60 -> "FM160";
            case RT_65 -> "FM165";
            case RT_66 -> "FM166";
            case RT_71 -> "FM171";
            case RT_72 -> "FM172";
            case RT_73 -> "FM173";
            case RT_74 -> "FM174";
            case RT_80 -> "FM180";
            case RT_90 -> "FM190";
            case RT_9D -> "FM9D";
            default -> "Unknown";
        };
    }

    /** Main entry used by controller. */
    public TableResult preview(MultipartFile file,
                               String charsetName,
                               RecordType requestedType,
                               Integer maxRows,
                               Integer recordNumber,
                               String filter,
                               SortBy sortBy,
                               SortDir sortDir) throws IOException {

        Charset cs = (charsetName == null || charsetName.isBlank())
                ? Charset.forName("Cp037")
                : Charset.forName(charsetName);

        int cap = (maxRows != null && maxRows > 0) ? Math.min(maxRows, 5000) : DEFAULT_MAX_ROWS;
        SortBy effSortBy = (sortBy == null ? SortBy.NONE : sortBy);
        SortDir effSortDir = (sortDir == null ? SortDir.ASC : sortDir);
        String effFilter = (filter == null ? "" : filter.trim());

        // Temp file backed upload
        Path temp = Files.createTempFile("its255_", ".dat");
        file.transferTo(temp.toFile());

        try {
            if (recordNumber != null && recordNumber > 0) {
                // --- Single record mode ---
                SingleRecord sr = readSingleRecord(temp, cs, recordNumber);

                Map<RecordType, List<FieldSpec>> schemas = Schemas.all();
                List<FieldSpec> layout = schemas.get(sr.type);
                if (layout == null) throw new IllegalArgumentException("Unsupported record type: " + sr.type);

//                List<String> headers = layout.stream().map(fs -> fs.name).toList();
                List<String> headers = new ArrayList<String>();
                headers.add(0, "REC_NO");
            	headers.add(1, "REC_TYPE");
            	headers.addAll(layout.stream().map(fs -> fs.name).toList());
            	
                List<String> row = new ArrayList<>(headers.size());
                for (String h : headers) {
                	row.add(sr.values.getOrDefault(h, ""));
                }

                return new TableResult(sr.type, cs.name(), headers, List.of(row), 1, 1,
                        recordNumber, effSortBy, effSortDir, effFilter);

            } else {
                // --- Multi-record mode: by requestedType ---
                Map<RecordType, List<FieldSpec>> schemas = Schemas.all();
                List<FieldSpec> layout = schemas.get(requestedType);
                if (layout == null) throw new IllegalArgumentException("Unsupported record type: " + requestedType);
//                List<String> headers = layout.stream().map(fs -> fs.name).toList();
                List<String> headers = new ArrayList<String>();
                headers.add(0, "REC_NO");
            	headers.add(1, "REC_TYPE");
            	headers.addAll(layout.stream().map(fs -> fs.name).toList());

                final List<List<String>> rows = new ArrayList<>();
                final int[] matched = new int[]{0};

                Fixed255Parser parser = new Fixed255Parser(schemas, cs, /*recTypeStart1Based*/22, /*len*/2);
                parser.parse(temp, (recNo, rt, values) -> {
                	values.put("REC_NO", String.valueOf(recNo));
                    values.put("REC_TYPE", String.valueOf(rt).replace("RT_", ""));
                    if (rt == requestedType) {
                        matched[0]++;
                        // Collect a bit more than cap before filtering/sorting to improve utility
                        if (rows.size() < Math.max(cap, 2000)) {
                            List<String> row = new ArrayList<>(headers.size());
                            for (String h : headers) row.add(values.getOrDefault(h, ""));
                            rows.add(row);
                        }
                    }
                });

                // Filtering (case-insensitive substring across any cell)
                if (!effFilter.isEmpty()) {
                    Predicate<List<String>> pred = r -> r.stream()
                            .anyMatch(v -> v != null && v.toLowerCase().contains(effFilter.toLowerCase()));
                    rows.removeIf(pred.negate());
                }

                // Sorting (use comparingInt for clean generic inference)
                Comparator<List<String>> cmp = switch (effSortBy) {
                    case JULIAN -> Comparator.comparingInt(r -> julianKey(headers, r));
                    case SERIAL -> Comparator.comparingInt(r -> serialKey(headers, r));
                    case NONE   -> null;
                };
                if (cmp != null) {
                    if (effSortDir == SortDir.DESC) cmp = cmp.reversed();
                    rows.sort(cmp);
                }

                // Cap to requested max WITHOUT reassigning 'rows' (keeps lambda capture valid)
                if (rows.size() > cap) {
                    rows.subList(cap, rows.size()).clear();
                }

                return new TableResult(requestedType, cs.name(), headers, rows, rows.size(), matched[0],
                        null, effSortBy, effSortDir, effFilter);
            }
        } finally {
            try { Files.deleteIfExists(temp); } catch (IOException ignore) {}
        }
    }

    /** Represents a decoded single record (used in single-record mode). */
    private static final class SingleRecord {
        final RecordType type;
        final Map<String, String> values;
        SingleRecord(RecordType t, Map<String, String> v) { this.type = t; this.values = v; }
    }

    /** Read one record by number (1-based), detect its type, and decode using schema. */
    private SingleRecord readSingleRecord(Path path, Charset cs, int recordNumber) throws IOException {
        int recLen = Fixed255Parser.RECORD_LEN;
        long offset = (long) (recordNumber - 1) * recLen;

        byte[] rec = Files.readAllBytes(path); // For very large files, replace with FileChannel
        if (offset < 0 || offset + recLen > rec.length) {
            throw new IllegalArgumentException("recordNumber " + recordNumber + " is out of range");
        }
        byte[] buf = Arrays.copyOfRange(rec, (int) offset, (int) offset + recLen);

        // Record type at 22-23 (1-based)
        String typeCode = new String(buf, 22 - 1, 2, cs).trim();
        RecordType rt = RecordType.from(typeCode);

        Map<RecordType, List<FieldSpec>> schemas = Schemas.all();
        List<FieldSpec> layout = schemas.get(rt);
        if (layout == null) {
            throw new IllegalArgumentException("Unknown record type code '" + typeCode + "' at record #" + recordNumber);
        }

        Map<String, String> values = decodeFields(buf, layout, cs);
        values.put("REC_NO", String.valueOf(recordNumber));
        values.put("REC_TYPE", typeCode);
        return new SingleRecord(rt, values);
    }

    // --------------------- Helpers: decode & sort keys ---------------------

    private Map<String, String> decodeFields(byte[] rec, List<FieldSpec> layout, Charset cs) {
        Map<String, String> out = new LinkedHashMap<>();
        for (FieldSpec f : layout) {
            int start = f.start1Based - 1;
            int len   = f.lengthBytes;
            switch (f.type) {
                case ALPHA, NUMERIC_TEXT -> out.put(f.name, new String(rec, start, len, cs).trim());
                case PACKED_DECIMAL -> out.put(f.name, decodeComp3(rec, start, len, f.scale).toPlainString());
                case BINARY -> out.put(f.name, decodeBinary(rec, start, len, f.scale));
            }
        }
        return out;
    }

    private BigDecimal decodeComp3(byte[] rec, int start, int lenBytes, int scale) {
        int end = start + lenBytes;
        StringBuilder digits = new StringBuilder();
        boolean neg = false;
        for (int i = start; i < end; i++) {
            int b = rec[i] & 0xFF;
            int hi = (b >>> 4) & 0x0F;
            int lo = b & 0x0F;
            if (i < end - 1) {
                digits.append(hi).append(lo);
            } else {
                digits.append(hi);
                if (lo == 0x0D || lo == 0x0B) neg = true; // 0x0C = positive, 0x0D/0x0B = negative
            }
        }
        BigInteger bi = digits.length() == 0 ? BigInteger.ZERO : new BigInteger(digits.toString());
        BigDecimal bd = new BigDecimal(bi).movePointLeft(Math.max(scale, 0));
        return neg ? bd.negate() : bd;
    }

    private String decodeBinary(byte[] rec, int start, int len, int scale) {
        long val = 0;
        for (int i = 0; i < len; i++) val = (val << 8) | (rec[start + i] & 0xFFL);
        long signBit = 1L << (len * 8 - 1);
        long signed = (val & signBit) != 0 ? val - (1L << (len * 8)) : val;
        if (scale > 0) return new BigDecimal(signed).movePointLeft(scale).toPlainString();
        return Long.toString(signed);
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    /** Build sortable key CC YY DDD -> int */
    private int julianKey(List<String> headers, List<String> row) {
        int iCC = indexOfSuffix(headers, "-SER-NUM-JULDT-CC");
        int iYY = indexOfSuffix(headers, "-SER-NUM-JULDT-YY");
        int iDD = indexOfSuffix(headers, "-SER-NUM-JULDT-DDD");
        int cc  = iCC >= 0 ? parseIntSafe(row.get(iCC)) : 0;
        int yy  = iYY >= 0 ? parseIntSafe(row.get(iYY)) : 0;
        int ddd = iDD >= 0 ? parseIntSafe(row.get(iDD)) : 0;
        return cc * 100_000 + yy * 1_000 + ddd;
    }

    /** Serial number component (sequence) -> int */
    private int serialKey(List<String> headers, List<String> row) {
        int iSeq = indexOfSuffix(headers, "-SER-NUM-SEQUENCE");
        return iSeq >= 0 ? parseIntSafe(row.get(iSeq)) : 0;
    }

    private int indexOfSuffix(List<String> headers, String suffix) {
        for (int i = 0; i < headers.size(); i++) {
            String h = headers.get(i);
            if (h != null && h.endsWith(suffix)) return i;
        }
        return -1;
    }
}