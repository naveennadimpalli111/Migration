package com.its255.util;

import com.its255.schema.FieldSpec;

public final class FieldValueNormalizer {
    private FieldValueNormalizer() {
    }

    public static boolean isBraceZeroField(String fieldName) {
        return "FM1F0-SERV-HH".equalsIgnoreCase(fieldName)
                || "FM1F0-SERV-MM".equalsIgnoreCase(fieldName);
    }

    public static boolean isBraceZeroField(FieldSpec fieldSpec) {
        return fieldSpec != null && isBraceZeroField(fieldSpec.name);
    }

    public static String normalize(String fieldName, String value) {
        if (value == null) {
            return "";
        }
        if (isBraceZeroField(fieldName)) {
            return value.replace("{", "0");
        }
        return value;
    }

    public static String normalize(FieldSpec fieldSpec, String value) {
        return normalize(fieldSpec != null ? fieldSpec.name : null, value);
    }
}
