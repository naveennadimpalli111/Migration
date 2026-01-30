package com.its255.schema;

public enum FieldType {
    ALPHA,          // PIC X(n) (alphanumeric)
    NUMERIC_TEXT,   // PIC 9(n) or S9(n) stored as text/zoned digits (no COMP)
    PACKED_DECIMAL, // PIC S9(n)V? COMP-3 (packed decimal)
    BINARY          // PIC S9(n) COMP/COMP-4/BINARY (signed binary)
}
