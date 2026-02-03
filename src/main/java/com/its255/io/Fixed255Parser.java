package com.its255.io;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.its255.schema.FieldSpec;
import com.its255.schema.FieldType;
import com.its255.schema.RecordType;

/**
 * Fixed-length 255-byte parser supporting ALPHA, NUMERIC_TEXT, PACKED_DECIMAL (COMP-3) and BINARY (COMP).
 */
public final class Fixed255Parser {
    public static final int RECORD_LEN = 255;

    private final Charset charset;
    private final Map<RecordType, List<FieldSpec>> schemas;

    // Where the record type is stored for dispatching; COBOL 1-based: 22-23
    private final int recTypeStart1Based;
    private final int recTypeLen;

    public Fixed255Parser(Map<RecordType, List<FieldSpec>> schemas,
                          Charset charset,
                          int recTypeStart1Based,
                          int recTypeLen) {
        this.schemas = Objects.requireNonNull(schemas);
        this.charset = Objects.requireNonNull(charset);
        this.recTypeStart1Based = recTypeStart1Based;
        this.recTypeLen = recTypeLen;
    }

    public void parse(Path input, RecordConsumer consumer) throws IOException {
        try (InputStream in = Files.newInputStream(input)) {
            byte[] rec = new byte[RECORD_LEN];
            long recNo = 0;
            while (true) {
                int n = readExact(in, rec);
                if (n == -1) break;
                if (n != RECORD_LEN) throw new EOFException("Partial record at #" + (recNo + 1));
                recNo++;

                String typeCode = slice(rec, recTypeStart1Based, recTypeLen).trim();
                RecordType rt = RecordType.from(typeCode);
                List<FieldSpec> layout = schemas.get(rt);
                if (layout == null) {
                    consumer.onUnknown(recNo, rt, typeCode, rec.clone());
                    continue;
                }
                Map<String, String> values = parseFields(rec, layout);
                consumer.onRecord(recNo, rt, values);
            }
        }
    }

    public interface RecordConsumer {
        void onRecord(long recNo, RecordType type, Map<String, String> values);
        default void onUnknown(long recNo, RecordType type, String typeCode, byte[] raw) {
            System.err.printf("Unknown record type at #%d: '%s'%n", recNo, typeCode);
        }
    }

    private int readExact(InputStream in, byte[] buf) throws IOException {
        int off = 0; int r;
        while (off < buf.length && (r = in.read(buf, off, buf.length - off)) != -1) off += r;
        return off == 0 ? -1 : off;
    }

    private Map<String, String> parseFields(byte[] rec, List<FieldSpec> layout) {
        Map<String, String> out = new LinkedHashMap<>();
        String v = null;
        for (FieldSpec f : layout) {
            switch (f.type) {
                case ALPHA:
                	out.put(f.name, slice(rec, f.start1Based, f.lengthBytes).trim());
                    break;
                case NUMERIC_TEXT:
                	v = slice(rec, f.start1Based, f.lengthBytes).trim();
                	//TODO: check on this
                	if(v.chars().count() == 1) {
                		try {
                        	v = String.valueOf(parseOverpunchInt(v));
                        } catch (NullPointerException ne) {
                        	v = "";
                        }
                	}
                	if(v.chars().count() == 4  && !v.startsWith("X")) {//need to review this
                    	try {
                        	v = String.valueOf(parseOverpunchInt(v));
                        } catch (NullPointerException ne) {
                        	v = "";
                        }
                    }
                    out.put(f.name, v);
                    break;
                case PACKED_DECIMAL:
                    out.put(f.name, decodeComp3(rec, f.start1Based, f.lengthBytes, f.scale).toPlainString());
                    break;
                case BINARY:
                    out.put(f.name, decodeBinary(rec, f.start1Based, f.lengthBytes, f.scale));
                    break;
                default:
                    out.put(f.name, "");
            }
        }
        return out;
    }

    private String slice(byte[] rec, int start1Based, int len) {
        int start = start1Based - 1;
        if (start < 0 || start + len > rec.length) return "";
        return new String(rec, start, len, charset);
    }

    /** Decode COMP-3 (packed decimal). */
    private BigDecimal decodeComp3(byte[] rec, int start1, int lenBytes, int scale) {
        int start = start1 - 1;
        int end = start + lenBytes;
        if (start < 0 || end > rec.length) return BigDecimal.ZERO;

        StringBuilder digits = new StringBuilder();
        boolean negative = false;
        for (int i = start; i < end; i++) {
            int b = rec[i] & 0xFF;
            int hi = (b >>> 4) & 0x0F;
            int lo = b & 0x0F;
            if (i < end - 1) {
                digits.append(hi).append(lo);
            } else {
                digits.append(hi);
                if (lo == 0x0D) negative = true; // D=neg, C/F=pos
            }
        }
        BigInteger bi = digits.length() == 0 ? BigInteger.ZERO : new BigInteger(digits.toString());
        BigDecimal bd = new BigDecimal(bi).movePointLeft(Math.max(scale, 0));
        return negative ? bd.negate() : bd;
    }

    /** Decode signed binary (COMP/COMP-4). */
    private String decodeBinary(byte[] rec, int start1, int len, int scale) {
        int start = start1 - 1;
        long val = 0;
        for (int i = 0; i < len; i++) {
            val = (val << 8) | (rec[start + i] & 0xFFL);
        }
        long signBit = 1L << (len * 8 - 1);
        long signed = (val & signBit) != 0 ? val - (1L << (len * 8)) : val;
        if (scale > 0) return new BigDecimal(signed).movePointLeft(scale).toPlainString();
        return Long.toString(signed);
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
