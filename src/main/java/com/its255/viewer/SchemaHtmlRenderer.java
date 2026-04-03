
package com.its255.viewer;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.its255.schema.FieldSpec;
import com.its255.schema.RecordType;
import com.its255.schema.SchemaRegistry;

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
    private final String transactionType;

    public SchemaHtmlRenderer(Object store, Charset cs,
            String transactionType) {
        this.store = store;
        this.cs = cs;
        this.transactionType = transactionType;
    }

    public String render(int recordNumber1Based) {
        String type = readType(recordNumber1Based).trim();
        RecordType rt = RecordType.from(type);
        List<FieldSpec> layout = SchemaRegistry.getSchema(transactionType, rt.code);
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
                String val = null;
                switch (f.type) {
                    case ALPHA:
                    	val = sliceTrim(rec, start, len);
                    	break;
                    case NUMERIC_TEXT:
                    	int lastByte = rec[start + f.lengthBytes - 1] & 0xFF;
                        int zone = (lastByte >>> 4) & 0x0F;
                        if (zone != 0xF) {
                            // ZONED DECIMAL
                        	val =  decodeZonedDecimal(rec, start, f.lengthBytes);
                        } else {
                        	val = sliceTrim(rec, start, len);
                        	
                        	if(val.chars().count() == 1){
                        		try {
                                	val = String.valueOf(parseOverpunchIntSafe(val));
                                } catch (NullPointerException ne) {
                                	val = "";
                                }
                        	}
                        	if (val.contains("}")) {
                        		try {
                        			val = String.valueOf(parseOverpunchIntSafe(val));
                        		} catch (NullPointerException ne) {
                                	val = "";
                                }
                        	}
                            if (val.contains("{")) {
                            	try {
                        			val = String.valueOf(parseOverpunchIntSafe(val));
                        		} catch (NullPointerException ne) {
                                	val = "";
                                }
                            }
                        	if(val.chars().count() == 4  && !val.startsWith("X")) {//need to review this
                            	try {
                                	val = String.valueOf(parseOverpunchIntSafe(val));
                                } catch (NullPointerException ne) {
                                	val = "";
                                }
                            }
                        }
                        
                        break;
                    case PACKED_DECIMAL:
                        val = invokeFixed("decodeComp3ToString", rec, start, len, f.scale);
                        if(val.contains("}")) {
                        	try {
                            	val = String.valueOf(parseOverpunchIntSafe(val));
                            } catch (NullPointerException ne) {
                            	val = "";
                            }
                    	} 
                        break;
                    case BINARY:
                        val = invokeFixed("decodeBinary", rec, start, len, f.scale);
//                        System.out.println("BINARY");
//                        System.out.println(Arrays.toString(rec));
//                        System.out.println(start);
//                        System.out.println(len);
//                        System.out.println(f.scale);
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
            // It's private – make it accessible
            m.setAccessible(true);

            
            Object val = m.invoke(null, rec, start, len, scale);//Catching Exception for Binary
            return (val == null) ? "" : String.valueOf(val);
        } catch (Exception ex) {
        	//ex.printStackTrace();
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
    
    static int parseOverpunchInt(String s) {
    	if(s == null || s.isEmpty()) {
    		return (Integer) null;
    	}
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
//    		throw new IllegalArgumentException("Invalid overpunch char: " + last);
    		System.err.println("Invalid overpunch char: " + last);
    	}
    	int value = Integer.parseInt(body + d);
    	return neg ? -value : value;
    }
    
    /** Safer overpunch decoder that returns null for invalid inputs. */
    private static Integer parseOverpunchIntSafe(String s) {
        if (s == null || s.isEmpty()) return null;
        char last = s.charAt(s.length() - 1);
        String body = s.substring(0, s.length() - 1);
        if (last >= '0' && last <= '9') {
            try { return Integer.parseInt(body + last); }
            catch (NumberFormatException e) { return null; }
        }
        Integer d = POS.get(last);
        boolean neg = false;
        if (d == null) {
            d = NEG.get(last);
            if (d != null) neg = true;
        }
        if (d == null) return null;
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
                if (hi == 0xD) negative = true;
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
    
    static final Map<Character, Integer> POS = Map.ofEntries(
    		Map.entry('{', 0),
    		Map.entry('A', 1),
    		Map.entry('B', 2),
    		Map.entry('C', 3),
    		Map.entry('D', 4),
    		Map.entry('E', 5),
    		Map.entry('F', 6),
    		Map.entry('G', 7),
    		Map.entry('H', 8),
    		Map.entry('I', 9),
    		Map.entry('X', 0)//Checking on this char
    		);
    
    static final Map<Character, Integer> NEG = Map.ofEntries(
    		Map.entry('}', 0),
    		Map.entry('J', 1),
    		Map.entry('K', 2),
    		Map.entry('L', 3),
    		Map.entry('M', 4),
    		Map.entry('N', 5),
    		Map.entry('O', 6),
    		Map.entry('P', 7),
    		Map.entry('Q', 8),
    		Map.entry('R', 9),
    		Map.entry('X', 0)//Checking on this char
    		);

}
