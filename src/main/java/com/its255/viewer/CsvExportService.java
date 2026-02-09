
package com.its255.viewer;

import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.Schemas;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CsvExportService {
    private final Object store; // reflection: readRecordBytes, readType, readSccf, offsetOf
    private final Charset cs;
    private final int bufferSize;

    public CsvExportService(Object store, Charset cs, int bufferSize) { this.store = store; this.cs = cs; this.bufferSize = bufferSize; }

    private byte[] readRecordBytes(int rn) {
        try { return (byte[]) store.getClass().getMethod("readRecordBytes", int.class).invoke(store, rn); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
    private long offsetOf(int rn) {
        try { return (long) store.getClass().getMethod("offsetOf", int.class).invoke(store, rn); }
        catch (Exception e) { return -1L; }
    }
    private String readType(int rn) {
        try { return (String) store.getClass().getMethod("readType", int.class).invoke(store, rn); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
    private String readSccf(int rn) {
        try { return (String) store.getClass().getMethod("readSccf", int.class).invoke(store, rn); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public void exportPerTypeZip(List<Integer> recordNumbers, OutputStream out) throws Exception {
        try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(out, bufferSize))) {
            Map<String, List<Integer>> byType = new LinkedHashMap<>();
            for (int rn : recordNumbers) {
                String type = readType(rn);
                byType.computeIfAbsent(type, k -> new ArrayList<>()).add(rn);
            }
            for (Map.Entry<String,List<Integer>> e : byType.entrySet()) {
                String type = e.getKey();
                List<Integer> list = e.getValue();
                zip.putNextEntry(new ZipEntry(type + ".csv"));
                OutputStreamWriter w = new OutputStreamWriter(zip, java.nio.charset.StandardCharsets.UTF_8);
                RecordType rt = RecordType.from(type);
                java.util.List<FieldSpec> layout = Schemas.all().get(rt);
                if (layout == null) {
                    w.write("REC_NO,SCCF,REC_TYPE,BYTE_OFFSET");
                    for (int rn : list) {
                        writeCsvRow(w, new String[]{ String.valueOf(rn), readSccf(rn), type, String.valueOf(offsetOf(rn)) });
                    }
                    w.flush(); zip.closeEntry(); continue;
                }
                String[] header = layout.stream().map(f -> f.name).toArray(String[]::new);
                writeCsvRow(w, header);
                for (int rn : list) {
                    byte[] rec = readRecordBytes(rn);
                    String[] row = new String[layout.size()];
                    for (int i=0;i<layout.size();i++) {
                        FieldSpec f = layout.get(i);
                        int start = f.start1Based - 1;
                        int len = f.lengthBytes;
                        String val;
                        switch (f.type) {
                            case ALPHA: case NUMERIC_TEXT: val = sliceTrim(rec, start, len); break;
                            case PACKED_DECIMAL: val = invokeParser("decodeComp3ToString", rec, start, len, f.scale); break;
                            case BINARY: val = invokeParser("decodeBinary", rec, start, len, f.scale); break;
                            default: val = "";
                        }
                        row[i] = val;
                    }
                    writeCsvRow(w, row);
                }
                w.flush(); zip.closeEntry();
            }
            zip.finish();
        }
    }

    private String sliceTrim(byte[] a, int off, int len) {
        int end = Math.min(a.length, off + len);
        int i = end - 1;
        byte space = (byte)0x40; // EBCDIC space
        while (i >= off && a[i] == space) i--; int newLen = (i < off) ? 0 : (i - off + 1);
        return new String(a, off, Math.max(0, newLen), cs);
    }

    private String invokeParser(String method, byte[] rec, int start, int len, int scale) {
        try {
            Class<?> cls = Class.forName("com.its255.io.Fixed255Parser");
            java.lang.reflect.Method m = cls.getDeclaredMethod(method, byte[].class, int.class, int.class, int.class);
            Object val = m.invoke(null, rec, start, len, scale);
            return String.valueOf(val);
        } catch (Exception ex) { return ""; }
    }

    private void writeCsvRow(Writer w, String[] cols) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<cols.length;i++) {
            if (i>0) sb.append(',');
            String s = cols[i]==null?"":cols[i];
            boolean needs = s.contains(",") || s.contains("") || s.contains("") || s.contains("\"");
            if (needs) { sb.append('"').append(s.replace("\"", "\"\"")).append('"'); }
            else sb.append(s);
        }
        sb.append("");
        w.write(sb.toString());
    }
}
