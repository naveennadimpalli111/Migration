package com.its255.util;

import java.util.Map;
import java.util.Optional;

/**
 * Overpunch decoding for zoned-decimal (DISPLAY) numerics.
 * Input: EBCDIC-decoded text (last character may be { A..I } J..R).
 * Output: normalized decimal string (e.g., "00{" -> "0", "12A" -> "121", "12J" -> "-121"),
 *         or original when input is not overpunched.
 *
 * References:
 * - IBM/COBOL zoned decimal (DISPLAY) w/ signed overpunch: last char encodes sign+last digit. 
 */
public final class Overpunch {
    private Overpunch() {}

    private static final Map<Character, Integer> POS = Map.of(
            '{', 0, 'A', 1, 'B', 2, 'C', 3, 'D', 4, 'E', 5, 'F', 6, 'G', 7, 'H', 8, 'I', 9
    );
    private static final Map<Character, Integer> NEG = Map.of(
            '}', 0, 'J', 1, 'K', 2, 'L', 3, 'M', 4, 'N', 5, 'O', 6, 'P', 7, 'Q', 8, 'R', 9
    );

    public static String decodeOrOriginal(String s) {
        if (s == null || s.isEmpty()) return "";
        Optional<String> dec = decode(s);
        return dec.orElse(s);
    }

    public static Optional<String> decode(String s) {
        if (s == null || s.isEmpty()) return Optional.empty();
        char last = s.charAt(s.length() - 1);
        String body = s.substring(0, s.length() - 1);

        Integer d = POS.get(last);
        boolean neg = false;
        if (d == null) { d = NEG.get(last); if (d != null) neg = true; }

        // If last is a digit, it's already plain numeric (unsigned 9(n)).
        if (d == null && last >= '0' && last <= '9') return Optional.of(s);
        if (d == null) return Optional.empty();

        String out = body + d;
        // Normalize leading zeros (keep a single zero)
        out = out.replaceFirst("^0+(?!$)", "");
        if (out.isEmpty()) out = "0";
        // Decide whether to preserve "-0" — by default we normalize to "0"
        if (neg && !"0".equals(out)) out = "-" + out;
        return Optional.of(out);
    }
}