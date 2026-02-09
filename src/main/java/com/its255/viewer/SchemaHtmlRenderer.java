
package com.its255.viewer;

import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.Schemas;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Renders ONE 255-byte record as an HTML table using the active copybook schema.
 * - Supports ALPHA / NUMERIC_TEXT via Cp037 slicing + trim of EBCDIC spaces (0x40)
 * - Supports PACKED_DECIMAL and BINARY via Fixed255Parser (reflection)
 *
 * This class depends on the host app providing:
 *   com.its255.schema.{Schemas, RecordType, FieldSpec}
 *   com.its255.io.Fixed255Parser with methods:
 *     decodeComp3ToString(byte[], int start, int len, int scale)
 *     decodeBinary(byte[], int start, int len, int scale)
 */
public class SchemaHtmlRenderer {
    private static final byte EBCDIC_SPACE = (byte)0x40;

    private final Object store; // must expose: readRecordBytes(int), readType(int)
    private final Charset cs;

    public SchemaHtmlRenderer(Object store, Charset cs) {
        this.store = store;
        this.cs = cs;
    }

    public String render(int recordNumber1Based) {
        String type = readType(recordNumber1Based).trim();
        RecordType rt = RecordType.from(type);
        List<FieldSpec> layout = Schemas.all().get(rt);
        byte[] rec = readRecordBytes(recordNumber1Based);

        StringBuilder sb = new StringBuilder(8_192);
        sb.append("<div class='table-responsive'>\n<table class='table table-sm table-striped table-bordered'>\n");
        sb.append("<thead><tr><th style='white-space:nowrap'>Field</th><th>Value</th></tr></thead><tbody>\n");

        if (layout == null || layout.isEmpty()) {
            // Fallback: minimal metadata view
            sb.append(row("REC_TYPE", type));
            sb.append(row("BYTE_LEN", String.valueOf(rec.length)));
        } else {
            for (FieldSpec f : layout) {
                int start = f.start1Based - 1;
                int len = f.lengthBytes;
                String val;
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
                sb.append(row(f.name, escape(val)));
            }
        }
        sb.append("</tbody></table></div>\n");
        return sb.toString();
    }

    private String row(String name, String value) {
        return new StringBuilder()
                .append("<tr><td style='white-space:nowrap'>")
                .append(escape(name))
                .append("</td><td><pre style='margin:0'>")
                .append(value)
                .append("</pre></td></tr>\n")
                .toString();
    }

    private String sliceTrim(byte[] a, int off, int len) {
        int end = Math.min(a.length, off + len);
        int i = end - 1;
        while (i >= off && a[i] == EBCDIC_SPACE) i--;
        int newLen = (i < off) ? 0 : (i - off + 1);
        return new String(a, off, Math.max(0, newLen), cs);
    }

    private String invokeFixed(String method, byte[] rec, int start, int len, int scale) {
        try {
            Class<?> cls = Class.forName("com.its255.io.Fixed255Parser");
            Method m = cls.getDeclaredMethod(method, byte[].class, int.class, int.class, int.class);
            Object val = m.invoke(null, rec, start, len, scale);
            return String.valueOf(val);
        } catch (Exception ex) {
            return "";
        }
    }

    private byte[] readRecordBytes(int rn) {
        try { return (byte[]) store.getClass().getMethod("readRecordBytes", int.class).invoke(store, rn); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    private String readType(int rn) {
        try { return String.valueOf(store.getClass().getMethod("readType", int.class).invoke(store, rn)); }
        catch (Exception e) { return ""; }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
}
