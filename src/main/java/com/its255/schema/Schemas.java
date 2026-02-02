package com.its255.schema;

import static com.its255.schema.FieldType.ALPHA;
import static com.its255.schema.FieldType.BINARY;
import static com.its255.schema.FieldType.NUMERIC_TEXT;
import static com.its255.schema.FieldType.PACKED_DECIMAL;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Schemas {

	// --------------------------------------------------------------------
    // FM110 — Institutional Provider (Record Type 10) — length 255 bytes
    // (Original schema provided; unchanged)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM110 = List.of(
        // ---- KEY ----
        new FieldSpec("FM110-SER-NUM-LOCAL-PLAN",        1,   3,  ALPHA),
        new FieldSpec("FM110-SER-NUM-JULDT-CC",          4,   2,  ALPHA),
        new FieldSpec("FM110-SER-NUM-JULDT-YY",          6,   2,  ALPHA),
        new FieldSpec("FM110-SER-NUM-JULDT-DDD",         8,   3,  ALPHA),
        new FieldSpec("FM110-SER-NUM-SEQUENCE",         11,   5,  ALPHA),
        new FieldSpec("FM110-SER-NUM-SUFFIX",           16,   2,  ALPHA),
        new FieldSpec("FM110-TRANS-ID",                  18,   2,  ALPHA),
        new FieldSpec("FM110-TRANS-QUAL",                20,   2,  ALPHA),
        new FieldSpec("FM110-REC-TYPE",                  22,   2,  ALPHA),
        new FieldSpec("FM110-SEQ-NUM",                   24,   2,  BINARY), // S9(4) COMP ~ 2 bytes

        // ---- DATA ----
        new FieldSpec("FM110-TYPE-BILL",                 26,   3,  ALPHA),
        new FieldSpec("FM110-FED-TAX-ID-NUM",            29,   9,  ALPHA),
        new FieldSpec("FM110-FED-TAX-SUB-ID",            38,   4,  ALPHA),
        new FieldSpec("FM110-MED-PROV-NUM",              42,  13,  ALPHA),
        new FieldSpec("FM110-BCBS-PROV-NUM",             55,  13,  ALPHA),
        new FieldSpec("FM110-PROV-NAME",                 68,  31, ALPHA),
        new FieldSpec("FM110-PROV-ADDR-LN-1",            99,  25,  ALPHA),
        new FieldSpec("FM110-PROV-ADDR-LN-2",           124,  25,  ALPHA),
        new FieldSpec("FM110-PROV-CITY",                149,  15,  ALPHA),
        new FieldSpec("FM110-PROV-ST",                  164,   2,  ALPHA),
        new FieldSpec("FM110-PROV-ZIP-CD-5",            166,   5,  ALPHA),
        new FieldSpec("FM110-PROV-ZIP-CD-4",            171,   4,  ALPHA),
        new FieldSpec("FM110-PROV-CNTRY-CD",            175,   4,  ALPHA),
        new FieldSpec("FM110-REF-PROV-NUM",             179,  14,  ALPHA),
        new FieldSpec("FM110-PERF-PROV-TXNMY-CD-CLM",   193,  15,  ALPHA),
        new FieldSpec("FM110-PROV-NPI",                 208,  10,  ALPHA),
        new FieldSpec("FM110-CUST-PRIMY-NTWK",          218,   4,  ALPHA),
        new FieldSpec("FM110-CUST-SCNDY-NTWK",          222,   4,  ALPHA),
        new FieldSpec("FM110-SERV-ELIG-CD-CLM-1",       226,   1,  ALPHA),
        new FieldSpec("FM110-SERV-ELIG-CD-CLM-2",       227,   1,  ALPHA),
        new FieldSpec("FM110-SERV-ELIG-CD-CLM-3",       228,   1,  ALPHA),
        new FieldSpec("FM110-PERF-PROV-IHS-CLM-IND",    229,   1,  ALPHA),
        new FieldSpec("FM110-FILLER-1",                 230,  26,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM115 — Institutional Appended Provider (Record Type 15)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM115 = List.of(
        // KEY (1–25)
        new FieldSpec("FM115-SER-NUM-LOCAL-PLAN",      1,   3,  ALPHA),
        new FieldSpec("FM115-SER-NUM-JULDT-CC",        4,   2,  ALPHA),
        new FieldSpec("FM115-SER-NUM-JULDT-YY",        6,   2,  ALPHA),
        new FieldSpec("FM115-SER-NUM-JULDT-DDD",       8,   3,  ALPHA),
        new FieldSpec("FM115-SER-NUM-SEQUENCE",       11,   5,  ALPHA),
        new FieldSpec("FM115-SER-NUM-SUFFIX",         16,   2,  ALPHA),
        new FieldSpec("FM115-TRANS-ID",               18,   2,  ALPHA),
        new FieldSpec("FM115-TRANS-QUAL",             20,   2,  ALPHA),
        new FieldSpec("FM115-REC-TYPE",               22,   2,  ALPHA),
        new FieldSpec("FM115-SEQ-NUM",                24,   2,  BINARY),

        // DATA (26–255)
        new FieldSpec("FM115-CLASS-PROV-CLM",         26,   1,  ALPHA),
        new FieldSpec("FM115-PMT-RSTRCT-IND",         27,   1,  ALPHA),
        new FieldSpec("FM115-FACIL-TYPE",             28,   4,  ALPHA),
        new FieldSpec("FM115-MKT-ID-CLM",             32,   4,  ALPHA),
        new FieldSpec("FM115-TIER-DESIG-IND-CLM",     36,   1,  ALPHA),
        new FieldSpec("FM115-FILLER-1",               37, 219,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM120 — Institutional Patient (Record Type 20)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM120 = List.of(
        // KEY
        new FieldSpec("FM120-SER-NUM-LOCAL-PLAN",         1,  3,  ALPHA),
        new FieldSpec("FM120-SER-NUM-JULDT-CC",           4,  2,  ALPHA),
        new FieldSpec("FM120-SER-NUM-JULDT-YY",           6,  2,  ALPHA),
        new FieldSpec("FM120-SER-NUM-JULDT-DDD",          8,  3,  ALPHA),
        new FieldSpec("FM120-SER-NUM-SEQUENCE",          11,  5,  ALPHA),
        new FieldSpec("FM120-SER-NUM-SUFFIX",            16,  2,  ALPHA),
        new FieldSpec("FM120-TRANS-ID",                  18,  2,  ALPHA),
        new FieldSpec("FM120-TRANS-QUAL",                20,  2,  ALPHA),
        new FieldSpec("FM120-REC-TYPE",                  22,  2,  ALPHA), // '20'
        new FieldSpec("FM120-SEQ-NUM",                   24,  2,  BINARY),// S9(4) COMP

        // DATA
        new FieldSpec("FM120-PAT-CNTL-NUM",              26, 20,  ALPHA),
        new FieldSpec("FM120-PAT-LAST-NAME",             46, 20,  ALPHA),
        new FieldSpec("FM120-PAT-FIRST-NAME",            66, 10,  ALPHA),
        new FieldSpec("FM120-PAT-MID-INIT",              76,  1,  ALPHA),
        new FieldSpec("FM120-PAT-SEX",                   77,  1,  ALPHA),
        new FieldSpec("FM120-PAT-BRT-DT-CC",             78,  2,  ALPHA),
        new FieldSpec("FM120-PAT-BRT-DT-YY",             80,  2,  ALPHA),
        new FieldSpec("FM120-PAT-BRT-DT-MM",             82,  2,  ALPHA),
        new FieldSpec("FM120-PAT-BRT-DT-DD",             84,  2,  ALPHA),
        new FieldSpec("FM120-PAT-MAR-STAT-CD",           86,  1,  ALPHA),
        new FieldSpec("FM120-TYPE-ADM",                  87,  1,  ALPHA),
        new FieldSpec("FM120-SRC-ADM",                   88,  1,  ALPHA),
        new FieldSpec("FM120-PAT-ADDR-LN-1",             89, 25,  ALPHA),
        new FieldSpec("FM120-PAT-ADDR-LN-2",            114, 25,  ALPHA),
        new FieldSpec("FM120-PAT-CITY",                 139, 15,  ALPHA),
        new FieldSpec("FM120-PAT-ST",                   154,  2,  ALPHA),
        new FieldSpec("FM120-PAT-ZIP-CD-5",             156,  5,  ALPHA),
        new FieldSpec("FM120-PAT-ZIP-CD-4",             161,  4,  ALPHA),
        new FieldSpec("FM120-ADM-DT-CC",                165,  2,  ALPHA),
        new FieldSpec("FM120-ADM-DT-YY",                167,  2,  ALPHA),
        new FieldSpec("FM120-ADM-DT-MM",                169,  2,  ALPHA),
        new FieldSpec("FM120-ADM-DT-DD",                171,  2,  ALPHA),
        new FieldSpec("FM120-ADM-HH",                   173,  2,  NUMERIC_TEXT), // S9(02)
        new FieldSpec("FM120-DISCHRG-HH",               175,  2,  NUMERIC_TEXT), // S9(02)
        new FieldSpec("FM120-STMT-COV-FROM-DT-CC",      177,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-FROM-DT-YY",      179,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-FROM-DT-MM",      181,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-FROM-DT-DD",      183,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-TO-DT-CC",        185,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-TO-DT-YY",        187,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-TO-DT-MM",        189,  2,  ALPHA),
        new FieldSpec("FM120-STMT-COV-TO-DT-DD",        191,  2,  ALPHA),
        new FieldSpec("FM120-LNG-STAY",                 193,  3,  NUMERIC_TEXT), // S9(03)
        new FieldSpec("FM120-PAT-STAT-CD",              196,  2,  ALPHA),
        new FieldSpec("FM120-PAT-PAID-AMT",             198,  6,  PACKED_DECIMAL, 2), // S9(8)V99 COMP-3
        new FieldSpec("FM120-MED-REC-NUM",              204, 18,  ALPHA),
        new FieldSpec("FM120-OTH-CARR-IND",             222,  1,  ALPHA),
        new FieldSpec("FM120-FILLER-1",                 223, 33,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM130 — Institutional Subscriber / Third Party Payer (Record Type 30)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM130 = List.of(
        // KEY
        new FieldSpec("FM130-SER-NUM-LOCAL-PLAN",       1,   3,  ALPHA),
        new FieldSpec("FM130-SER-NUM-JULDT-CC",         4,   2,  ALPHA),
        new FieldSpec("FM130-SER-NUM-JULDT-YY",         6,   2,  ALPHA),
        new FieldSpec("FM130-SER-NUM-JULDT-DDD",        8,   3,  ALPHA),
        new FieldSpec("FM130-SER-NUM-SEQUENCE",        11,   5,  ALPHA),
        new FieldSpec("FM130-SER-NUM-SUFFIX",          16,   2,  ALPHA),
        new FieldSpec("FM130-TRANS-ID",                18,   2,  ALPHA),
        new FieldSpec("FM130-TRANS-QUAL",              20,   2,  ALPHA),
        new FieldSpec("FM130-REC-TYPE",                22,   2,  ALPHA),  // '30'
        new FieldSpec("FM130-SEQ-NUM",                 24,   2,  BINARY), // S9(4) COMP

        // DATA
        new FieldSpec("FM130-CLM-OTH-CARR-FNCL-CD",    26,   1,  ALPHA),
        new FieldSpec("FM130-CNTL-PLAN-CD",            27,   3,  ALPHA),
        new FieldSpec("FM130-SUB-ID-PFX-1-3",          30,   3,  ALPHA),
        new FieldSpec("FM130-SUB-ID-SUFX-4-17",        33,  14,  ALPHA),
        new FieldSpec("FM130-PAYER-PLAN-NAME",         47,  25,  ALPHA),
        new FieldSpec("FM130-SUB-GRP-NUM",             72,   9,  ALPHA),
        new FieldSpec("FM130-SUB-LAST-NAME",           81,  20,  ALPHA),
        new FieldSpec("FM130-SUB-FIRST-NAME",         101,  10,  ALPHA),
        new FieldSpec("FM130-SUB-MID-INIT",           111,   1,  ALPHA),
        new FieldSpec("FM130-REL-INFO-CD",            112,   1,  ALPHA),
        new FieldSpec("FM130-ASGNMT-BNFT-IND",        113,   1,  ALPHA),
        new FieldSpec("FM130-PAT-REL-SUB",            114,   2,  ALPHA),
        new FieldSpec("FM130-ITS-PAYER-IND",          116,   1,  ALPHA),
        new FieldSpec("FM130-MED-CLM-NUM",            117,  12,  ALPHA),
        new FieldSpec("FM130-MED-CLM-REJ-CD-CLM",     129,   5,  ALPHA),
        new FieldSpec("FM130-SEC-PAYOR-PRC-QUAL",     134,   1,  ALPHA),
        new FieldSpec("FM130-MED-ASGNMT-IND",         135,   1,  ALPHA),
        new FieldSpec("FM130-MPPQ-CD-ASGN",           136,   1,  ALPHA),
        new FieldSpec("FM130-MPPQ-CD-UNASGN",         137,   1,  ALPHA),
        new FieldSpec("FM130-SRC-PMT-CD",             138,   2,  ALPHA),
        new FieldSpec("FM130-FILLER-1",               140, 116,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM140 — Institutional Claim-Treatment Authorization/Occurrence (Record Type 40)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM140 = List.of(
        // KEY
        new FieldSpec("FM140-SER-NUM-LOCAL-PLAN",      1,   3,  ALPHA),
        new FieldSpec("FM140-SER-NUM-JULDT-CC",        4,   2,  ALPHA),
        new FieldSpec("FM140-SER-NUM-JULDT-YY",        6,   2,  ALPHA),
        new FieldSpec("FM140-SER-NUM-JULDT-DDD",       8,   3,  ALPHA),
        new FieldSpec("FM140-SER-NUM-SEEQUENCE",      11,   5,  ALPHA),
        new FieldSpec("FM140-SER-NUM-SUFFIX",         16,   2,  ALPHA),
        new FieldSpec("FM140-TRANS-ID",               18,   2,  ALPHA),
        new FieldSpec("FM140-TRANS-QUAL",             20,   2,  ALPHA),
        new FieldSpec("FM140-REC-TYPE",               22,   2,  ALPHA),   // '40'
        new FieldSpec("FM140-SEQ-NUM",                24,   2,  BINARY),  // S9(4) COMP

        // DATA
        new FieldSpec("FM140-BNFT-MGT-TRTMT-AUTH-NUM-1", 26, 25, ALPHA),
        new FieldSpec("FM140-BNFT-MGT-TRTMT-AUTH-NUM-2", 51, 25, ALPHA),
        new FieldSpec("FM140-BNFT-MGT-TRTMT-AUTH-NUM-3", 76, 25, ALPHA),
        new FieldSpec("FM140-FILLER-1",               101, 155, ALPHA)
    );

    // --------------------------------------------------------------------
    // FM150 — Institutional Claim (Record Type 50)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM150 = List.of(
        // KEY
        new FieldSpec("FM150-SER-NUM-LOCAL-PLAN",            1,   3,  ALPHA),
        new FieldSpec("FM150-SER-NUM-JULDT-CC",              4,   2,  ALPHA),
        new FieldSpec("FM150-SER-NUM-JULDT-YY",              6,   2,  ALPHA),
        new FieldSpec("FM150-SER-NUM-JULDT-DDD",             8,   3,  ALPHA),
        new FieldSpec("FM150-SER-NUM-SEQUENCE",             11,   5,  ALPHA),
        new FieldSpec("FM150-SER-NUM-SUFFIX",               16,   2,  ALPHA),
        new FieldSpec("FM150-TRANS-ID",                     18,   2,  ALPHA),
        new FieldSpec("FM150-TRANS-QUAL",                   20,   2,  ALPHA),
        new FieldSpec("FM150-REC-TYPE",                     22,   2,  ALPHA),       // '50'
        new FieldSpec("FM150-SEQ-NUM",                      24,   2,  BINARY),      // S9(4) COMP

        // DATA
        new FieldSpec("FM150-PRC-MTD-CLM",                  26,   2,  ALPHA),
        new FieldSpec("FM150-RULE-NUM-PRIM-CLM-1-3",        28,   3,  ALPHA),
        new FieldSpec("FM150-RULE-NUM-PRIM-CLM-4-6",        31,   3,  ALPHA),
        new FieldSpec("FM150-RULE-NUM-SEC-CLM-1-3",         34,   3,  ALPHA),
        new FieldSpec("FM150-RULE-NUM-SEC-CLM-4-6",         37,   3,  ALPHA),

        new FieldSpec("FM150-PCT-FCTR-CLM",                 40,   3,  PACKED_DECIMAL, 2), // S9(3)V99
        new FieldSpec("FM150-AVG-SEMI-PRIV-RM-RATE",        43,   6,  PACKED_DECIMAL, 2), // S9(8)V99
        new FieldSpec("FM150-PRIV-RM-RATE",                 49,   6,  PACKED_DECIMAL, 2), // S9(8)V99
        new FieldSpec("FM150-PER-DIEM-RATE",                55,   6,  PACKED_DECIMAL, 2), // S9(8)V99

        new FieldSpec("FM150-DRG-CD",                       61,   4,  ALPHA),
        new FieldSpec("FM150-CASE-ALLW-AMT",                65,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-SUPPL-AMT",                    71,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-ACCESS-FEE-PCT",               77,   3,  PACKED_DECIMAL, 2),

        new FieldSpec("FM150-ATTACH-IND",                   80,   1,  ALPHA),
        new FieldSpec("FM150-POS-IND",                      81,   1,  ALPHA),

        // SF message codes (5 × X(4))
        new FieldSpec("FM150-SF-MSG-CD-CLM-1",              82,   4,  ALPHA),
        new FieldSpec("FM150-SF-MSG-CD-CLM-2",              86,   4,  ALPHA),
        new FieldSpec("FM150-SF-MSG-CD-CLM-3",              90,   4,  ALPHA),
        new FieldSpec("FM150-SF-MSG-CD-CLM-4",              94,   4,  ALPHA),
        new FieldSpec("FM150-SF-MSG-CD-CLM-5",              98,   4,  ALPHA),

        // Special pricing condition codes (5 × X(3))
        new FieldSpec("FM150-SPEC-PRC-COND-CD-CLM-1",      102,   3,  ALPHA),
        new FieldSpec("FM150-SPEC-PRC-COND-CD-CLM-2",      105,   3,  ALPHA),
        new FieldSpec("FM150-SPEC-PRC-COND-CD-CLM-3",      108,   3,  ALPHA),
        new FieldSpec("FM150-SPEC-PRC-COND-CD-CLM-4",      111,   3,  ALPHA),
        new FieldSpec("FM150-SPEC-PRC-COND-CD-CLM-5",      114,   3,  ALPHA),

        // Amounts (5 × S9(8)V99 COMP-3 => 6 bytes, scale 2)
        new FieldSpec("FM150-SPEC-PRC-COND-AMT-CLM-1",     117,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-SPEC-PRC-COND-AMT-CLM-2",     123,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-SPEC-PRC-COND-AMT-CLM-3",     129,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-SPEC-PRC-COND-AMT-CLM-4",     135,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM150-SPEC-PRC-COND-AMT-CLM-5",     141,   6,  PACKED_DECIMAL, 2),

        // Percentages (5 × S9(3)V9(5) COMP-3 => 5 bytes, scale 5)
        new FieldSpec("FM150-SPEC-PRC-COND-PCT-CLM-1",     147,   5,  PACKED_DECIMAL, 5),
        new FieldSpec("FM150-SPEC-PRC-COND-PCT-CLM-2",     152,   5,  PACKED_DECIMAL, 5),
        new FieldSpec("FM150-SPEC-PRC-COND-PCT-CLM-3",     157,   5,  PACKED_DECIMAL, 5),
        new FieldSpec("FM150-SPEC-PRC-COND-PCT-CLM-4",     162,   5,  PACKED_DECIMAL, 5),
        new FieldSpec("FM150-SPEC-PRC-COND-PCT-CLM-5",     167,   5,  PACKED_DECIMAL, 5),

        new FieldSpec("FM150-PPO-AVBL",                    172,   1,  ALPHA),
        new FieldSpec("FM150-PPO-PRV-TYP-AVL-CLM",         173,   1,  ALPHA),
        new FieldSpec("FM150-INDV-CASE-MGMT-STAT",         174,   1,  ALPHA),
        new FieldSpec("FM150-HOST-OPL-PROV-ARRNG-CD",      175,   1,  ALPHA),

        // Special pricing condition days (5 × S9(3) => 3 bytes text)
        new FieldSpec("FM150-SPEC-PRC-COND-DAYS-1",        176,   3,  NUMERIC_TEXT),
        new FieldSpec("FM150-SPEC-PRC-COND-DAYS-2",        179,   3,  NUMERIC_TEXT),
        new FieldSpec("FM150-SPEC-PRC-COND-DAYS-3",        182,   3,  NUMERIC_TEXT),
        new FieldSpec("FM150-SPEC-PRC-COND-DAYS-4",        185,   3,  NUMERIC_TEXT),
        new FieldSpec("FM150-SPEC-PRC-COND-DAYS-5",        188,   3,  NUMERIC_TEXT),

        // Info-only message codes (5 × X(4))
        new FieldSpec("FM150-SF-INFO-ONLY-MSG-CD-CLM-1",   191,   4,  ALPHA),
        new FieldSpec("FM150-SF-INFO-ONLY-MSG-CD-CLM-2",   195,   4,  ALPHA),
        new FieldSpec("FM150-SF-INFO-ONLY-MSG-CD-CLM-3",   199,   4,  ALPHA),
        new FieldSpec("FM150-SF-INFO-ONLY-MSG-CD-CLM-4",   203,   4,  ALPHA),
        new FieldSpec("FM150-SF-INFO-ONLY-MSG-CD-CLM-5",   207,   4,  ALPHA),

        new FieldSpec("FM150-FILLER-1",                    211,  45,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM160 — Institutional Line Level (Record Type 60)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM160 = List.of(
        // KEY
        new FieldSpec("FM160-SER-NUM-LOCAL-PLAN",        1,   3,  ALPHA),
        new FieldSpec("FM160-SER-NUM-JULDT-CC",          4,   2,  ALPHA),
        new FieldSpec("FM160-SER-NUM-JULDT-YY",          6,   2,  ALPHA),
        new FieldSpec("FM160-SER-NUM-JULDT-DDD",         8,   3,  ALPHA),
        new FieldSpec("FM160-SER-NUM-SEQUENCE",         11,   5,  ALPHA),
        new FieldSpec("FM160-SER-NUM-SUFFIX",           16,   2,  ALPHA),
        new FieldSpec("FM160-TRANS-ID",                 18,   2,  ALPHA),
        new FieldSpec("FM160-TRANS-QUAL",               20,   2,  ALPHA),
        new FieldSpec("FM160-REC-TYPE",                 22,   2,  ALPHA),     // '60'
        new FieldSpec("FM160-SEQ-NUM",                  24,   2,  BINARY),    // S9(4) COMP

        // DATA
        new FieldSpec("FM160-REV-CD",                   26,   4,  NUMERIC_TEXT),   // 9(04)
        new FieldSpec("FM160-HCPCS-PROC-CD",            30,   5,  ALPHA),
        new FieldSpec("FM160-HCPCS-PROC-CD-MOD-1",      35,   2,  ALPHA),
        new FieldSpec("FM160-HCPCS-PROC-CD-MOD-2",      37,   2,  ALPHA),
        new FieldSpec("FM160-NUM-SERV",                 39,   4,  NUMERIC_TEXT),   // S9(04)

        new FieldSpec("FM160-DT-SERV-CC",               43,   2,  ALPHA),
        new FieldSpec("FM160-DT-SERV-YY",               45,   2,  ALPHA),
        new FieldSpec("FM160-DT-SERV-MM",               47,   2,  ALPHA),
        new FieldSpec("FM160-DT-SERV-DD",               49,   2,  ALPHA),

        new FieldSpec("FM160-SERV-CHRG",                51,   6,  PACKED_DECIMAL, 2), // S9(8)V99 COMP-3
        new FieldSpec("FM160-ACCOM-RATE",               57,   6,  PACKED_DECIMAL, 2), // S9(8)V99 COMP-3

        new FieldSpec("FM160-PERF-PROV-TXNMY-CD-LN",    63,  15,  ALPHA),
        new FieldSpec("FM160-HCPCS-PROC-CD-MOD-3",      78,   2,  ALPHA),
        new FieldSpec("FM160-HCPCS-PROC-CD-MOD-4",      80,   2,  ALPHA),
        new FieldSpec("FM160-NDC-CD",                   82,  13,  ALPHA),
        new FieldSpec("FM160-LN-ITEM-CNTL-NUM",         95,  30,  ALPHA),

        // 5 occurrences: (2 + 5 + 6) = 13 bytes each
        new FieldSpec("FM160-CLM-ADJ-GP-CD-LN-PAYB-1", 125,   2,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-RSN-CD-LN-PAYB-1",127,   5,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-AMT-LN-PAYB-1",   132,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM160-CLM-ADJ-GP-CD-LN-PAYB-2", 138,   2,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-RSN-CD-LN-PAYB-2",140,   5,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-AMT-LN-PAYB-2",   145,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM160-CLM-ADJ-GP-CD-LN-PAYB-3", 151,   2,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-RSN-CD-LN-PAYB-3",153,   5,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-AMT-LN-PAYB-3",   158,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM160-CLM-ADJ-GP-CD-LN-PAYB-4", 164,   2,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-RSN-CD-LN-PAYB-4",166,   5,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-AMT-LN-PAYB-4",   171,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM160-CLM-ADJ-GP-CD-LN-PAYB-5", 177,   2,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-RSN-CD-LN-PAYB-5",179,   5,  ALPHA),
        new FieldSpec("FM160-CLM-ADJ-AMT-LN-PAYB-5",   184,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM160-REV-TYPE-CD",             190,   1,  ALPHA),

        new FieldSpec("FM160-SERV-ELIG-CD-LN-1",       191,   1,  ALPHA),
        new FieldSpec("FM160-SERV-ELIG-CD-LN-2",       192,   1,  ALPHA),
        new FieldSpec("FM160-SERV-ELIG-CD-LN-3",       193,   1,  ALPHA),

        new FieldSpec("FM160-FILLER-1",                194,  62,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM171 — Institutional Medical 2 (Record Type 71)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM171 = List.of(
        // KEY
        new FieldSpec("FM171-SER-NUM-LOCAL-PLAN",         1,   3,  ALPHA),
        new FieldSpec("FM171-SER-NUM-JULDT-CC",           4,   2,  ALPHA),
        new FieldSpec("FM171-SER-NUM-JULDT-YY",           6,   2,  ALPHA),
        new FieldSpec("FM171-SER-NUM-JULDT-DDD",          8,   3,  ALPHA),
        new FieldSpec("FM171-SER-NUM-SEQUENCE",          11,   5,  ALPHA),
        new FieldSpec("FM171-SER-NUM-SUFFIX",            16,   2,  ALPHA),
        new FieldSpec("FM171-TRANS-ID",                  18,   2,  ALPHA),
        new FieldSpec("FM171-TRANS-QUAL",                20,   2,  ALPHA),
        new FieldSpec("FM171-REC-TYPE",                  22,   2,  ALPHA),  // '71'
        new FieldSpec("FM171-SEQ-NUM",                   24,   2,  BINARY),

        // DATA
        new FieldSpec("FM171-ICD-ADMIT-DIAG-CD",         26,   7,  ALPHA),

        new FieldSpec("FM171-ICD-PAT-VISIT-RSN-CD-1",    33,   7,  ALPHA),
        new FieldSpec("FM171-ICD-PAT-VISIT-RSN-CD-2",    40,   7,  ALPHA),
        new FieldSpec("FM171-ICD-PAT-VISIT-RSN-CD-3",    47,   7,  ALPHA),

        new FieldSpec("FM171-PRINC-DIAG-CD-POA-IND",     54,   1,  ALPHA),
        new FieldSpec("FM171-ICD-PRINC-DIAG-CD",         55,   7,  ALPHA),

        // 24 × POA indicators X(1)
        new FieldSpec("FM171-DIAG-CD-POA-IND-1",         62,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-2",         63,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-3",         64,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-4",         65,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-5",         66,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-6",         67,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-7",         68,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-8",         69,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-9",         70,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-10",        71,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-11",        72,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-12",        73,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-13",        74,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-14",        75,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-15",        76,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-16",        77,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-17",        78,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-18",        79,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-19",        80,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-20",        81,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-21",        82,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-22",        83,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-23",        84,   1,  ALPHA),
        new FieldSpec("FM171-DIAG-CD-POA-IND-24",        85,   1,  ALPHA),

        // 24 × ICD diagnosis codes X(7): 86 .. 253
        new FieldSpec("FM171-ICD-DIAG-CD-1",             86,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-2",             93,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-3",            100,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-4",            107,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-5",            114,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-6",            121,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-7",            128,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-8",            135,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-9",            142,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-10",           149,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-11",           156,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-12",           163,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-13",           170,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-14",           177,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-15",           184,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-16",           191,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-17",           198,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-18",           205,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-19",           212,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-20",           219,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-21",           226,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-22",           233,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-23",           240,   7,  ALPHA),
        new FieldSpec("FM171-ICD-DIAG-CD-24",           247,   7,  ALPHA),

        new FieldSpec("FM171-FILLER-1",                 254,   2,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM172 — Institutional Medical 3 (Record Type 72)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM172 = List.of(
        // KEY
        new FieldSpec("FM172-SER-NUM-LOCAL-PLAN",       1,   3,  ALPHA),
        new FieldSpec("FM172-SER-NUM-JULDT-CC",         4,   2,  ALPHA),
        new FieldSpec("FM172-SER-NUM-JULDT-YY",         6,   2,  ALPHA),
        new FieldSpec("FM172-SER-NUM-JULDT-DDD",        8,   3,  ALPHA),
        new FieldSpec("FM172-SER-NUM-SEQUENCE",        11,   5,  ALPHA),
        new FieldSpec("FM172-SER-NUM-SUFFIX",          16,   2,  ALPHA),
        new FieldSpec("FM172-TRANS-ID",                18,   2,  ALPHA),
        new FieldSpec("FM172-TRANS-QUAL",              20,   2,  ALPHA),
        new FieldSpec("FM172-REC-TYPE",                22,   2,  ALPHA),  // '72'
        new FieldSpec("FM172-SEQ-NUM",                 24,   2,  BINARY),

        // DATA
        new FieldSpec("FM172-ICD-PRINC-PROC-CD",       26,   7,  ALPHA),

        new FieldSpec("FM172-ICD-PROC-CD-1",           33,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-2",           40,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-3",           47,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-4",           54,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-5",           61,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-6",           68,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-7",           75,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-8",           82,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-9",           89,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-10",          96,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-11",         103,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-12",         110,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-13",         117,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-14",         124,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-15",         131,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-16",         138,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-17",         145,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-18",         152,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-19",         159,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-20",         166,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-21",         173,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-22",         180,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-23",         187,   7,  ALPHA),
        new FieldSpec("FM172-ICD-PROC-CD-24",         194,   7,  ALPHA),

        new FieldSpec("FM172-FILLER-1",                201,  55,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM173 — Institutional Medical 4 (Record Type 73) — OCCURS expanded (A)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM173 = List.of(
        // KEY
        new FieldSpec("FM173-SER-NUM-LOCAL-PLAN",       1,   3,  ALPHA),
        new FieldSpec("FM173-SER-NUM-JULDT-CC",         4,   2,  ALPHA),
        new FieldSpec("FM173-SER-NUM-JULDT-YY",         6,   2,  ALPHA),
        new FieldSpec("FM173-SER-NUM-JULDT-DDD",        8,   3,  ALPHA),
        new FieldSpec("FM173-SER-NUM-SEQUENCE",        11,   5,  ALPHA),
        new FieldSpec("FM173-SER-NUM-SUFFIX",          16,   2,  ALPHA),
        new FieldSpec("FM173-TRANS-ID",                18,   2,  ALPHA),
        new FieldSpec("FM173-TRANS-QUAL",              20,   2,  ALPHA),
        new FieldSpec("FM173-REC-TYPE",                22,   2,  ALPHA),  // '73'
        new FieldSpec("FM173-SEQ-NUM",                 24,   2,  BINARY),

        // Principal procedure date (CC YY MM DD) — 26..33
        new FieldSpec("FM173-PRINC-PROC-DT-CC",        26,   2,  ALPHA),
        new FieldSpec("FM173-PRINC-PROC-DT-YY",        28,   2,  ALPHA),
        new FieldSpec("FM173-PRINC-PROC-DT-MM",        30,   2,  ALPHA),
        new FieldSpec("FM173-PRINC-PROC-DT-DD",        32,   2,  ALPHA),

        // 24× Procedure dates (each 8 bytes; CC YY MM DD), starting at 34
        new FieldSpec("FM173-PROC-DT-CC-1",            34,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-1",            36,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-1",            38,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-1",            40,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-2",            42,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-2",            44,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-2",            46,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-2",            48,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-3",            50,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-3",            52,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-3",            54,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-3",            56,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-4",            58,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-4",            60,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-4",            62,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-4",            64,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-5",            66,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-5",            68,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-5",            70,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-5",            72,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-6",            74,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-6",            76,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-6",            78,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-6",            80,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-7",            82,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-7",            84,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-7",            86,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-7",            88,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-8",            90,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-8",            92,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-8",            94,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-8",            96,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-9",            98,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-9",           100,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-9",           102,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-9",           104,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-10",          106,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-10",          108,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-10",          110,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-10",          112,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-11",          114,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-11",          116,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-11",          118,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-11",          120,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-12",          122,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-12",          124,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-12",          126,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-12",          128,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-13",          130,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-13",          132,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-13",          134,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-13",          136,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-14",          138,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-14",          140,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-14",          142,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-14",          144,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-15",          146,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-15",          148,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-15",          150,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-15",          152,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-16",          154,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-16",          156,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-16",          158,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-16",          160,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-17",          162,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-17",          164,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-17",          166,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-17",          168,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-18",          170,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-18",          172,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-18",          174,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-18",          176,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-19",          178,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-19",          180,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-19",          182,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-19",          184,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-20",          186,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-20",          188,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-20",          190,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-20",          192,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-21",          194,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-21",          196,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-21",          198,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-21",          200,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-22",          202,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-22",          204,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-22",          206,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-22",          208,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-23",          210,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-23",          212,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-23",          214,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-23",          216,   2,  ALPHA),

        new FieldSpec("FM173-PROC-DT-CC-24",          218,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-YY-24",          220,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-MM-24",          222,   2,  ALPHA),
        new FieldSpec("FM173-PROC-DT-DD-24",          224,   2,  ALPHA),

        new FieldSpec("FM173-FILLER-1",               226,  30,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM180 — Institutional Physician (Record Type 80)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM180 = List.of(
        // KEY
        new FieldSpec("FM180-SER-NUM-LOCAL-PLAN",        1,   3,  ALPHA),
        new FieldSpec("FM180-SER-NUM-JULDT-CC",          4,   2,  ALPHA),
        new FieldSpec("FM180-SER-NUM-JULDT-YY",          6,   2,  ALPHA),
        new FieldSpec("FM180-SER-NUM-JULDT-DDD",         8,   3,  ALPHA),
        new FieldSpec("FM180-SER-NUM-SEQUENCE",         11,   5,  ALPHA),
        new FieldSpec("FM180-SER-NUM-SUFFIX",           16,   2,  ALPHA),
        new FieldSpec("FM180-TRANS-ID",                 18,   2,  ALPHA),
        new FieldSpec("FM180-TRANS-QUAL",               20,   2,  ALPHA),
        new FieldSpec("FM180-REC-TYPE",                 22,   2,  ALPHA),  // '80'
        new FieldSpec("FM180-SEQ-NUM",                  24,   2,  BINARY),

        // DATA
        new FieldSpec("FM180-ATTD-PHY-NUM",             26,  16,  ALPHA),
        new FieldSpec("FM180-OP-PHY-NUM",               42,  16,  ALPHA),
        new FieldSpec("FM180-OTH-PHY-NUM-1",            58,  16,  ALPHA),
        new FieldSpec("FM180-OTH-PHY-NUM-2",            74,  16,  ALPHA),

        new FieldSpec("FM180-ATTD-PHY-LAST-NAME",       90,  20,  ALPHA),
        new FieldSpec("FM180-ATTD-PHY-FIRST-NAME",     110,  10,  ALPHA),
        new FieldSpec("FM180-ATTD-PHY-MID-NAME",       120,   1,  ALPHA),

        new FieldSpec("FM180-OP-PHY-LAST-NAME",        121,  20,  ALPHA),
        new FieldSpec("FM180-OP-PHY-FIRST-NAME",       141,  10,  ALPHA),
        new FieldSpec("FM180-OP-PHY-MID-NAME",         151,   1,  ALPHA),

        new FieldSpec("FM180-OTH1-PHY-LAST-NAME",      152,  20,  ALPHA),
        new FieldSpec("FM180-OTH1-PHY-FIRST-NAME",     172,  10,  ALPHA),
        new FieldSpec("FM180-OTH1-PHY-MID-NAME",       182,   1,  ALPHA),

        new FieldSpec("FM180-OTH2-PHY-LAST-NAME",      183,  20,  ALPHA),
        new FieldSpec("FM180-OTH2-PHY-FIRST-NAME",     203,  10,  ALPHA),
        new FieldSpec("FM180-OTH2-PHY-MID-NAME",       213,   1,  ALPHA),

        new FieldSpec("FM180-ATTD-PHY-NUM-QUAL",       214,   2,  ALPHA),
        new FieldSpec("FM180-OP-PHY-NUM-QUAL",         216,   2,  ALPHA),
        new FieldSpec("FM180-OTH-PHY-NUM-1-QUAL",      218,   2,  ALPHA),

        new FieldSpec("FM180-FILLER-1",                220,  36,  ALPHA)
    );

    // --------------------------------------------------------------------
    // FM190 — Institutional Trailer (Record Type 90)
    // --------------------------------------------------------------------
    public static final List<FieldSpec> FM190 = List.of(
        // KEY
        new FieldSpec("FM190-SER-NUM-LOCAL-PLAN",          1,   3,  ALPHA),
        new FieldSpec("FM190-SER-NUM-JULDT-CC",            4,   2,  ALPHA),
        new FieldSpec("FM190-SER-NUM-JULDT-YY",            6,   2,  ALPHA),
        new FieldSpec("FM190-SER-NUM-JULDT-DDD",           8,   3,  ALPHA),
        new FieldSpec("FM190-SER-NUM-SEQUENCE",           11,   5,  ALPHA),
        new FieldSpec("FM190-SER-NUM-SUFFIX",             16,   2,  ALPHA),
        new FieldSpec("FM190-TRANS-ID",                   18,   2,  ALPHA),
        new FieldSpec("FM190-TRANS-QUAL",                 20,   2,  ALPHA),
        new FieldSpec("FM190-REC-TYPE",                   22,   2,  ALPHA),     // '90'
        new FieldSpec("FM190-SEQ-NUM",                    24,   2,  BINARY),

        // DATA
        new FieldSpec("FM190-PHY-REC-CNT",                26,   4,  NUMERIC_TEXT), // S9(04)
        new FieldSpec("FM190-REC-TYPE-1X-CNT",            30,   2,  NUMERIC_TEXT), // S9(02)
        new FieldSpec("FM190-REC-TYPE-2X-CNT",            32,   2,  NUMERIC_TEXT),
        new FieldSpec("FM190-REC-TYPE-3X-CNT",            34,   2,  NUMERIC_TEXT),
        new FieldSpec("FM190-REC-TYPE-4X-CNT",            36,   4,  NUMERIC_TEXT), // S9(04)
        new FieldSpec("FM190-REC-TYPE-5X-CNT",            40,   2,  NUMERIC_TEXT),
        new FieldSpec("FM190-REC-TYPE-6X-CNT",            42,   4,  NUMERIC_TEXT), // S9(04)
        new FieldSpec("FM190-REC-TYPE-7X-CNT",            46,   2,  NUMERIC_TEXT),
        new FieldSpec("FM190-REC-TYPE-8X-CNT",            48,   2,  NUMERIC_TEXT),
        new FieldSpec("FM190-TOT-NUM-DAY-VIS",            50,   4,  NUMERIC_TEXT), // S9(04)

        new FieldSpec("FM190-TOT-ACCOM-CHRG",             54,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM190-TOT-ACCOM-NON-COV-CHRG",     60,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM190-TOT-ANC-CHRG",               66,   6,  PACKED_DECIMAL, 2),
        new FieldSpec("FM190-TOT-ANC-NON-COV-CHRG",       72,   6,  PACKED_DECIMAL, 2),

        new FieldSpec("FM190-NARRATIVE",                  78, 116,  ALPHA),
        new FieldSpec("FM190-BLUE2-USER-ID",             194,  40,  ALPHA),
        new FieldSpec("FM190-FILLER-1",                  234,  22,  ALPHA)
    );
    


 // ===== FM132_schema =====

 // FM132 — Institutional OPL Claim Level (Record Type 32)
 public static final List<FieldSpec> FM132 = List.of(
     new FieldSpec("FM132-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM132-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM132-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM132-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM132-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM132-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM132-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM132-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM132-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM132-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-1", 26, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-2", 28, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-3", 30, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-4", 32, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-5", 34, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-6", 36, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-7", 38, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-8", 40, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-9", 42, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-10", 44, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-11", 46, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-12", 48, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-13", 50, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-14", 52, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-15", 54, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-CD-CLM-16", 56, 2, ALPHA),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-1", 58, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-2", 64, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-3", 70, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-4", 76, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-5", 82, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-6", 88, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-7", 94, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-8", 100, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-9", 106, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-10", 112, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-11", 118, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-12", 124, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-13", 130, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-14", 136, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-15", 142, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-OPL-VAL-AMT-CLM-16", 148, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM132-FILLER-1", 154, 102, ALPHA)
 );

 // ===== FM142_schema =====

 // FM142 — Institutional Claim 2 (Record Type 42)
 public static final List<FieldSpec> FM142 = List.of(
     new FieldSpec("FM142-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM142-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM142-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM142-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM142-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM142-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM142-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM142-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM142-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM142-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM142-VAL-CD-1", 26, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-2", 28, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-3", 30, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-4", 32, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-5", 34, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-6", 36, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-7", 38, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-8", 40, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-9", 42, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-10", 44, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-11", 46, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-12", 48, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-13", 50, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-14", 52, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-15", 54, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-16", 56, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-17", 58, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-18", 60, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-19", 62, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-20", 64, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-21", 66, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-22", 68, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-23", 70, 2, ALPHA),
     new FieldSpec("FM142-VAL-CD-24", 72, 2, ALPHA),
     new FieldSpec("FM142-VAL-AMT-1", 74, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-2", 80, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-3", 86, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-4", 92, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-5", 98, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-6", 104, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-7", 110, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-8", 116, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-9", 122, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-10", 128, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-11", 134, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-12", 140, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-13", 146, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-14", 152, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-15", 158, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-16", 164, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-17", 170, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-18", 176, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-19", 182, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-20", 188, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-21", 194, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-22", 200, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-23", 206, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-VAL-AMT-24", 212, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM142-FILLER-1", 218, 38, ALPHA)
 );

 // ===== FM143_schema =====

 // FM143 — Institutional Claim 3 (Record Type 43)
 public static final List<FieldSpec> FM143 = List.of(
     new FieldSpec("FM143-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM143-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM143-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM143-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM143-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM143-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM143-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM143-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM143-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM143-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM143-OCC-CD-1", 26, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-2", 28, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-3", 30, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-4", 32, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-5", 34, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-6", 36, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-7", 38, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-8", 40, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-9", 42, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-10", 44, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-11", 46, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-12", 48, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-13", 50, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-14", 52, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-15", 54, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-16", 56, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-17", 58, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-18", 60, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-19", 62, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-20", 64, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-21", 66, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-22", 68, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-23", 70, 2, ALPHA),
     new FieldSpec("FM143-OCC-CD-24", 72, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-1", 74, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-2", 76, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-3", 78, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-4", 80, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-5", 82, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-6", 84, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-7", 86, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-8", 88, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-9", 90, 2, ALPHA),
     new FieldSpec("FM143-OCC-SPAN-CD-10", 92, 2, ALPHA),
     new FieldSpec("FM143-FILLER-1", 94, 162, ALPHA)
 );

 // ===== FM144_schema =====

 // FM144 — Institutional Claim 4 (Record Type 44)
 public static final List<FieldSpec> FM144 = List.of(
     new FieldSpec("FM144-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM144-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM144-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM144-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM144-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM144-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM144-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM144-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM144-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM144-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM144-OCC-DT-CC-1", 26, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-1", 28, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-1", 30, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-1", 32, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-2", 34, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-2", 36, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-2", 38, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-2", 40, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-3", 42, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-3", 44, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-3", 46, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-3", 48, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-4", 50, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-4", 52, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-4", 54, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-4", 56, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-5", 58, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-5", 60, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-5", 62, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-5", 64, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-6", 66, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-6", 68, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-6", 70, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-6", 72, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-7", 74, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-7", 76, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-7", 78, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-7", 80, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-8", 82, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-8", 84, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-8", 86, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-8", 88, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-9", 90, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-9", 92, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-9", 94, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-9", 96, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-10", 98, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-10", 100, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-10", 102, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-10", 104, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-11", 106, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-11", 108, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-11", 110, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-11", 112, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-12", 114, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-12", 116, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-12", 118, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-12", 120, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-13", 122, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-13", 124, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-13", 126, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-13", 128, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-14", 130, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-14", 132, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-14", 134, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-14", 136, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-15", 138, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-15", 140, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-15", 142, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-15", 144, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-16", 146, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-16", 148, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-16", 150, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-16", 152, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-17", 154, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-17", 156, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-17", 158, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-17", 160, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-18", 162, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-18", 164, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-18", 166, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-18", 168, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-19", 170, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-19", 172, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-19", 174, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-19", 176, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-20", 178, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-20", 180, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-20", 182, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-20", 184, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-21", 186, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-21", 188, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-21", 190, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-21", 192, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-22", 194, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-22", 196, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-22", 198, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-22", 200, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-23", 202, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-23", 204, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-23", 206, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-23", 208, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-CC-24", 210, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-YY-24", 212, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-MM-24", 214, 2, ALPHA),
     new FieldSpec("FM144-OCC-DT-DD-24", 216, 2, ALPHA),
     new FieldSpec("FM144-FILLER-1", 218, 38, ALPHA)
 );

 // ===== FM145_schema =====

 // FM145 — Institutional Claim 7 (Record Type 45)
 public static final List<FieldSpec> FM145 = List.of(
     new FieldSpec("FM145-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM145-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM145-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM145-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM145-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM145-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM145-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM145-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM145-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM145-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM145-OCC-FROM-DT-CC-1", 26, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-1", 28, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-1", 30, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-1", 32, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-2", 34, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-2", 36, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-2", 38, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-2", 40, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-3", 42, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-3", 44, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-3", 46, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-3", 48, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-4", 50, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-4", 52, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-4", 54, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-4", 56, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-5", 58, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-5", 60, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-5", 62, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-5", 64, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-6", 66, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-6", 68, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-6", 70, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-6", 72, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-7", 74, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-7", 76, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-7", 78, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-7", 80, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-8", 82, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-8", 84, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-8", 86, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-8", 88, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-9", 90, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-9", 92, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-9", 94, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-9", 96, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-CC-10", 98, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-YY-10", 100, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-MM-10", 102, 2, ALPHA),
     new FieldSpec("FM145-OCC-FROM-DT-DD-10", 104, 2, ALPHA),
     new FieldSpec("FM145-FILLER-1", 106, 150, ALPHA)
 );

 // ===== FM146_schema =====

 // FM146 — Institutional Special Notations (Record Type 46)
 public static final List<FieldSpec> FM146 = List.of(
     new FieldSpec("FM146-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM146-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM146-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM146-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM146-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM146-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM146-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM146-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM146-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM146-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM146-SPEC-NOTE-TRNSM-IND", 26, 1, ALPHA),
     new FieldSpec("FM146-SPEC-NOTE-CD", 27, 5, ALPHA),
     new FieldSpec("FM146-SPEC-NOTE-DATA", 32, 215, ALPHA),
     new FieldSpec("FM146-FILLER-1", 247, 9, ALPHA)
 );


 // ===== FM147_schema =====

 // FM147 — Institutional Claim 6 (Record Type 47)
 public static final List<FieldSpec> FM147 = List.of(
     new FieldSpec("FM147-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM147-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM147-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM147-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM147-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM147-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM147-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM147-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM147-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM147-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM147-OCC-TO-DT-CC-1", 26, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-1", 28, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-1", 30, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-1", 32, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-2", 34, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-2", 36, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-2", 38, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-2", 40, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-3", 42, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-3", 44, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-3", 46, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-3", 48, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-4", 50, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-4", 52, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-4", 54, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-4", 56, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-5", 58, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-5", 60, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-5", 62, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-5", 64, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-6", 66, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-6", 68, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-6", 70, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-6", 72, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-7", 74, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-7", 76, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-7", 78, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-7", 80, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-8", 82, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-8", 84, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-8", 86, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-8", 88, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-9", 90, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-9", 92, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-9", 94, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-9", 96, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-CC-10", 98, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-YY-10", 100, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-MM-10", 102, 2, ALPHA),
     new FieldSpec("FM147-OCC-TO-DT-DD-10", 104, 2, ALPHA),
     new FieldSpec("FM147-FILLER-1", 106, 150, ALPHA)
 );

 // ===== FM165_schema =====

 // FM165 — Institutional Line Level Pricing & POS (Record Type 65)
 public static final List<FieldSpec> FM165 = List.of(
     new FieldSpec("FM165-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM165-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM165-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM165-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM165-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM165-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM165-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM165-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM165-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM165-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM165-LOC-RATE", 26, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-PRC-MTD-LN", 32, 2, ALPHA),
     new FieldSpec("FM165-RULE-NUM-LN-1-3", 34, 3, ALPHA),
     new FieldSpec("FM165-RULE-NUM-LN-4-6", 37, 3, ALPHA),
     new FieldSpec("FM165-PCT-FCTR-LN", 40, 4, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-CAP-IND", 44, 1, ALPHA),
     new FieldSpec("FM165-LOC-PLAN-REF-NUM", 45, 25, ALPHA),
     new FieldSpec("FM165-NUM-APPV-SERV", 70, 3, NUMERIC_TEXT),
     new FieldSpec("FM165-POS-PRC-LEV-IND", 73, 1, ALPHA),
     new FieldSpec("FM165-PREATH-PRECRT-STAT-IND", 74, 1, ALPHA),
     new FieldSpec("FM165-REF-STAT-IND", 75, 1, ALPHA),
     new FieldSpec("FM165-REF-PCP-IND", 76, 2, ALPHA),
     new FieldSpec("FM165-REVIEW-DETER-COND-CD", 78, 2, ALPHA),
     new FieldSpec("FM165-PROV-BASE-PEN-AMT-LN", 80, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-PROV-BASE-PEN-PCT-LN", 86, 4, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-PRV-BSE-PEN-APL-RLE-LN", 90, 4, ALPHA),
     new FieldSpec("FM165-SF-MSG-CD-LN-1", 94, 4, ALPHA),
     new FieldSpec("FM165-SF-MSG-CD-LN-2", 98, 4, ALPHA),
     new FieldSpec("FM165-SF-MSG-CD-LN-3", 102, 4, ALPHA),
     new FieldSpec("FM165-SF-MSG-CD-LN-4", 106, 4, ALPHA),
     new FieldSpec("FM165-SF-MSG-CD-LN-5", 110, 4, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-CD-LN-1", 114, 3, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-CD-LN-2", 117, 3, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-CD-LN-3", 120, 3, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-CD-LN-4", 123, 3, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-CD-LN-5", 126, 3, ALPHA),
     new FieldSpec("FM165-SPEC-PRC-COND-AMT-LN-1", 129, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-SPEC-PRC-COND-AMT-LN-2", 135, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-SPEC-PRC-COND-AMT-LN-3", 141, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-SPEC-PRC-COND-AMT-LN-4", 147, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-SPEC-PRC-COND-AMT-LN-5", 153, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM165-SPEC-PRC-COND-PCT-LN-1", 159, 5, PACKED_DECIMAL, 5),
     new FieldSpec("FM165-SPEC-PRC-COND-PCT-LN-2", 164, 5, PACKED_DECIMAL, 5),
     new FieldSpec("FM165-SPEC-PRC-COND-PCT-LN-3", 169, 5, PACKED_DECIMAL, 5),
     new FieldSpec("FM165-SPEC-PRC-COND-PCT-LN-4", 174, 5, PACKED_DECIMAL, 5),
     new FieldSpec("FM165-SPEC-PRC-COND-PCT-LN-5", 179, 5, PACKED_DECIMAL, 5),
     new FieldSpec("FM165-INCL-GROUPING-NBR", 184, 2, ALPHA),
     new FieldSpec("FM165-ACT-AMB-MILEAGE", 186, 3, PACKED_DECIMAL, 1),
     new FieldSpec("FM165-FILLER-1", 189, 67, ALPHA)
 );

 // ===== FM166_schema =====

 // FM166 — Institutional OPL Line Level (Record Type 66)
 public static final List<FieldSpec> FM166 = List.of(
     new FieldSpec("FM166-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM166-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM166-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM166-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM166-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM166-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM166-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM166-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM166-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM166-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM166-OPL-VAL-CD-LN-1", 26, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-2", 28, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-3", 30, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-4", 32, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-5", 34, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-6", 36, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-7", 38, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-8", 40, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-9", 42, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-10", 44, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-11", 46, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-12", 48, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-13", 50, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-14", 52, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-15", 54, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-CD-LN-16", 56, 2, ALPHA),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-1", 58, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-2", 64, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-3", 70, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-4", 76, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-5", 82, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-6", 88, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-7", 94, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-8", 100, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-9", 106, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-10", 112, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-11", 118, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-12", 124, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-13", 130, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-14", 136, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-15", 142, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-OPL-VAL-AMT-LN-16", 148, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-1", 154, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-1", 156, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-1", 161, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-2", 167, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-2", 169, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-2", 174, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-3", 180, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-3", 182, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-3", 187, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-4", 193, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-4", 195, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-4", 200, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-5", 206, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-5", 208, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-5", 213, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYA-6", 219, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYA-6", 221, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYA-6", 226, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-CLM-ADJ-GP-CD-LN-PAYB", 232, 2, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-RSN-CD-LN-PAYB", 234, 5, ALPHA),
     new FieldSpec("FM166-CLM-ADJ-AMT-LN-PAYB", 239, 6, PACKED_DECIMAL, 2),
     new FieldSpec("FM166-FILLER-1", 245, 11, ALPHA)
 );

 // ===== FM174_schema =====

 // FM174 — Institutional Medical 5 (Record Type 74)
 public static final List<FieldSpec> FM174 = List.of(
     new FieldSpec("FM174-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
     new FieldSpec("FM174-SER-NUM-JULDT-CC", 4, 2, ALPHA),
     new FieldSpec("FM174-SER-NUM-JULDT-YY", 6, 2, ALPHA),
     new FieldSpec("FM174-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
     new FieldSpec("FM174-SER-NUM-SEQUENCE", 11, 5, ALPHA),
     new FieldSpec("FM174-SER-NUM-SUFFIX", 16, 2, ALPHA),
     new FieldSpec("FM174-TRANS-ID", 18, 2, ALPHA),
     new FieldSpec("FM174-TRANS-QUAL", 20, 2, ALPHA),
     new FieldSpec("FM174-REC-TYPE", 22, 2, ALPHA),
     new FieldSpec("FM174-SEQ-NUM", 24, 2, BINARY),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-1", 26, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-2", 27, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-3", 28, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-4", 29, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-5", 30, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-6", 31, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-7", 32, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-8", 33, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-9", 34, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-10", 35, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-11", 36, 1, ALPHA),
     new FieldSpec("FM174-EXT-CAUSE-INJ-POA-IND-12", 37, 1, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-1", 38, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-2", 45, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-3", 52, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-4", 59, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-5", 66, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-6", 73, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-7", 80, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-8", 87, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-9", 94, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-10", 101, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-11", 108, 7, ALPHA),
     new FieldSpec("FM174-ICD-EXT-CAUSE-INJ-12", 115, 7, ALPHA),
     new FieldSpec("FM174-FILLER-1", 122, 134, ALPHA)
 );
 
 public static final List<FieldSpec> FM9D = List.of(

		    new FieldSpec("CDB99D-SCCF-FILLER", 1, 17, ALPHA),

		    new FieldSpec("CDB99D-TRANS-ID", 18, 2, ALPHA),

		    new FieldSpec("CDB99D-FILLER-2", 20, 2, ALPHA),

		    new FieldSpec("CDB99D-REC-TYPE", 22, 2, ALPHA),

		    new FieldSpec("CDB99D-SEQ-NUM", 24, 2, BINARY),

		 

		    new FieldSpec("CDB99D-INP-SRC-ID", 26, 20, ALPHA),

		    new FieldSpec("CDB99D-CLM-TRANS-CNT", 46, 5, PACKED_DECIMAL, 0),

		    new FieldSpec("CDB99D-REC-CNT", 51, 5, PACKED_DECIMAL, 0),

		    new FieldSpec("CDB99D-CREATE-DT-CC", 56, 2, ALPHA),

		    new FieldSpec("CDB99D-CREATE-DT-YY", 58, 2, ALPHA),

		    new FieldSpec("CDB99D-CREATE-DT-MM", 60, 2, ALPHA),

		    new FieldSpec("CDB99D-CREATE-DT-DD", 62, 2, ALPHA),

		 

		    new FieldSpec("CDB99D-CREATE-TIME-HH", 64, 2, ALPHA),

		    new FieldSpec("CDB99D-CREATE-TIME-MM", 66, 2, ALPHA),

		    new FieldSpec("CDB99D-CREATE-TIME-SS", 68, 2, ALPHA),

		 

		    new FieldSpec("CDB99D-NET-LIAB-AMT", 70, 7, PACKED_DECIMAL, 2),

		 

		    new FieldSpec("CDB99D-FILLER-1", 77, 179, ALPHA)

		);
 
 public static final List<FieldSpec> FM131 = List.of(

		    new FieldSpec("FM131-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),

		    new FieldSpec("FM131-SER-NUM-JULDT-CC", 4, 2, ALPHA),

		    new FieldSpec("FM131-SER-NUM-JULDT-YY", 6, 2, ALPHA),

		    new FieldSpec("FM131-SER-NUM-JULDT-DDD", 8, 3, ALPHA),

		    new FieldSpec("FM131-SER-NUM-SEQUENCE", 11, 5, ALPHA),

		    new FieldSpec("FM131-SER-NUM-SUFFIX", 16, 2, ALPHA),

		 

		    new FieldSpec("FM131-TRANS-ID", 18, 2, ALPHA),

		    new FieldSpec("FM131-TRANS-QUAL", 20, 2, ALPHA),

		    new FieldSpec("FM131-REC-TYPE", 22, 2, ALPHA),

		    new FieldSpec("FM131-SEQ-NUM", 24, 2, BINARY),

		 

		    new FieldSpec("FM131-SUB-ADDR-LN-1", 26, 25, ALPHA),

		    new FieldSpec("FM131-SUB-ADDR-LN-2", 51, 25, ALPHA),

		    new FieldSpec("FM131-SUB-CITY", 76, 15, ALPHA),

		    new FieldSpec("FM131-SUB-ZIP-CD-5", 91, 5, ALPHA),

		    new FieldSpec("FM131-SUB-ZIP-CD-4", 96, 4, ALPHA),

		 

		    new FieldSpec("FM131-FILLER-1", 100, 156, ALPHA)

		);
 
//FM105 — Institutional Header (Record Type 05)

public static final List<FieldSpec> FM105 = List.of(

  new FieldSpec("FM105-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),

  new FieldSpec("FM105-SER-NUM-JULDT-CC", 4, 2, ALPHA),

  new FieldSpec("FM105-SER-NUM-JULDT-YY", 6, 2, ALPHA),

  new FieldSpec("FM105-SER-NUM-JULDT-DDD", 8, 3, ALPHA),

  new FieldSpec("FM105-SER-NUM-SEQUENCE", 11, 5, ALPHA),

  new FieldSpec("FM105-SER-NUM-SUFFIX", 16, 2, ALPHA),

  new FieldSpec("FM105-TRANS-ID", 18, 2, ALPHA),

  new FieldSpec("FM105-TRANS-QUAL", 20, 2, ALPHA),

  new FieldSpec("FM105-REC-TYPE", 22, 2, ALPHA),

  new FieldSpec("FM105-SEQ-NUM", 24, 2, BINARY),

  new FieldSpec("FM105-CLM-TYPE", 26, 2, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-CD", 28, 3, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-STA-CD", 31, 4, ALPHA),

  new FieldSpec("FM105-PROC-SITE-PLAN-CD", 35, 3, ALPHA),

  new FieldSpec("FM105-PROC-SITE-STA-CD", 38, 4, ALPHA),

  new FieldSpec("FM105-CNTL-PLAN-CD", 42, 3, ALPHA),

  new FieldSpec("FM105-TRANSM-MODE-CD", 45, 1, ALPHA),

  new FieldSpec("FM105-PLAN-PAYER-CD", 46, 1, ALPHA),

  new FieldSpec("FM105-PMT-DISP-CD", 47, 1, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-CNTL-NUM", 48, 17, ALPHA),

  new FieldSpec("FM105-PROC-SITE-CNTL-NUM", 65, 17, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-CLM-REF-NUM", 82, 17, ALPHA),

  new FieldSpec("FM105-REF-NUM-LOCAL-PLAN", 99, 3, ALPHA),

  new FieldSpec("FM105-REF-NUM-JULDT-CC", 102, 2, ALPHA),

  new FieldSpec("FM105-REF-NUM-JULDT-YY", 104, 2, ALPHA),

  new FieldSpec("FM105-REF-NUM-JULDT-DDD", 106, 3, ALPHA),

  new FieldSpec("FM105-REF-NUM-SEQUENCE", 109, 5, ALPHA),

  new FieldSpec("FM105-REF-NUM-SUFFIX", 114, 2, ALPHA),

  new FieldSpec("FM105-INP-MED", 116, 1, ALPHA),

  new FieldSpec("FM105-REL-NUM", 117, 3, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-RCPT-DT-CC", 120, 2, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-RCPT-DT-YY", 122, 2, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-RCPT-DT-MM", 124, 2, ALPHA),

  new FieldSpec("FM105-LOC-PLAN-RCPT-DT-DD", 126, 2, ALPHA),

  new FieldSpec("FM105-EDIT-DT-CC", 128, 2, ALPHA),

  new FieldSpec("FM105-EDIT-DT-YY", 130, 2, ALPHA),

  new FieldSpec("FM105-EDIT-DT-MM", 132, 2, ALPHA),

  new FieldSpec("FM105-EDIT-DT-DD", 134, 2, ALPHA),

  new FieldSpec("FM105-PGM-CD", 136, 1, ALPHA),

  new FieldSpec("FM105-ONL-DT-LAST-ACT-CC", 137, 2, ALPHA),

  new FieldSpec("FM105-ONL-DT-LAST-ACT-YY", 139, 2, ALPHA),

  new FieldSpec("FM105-ONL-DT-LAST-ACT-MM", 141, 2, ALPHA),

  new FieldSpec("FM105-ONL-DT-LAST-ACT-DD", 143, 2, ALPHA),

  new FieldSpec("FM105-ONL-TIME-LAST-ACT-HH", 145, 2, ALPHA),

  new FieldSpec("FM105-ONL-TIME-LAST-ACT-MM", 147, 2, ALPHA),

  new FieldSpec("FM105-ONL-TIME-LAST-ACT-SS", 149, 2, ALPHA),

  new FieldSpec("FM105-STAT-CD", 151, 1, ALPHA),

  new FieldSpec("FM105-CLERK-NUM", 152, 4, ALPHA),

  new FieldSpec("FM105-ERR-CD-1", 156, 5, ALPHA),

  new FieldSpec("FM105-ERR-CD-2", 161, 5, ALPHA),

  new FieldSpec("FM105-ERR-CD-3", 166, 5, ALPHA),

  new FieldSpec("FM105-ERR-CD-4", 171, 5, ALPHA),

  new FieldSpec("FM105-ERR-CD-5", 176, 5, ALPHA),

  new FieldSpec("FM105-LINE-BUS", 181, 1, ALPHA),

  new FieldSpec("FM105-SYS-INP-SRC", 182, 2, ALPHA),

  new FieldSpec("FM105-TRANS-IND", 184, 1, ALPHA),

  new FieldSpec("FM105-PLAN-PROF-STND-IND", 185, 1, ALPHA),

  new FieldSpec("FM105-TYPE-SUBM", 186, 1, ALPHA),

  new FieldSpec("FM105-PLAN-PROF-ADJ-EDIT-IND", 187, 1, ALPHA),

  new FieldSpec("FM105-NAT-OOA-CD", 188, 1, ALPHA),

  new FieldSpec("FM105-INVEST-IND", 189, 1, ALPHA),

  new FieldSpec("FM105-RMRK-CD", 190, 3, ALPHA),

  new FieldSpec("FM105-FMT-DB-POST-DT-CC", 193, 2, ALPHA),

  new FieldSpec("FM105-FMT-DB-POST-DT-YY", 195, 2, ALPHA),

  new FieldSpec("FM105-FMT-DB-POST-DT-MM", 197, 2, ALPHA),

  new FieldSpec("FM105-FMT-DB-POST-DT-DD", 199, 2, ALPHA),

  new FieldSpec("FM105-CFA-CD", 201, 1, ALPHA),

  new FieldSpec("FM105-CNTL-PLAN-CFA-ACCT-CD", 202, 2, ALPHA),

  new FieldSpec("FM105-AEA-CD", 204, 1, ALPHA),

  new FieldSpec("FM105-ACCESS-FEE-CD", 205, 1, ALPHA),

  new FieldSpec("FM105-RESUB-DF-IND", 206, 1, ALPHA),

  new FieldSpec("FM105-ADJ-RCPT-DT-CC", 207, 2, ALPHA),

  new FieldSpec("FM105-ADJ-RCPT-DT-YY", 209, 2, ALPHA),

  new FieldSpec("FM105-ADJ-RCPT-DT-MM", 211, 2, ALPHA),

  new FieldSpec("FM105-ADJ-RCPT-DT-DD", 213, 2, ALPHA),

  new FieldSpec("FM105-BCP-PROD-TYPE", 215, 1, ALPHA),

  new FieldSpec("FM105-PLAN-OWNER-IND", 216, 1, ALPHA),

  new FieldSpec("FM105-RECYC-CTR", 217, 1, NUMERIC_TEXT),

  new FieldSpec("FM105-EXCLUDE-PER-USER", 218, 1, ALPHA),

  new FieldSpec("FM105-EXCLUDE-PER-BLUECD", 219, 1, ALPHA),

  new FieldSpec("FM105-INTL-CD", 220, 1, ALPHA),

  new FieldSpec("FM105-ECR-IND", 221, 1, ALPHA),

  new FieldSpec("FM105-ADMIN-EXP-ALLW", 222, 5, PACKED_DECIMAL, 2),

  new FieldSpec("FM105-DEL-MTD", 227, 1, ALPHA),

  new FieldSpec("FM105-UPF-PRC-EDIT-CD", 228, 1, ALPHA),

  new FieldSpec("FM105-POSE-IND", 229, 1, ALPHA),

  new FieldSpec("FM105-ACCT-TYPE-CD", 230, 1, ALPHA),

  new FieldSpec("FM105-EST-IND", 231, 1, ALPHA),

  new FieldSpec("FM105-BOID", 232, 4, ALPHA),

  new FieldSpec("FM105-INIT-TRANSM-MODE-CD", 236, 1, ALPHA),

  new FieldSpec("FM105-SYS-CRT-CD", 237, 1, ALPHA),

  new FieldSpec("FM105-SYS-UPDT-CD", 238, 1, ALPHA),

  new FieldSpec("FM105-ICD-PROV-SUB-VER-IND", 239, 1, ALPHA),

  new FieldSpec("FM105-FMT-CONV-IND", 240, 1, ALPHA),

  new FieldSpec("FM105-PRFX-ACCT-ID-IND", 241, 1, ALPHA),

  new FieldSpec("FM105-FILLER-1", 242, 14, ALPHA));

	// --------------------------------------------------------------------
	// FM133 — Institutional Appended Provider (Record Type 33)
	// --------------------------------------------------------------------
	public static final List<FieldSpec> FM133 = List.of(
			new FieldSpec("FM133-SER-NUM-LOCAL-PLAN",  1,  3, FieldType.ALPHA),

		    new FieldSpec("FM133-SER-NUM-JULDT-CC",    4,  2, FieldType.ALPHA),

		    new FieldSpec("FM133-SER-NUM-JULDT-YY",    6,  2, FieldType.ALPHA),

		    new FieldSpec("FM133-SER-NUM-JULDT-DDD",   8,  3, FieldType.ALPHA),

		    new FieldSpec("FM133-SER-NUM-SEQUENCE",   11,  5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[1]", 26, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[1]",28, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[1]",   33, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #2: 39..51

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[2]", 39, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[2]",41, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[2]",   46, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #3: 52..64

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[3]", 52, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[3]",54, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[3]",   59, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #4: 65..77

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[4]", 65, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[4]",67, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[4]",   72, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #5: 78..90

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[5]", 78, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[5]",80, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[5]",   85, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #6: 91..103

		    new FieldSpec("FM133-CLM-ADJ-PAYA-GP-CD[6]", 91, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-RSN-CD[6]",93, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYA-AMT[6]",   98, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // FILLER-1: 104..129 (26 bytes)

		    new FieldSpec("FM133-FILLER-1",             104, 26, FieldType.ALPHA),

		 

		    // CLM-ADJ-PAYB OCCURS 6 — each occurrence: GP-CD(2), RSN-CD(5), AMT(6 packed, scale=2)

		    // #1: 130..142

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[1]",130, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[1]",132, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[1]",  137, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #2: 143..155

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[2]",143, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[2]",145, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[2]",  150, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #3: 156..168

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[3]",156, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[3]",158, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[3]",  163, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #4: 169..181

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[4]",169, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[4]",171, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[4]",  176, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #5: 182..194

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[5]",182, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[5]",184, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[5]",  189, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // #6: 195..207

		    new FieldSpec("FM133-CLM-ADJ-PAYB-GP-CD[6]",195, 2, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-RSN-CD[6]",197, 5, FieldType.ALPHA),

		    new FieldSpec("FM133-CLM-ADJ-PAYB-AMT[6]",  202, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // Tail filler: 208..255

		    new FieldSpec("FM133-FILLER-2",             208, 48, FieldType.ALPHA),

		    new FieldSpec("FM133-SER-NUM-SUFFIX",     16,  2, FieldType.ALPHA),

		 

		    new FieldSpec("FM133-TRANS-ID",           18,  2, FieldType.ALPHA),

		    new FieldSpec("FM133-TRANS-QUAL",         20,  2, FieldType.ALPHA),

		    new FieldSpec("FM133-REC-TYPE",           22,  2, FieldType.ALPHA));          // "33"


// --------------------------------------------------------------------
	// FM141 — Institutional Appended Provider (Record Type 41)
	// --------------------------------------------------------------------
	public static final List<FieldSpec> FM141 = List.of(
			// ----- KEY (1..25) -----

		    new FieldSpec("FM141-SER-NUM-LOCAL-PLAN",   1,  3, FieldType.ALPHA),

		    new FieldSpec("FM141-SER-NUM-JULDT-CC",     4,  2, FieldType.ALPHA),

		    new FieldSpec("FM141-SER-NUM-JULDT-YY",     6,  2, FieldType.ALPHA),

		    new FieldSpec("FM141-SER-NUM-JULDT-DDD",    8,  3, FieldType.ALPHA),

		    new FieldSpec("FM141-SER-NUM-SEQUENCE",    11,  5, FieldType.ALPHA),

		    new FieldSpec("FM141-SER-NUM-SUFFIX",      16,  2, FieldType.ALPHA),

		 

		    new FieldSpec("FM141-TRANS-ID",            18,  2, FieldType.ALPHA),

		    new FieldSpec("FM141-TRANS-QUAL",          20,  2, FieldType.ALPHA),

		    new FieldSpec("FM141-REC-TYPE",            22,  2, FieldType.ALPHA),         // "41"

		    new FieldSpec("FM141-SEQ-NUM",             24,  2, FieldType.BINARY), // S9(4) COMP

		 

		    // ----- DATA SECTION -----

		    // These depend on actual FM141 copybook definitions.

		    // Filling based on typical ITCFM14x structure (VAL-CD and VAL-AMT arrays).

		 

		    // --- VAL-CD OCCURS 12 (each 2 bytes) -> 26..49 ---

		    new FieldSpec("FM141-VAL-CD[1]",   26, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[2]",   28, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[3]",   30, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[4]",   32, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[5]",   34, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[6]",   36, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[7]",   38, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[8]",   40, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[9]",   42, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[10]",  44, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[11]",  46, 2, FieldType.ALPHA),

		    new FieldSpec("FM141-VAL-CD[12]",  48, 2, FieldType.ALPHA),

		 

		    // --- VAL-AMT OCCURS 12 (each 6 bytes, packed) -> 50..121 ---

		    new FieldSpec("FM141-VAL-AMT[1]",   50, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[2]",   56, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[3]",   62, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[4]",   68, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[5]",   74, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[6]",   80, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[7]",   86, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[8]",   92, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[9]",   98, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[10]", 104, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[11]", 110, 6, FieldType.PACKED_DECIMAL, 2),

		    new FieldSpec("FM141-VAL-AMT[12]", 116, 6, FieldType.PACKED_DECIMAL, 2),

		 

		    // Tail filler — rest of 255 bytes

		    new FieldSpec("FM141-FILLER-1",    122, 134, FieldType.ALPHA)  // 122..255
			);          // "41"
	
	// FM1A5 — Professional Header (Record Type A5)
    public static final List<FieldSpec> FM1A5 = List.of(
        // KEY (1–25)
        new FieldSpec("FM1A5-SER-NUM-LOCAL-PLAN",        1,   3,  ALPHA),
        new FieldSpec("FM1A5-SER-NUM-JULDT-CC",          4,   2,  ALPHA),
        new FieldSpec("FM1A5-SER-NUM-JULDT-YY",          6,   2,  ALPHA),
        new FieldSpec("FM1A5-SER-NUM-JULDT-DDD",         8,   3,  ALPHA),
        new FieldSpec("FM1A5-SER-NUM-SEQUENCE",         11,   5,  ALPHA),
        new FieldSpec("FM1A5-SER-NUM-SUFFIX",           16,   2,  ALPHA),
        new FieldSpec("FM1A5-TRANS-ID",                 18,   2,  ALPHA),
        new FieldSpec("FM1A5-TRANS-QUAL",               20,   2,  ALPHA),
        new FieldSpec("FM1A5-REC-TYPE",                 22,   2,  ALPHA),  // 'A5'
        new FieldSpec("FM1A5-SEQ-NUM",                  24,   2,  BINARY),

        // DATA (26–255)
        new FieldSpec("FM1A5-CLM-TYPE",                  26,   2,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-CD",               28,   3,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-STA-CD",           31,   4,  ALPHA),
        new FieldSpec("FM1A5-PROC-SITE-PLAN-CD",         35,   3,  ALPHA),
        new FieldSpec("FM1A5-PROC-SITE-STA-CD",          38,   4,  ALPHA),
        new FieldSpec("FM1A5-CNTL-PLAN-CD",              42,   3,  ALPHA),
        new FieldSpec("FM1A5-TRANSM-MODE-CD",            45,   1,  ALPHA),
        new FieldSpec("FM1A5-PLAN-PAYER-CD",             46,   1,  ALPHA),
        new FieldSpec("FM1A5-PMT-DISP-CD",               47,   1,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-CNTL-NUM",         48,  17,  ALPHA),
        new FieldSpec("FM1A5-PROC-SITE-CNTL-NUM",        65,  17,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-CLM-REF-NUM",      82,  17,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-LOCAL-PLAN",        99,   3,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-JULDT-CC",         102,   2,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-JULDT-YY",         104,   2,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-JULDT-DDD",        106,   3,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-SEQUENCE",         109,   5,  ALPHA),
        new FieldSpec("FM1A5-REF-NUM-SUFFIX",           114,   2,  ALPHA),
        new FieldSpec("FM1A5-INP-MED",                  116,   1,  ALPHA),
        new FieldSpec("FM1A5-REL-NUM",                  117,   3,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-RCPT-DT-CC",      120,   2,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-RCPT-DT-YY",      122,   2,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-RCPT-DT-MM",      124,   2,  ALPHA),
        new FieldSpec("FM1A5-LOC-PLAN-RCPT-DT-DD",      126,   2,  ALPHA),
        new FieldSpec("FM1A5-EDIT-DT-CC",               128,   2,  ALPHA),
        new FieldSpec("FM1A5-EDIT-DT-YY",               130,   2,  ALPHA),
        new FieldSpec("FM1A5-EDIT-DT-MM",               132,   2,  ALPHA),
        new FieldSpec("FM1A5-EDIT-DT-DD",               134,   2,  ALPHA),
        new FieldSpec("FM1A5-PGM-CD",                   136,   1,  ALPHA),
        new FieldSpec("FM1A5-ONL-DT-LAST-ACT-CC",       137,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-DT-LAST-ACT-YY",       139,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-DT-LAST-ACT-MM",       141,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-DT-LAST-ACT-DD",       143,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-TIME-LAST-ACT-HH",     145,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-TIME-LAST-ACT-MM",     147,   2,  ALPHA),
        new FieldSpec("FM1A5-ONL-TIME-LAST-ACT-SS",     149,   2,  ALPHA),
        new FieldSpec("FM1A5-STAT-CD",                  151,   1,  ALPHA),
        new FieldSpec("FM1A5-CLERK-NUM",                152,   4,  ALPHA),
        new FieldSpec("FM1A5-ERR-CD-1",                 156,   5,  ALPHA),
        new FieldSpec("FM1A5-ERR-CD-2",                 161,   5,  ALPHA),
        new FieldSpec("FM1A5-ERR-CD-3",                 166,   5,  ALPHA),
        new FieldSpec("FM1A5-ERR-CD-4",                 171,   5,  ALPHA),
        new FieldSpec("FM1A5-ERR-CD-5",                 176,   5,  ALPHA),
        new FieldSpec("FM1A5-LINE-BUS",                 181,   1,  ALPHA),
        new FieldSpec("FM1A5-SYS-INP-SRC",              182,   2,  ALPHA),
        new FieldSpec("FM1A5-TRANS-IND",                184,   1,  ALPHA),
        new FieldSpec("FM1A5-PLAN-PROF-STND-IND",       185,   1,  ALPHA),
        new FieldSpec("FM1A5-TYPE-SUBM",                186,   1,  ALPHA),
        new FieldSpec("FM1A5-PLAN-PROF-ADJ-EDIT-IND",   187,   1,  ALPHA),
        new FieldSpec("FM1A5-NAT-OOA-CD",               188,   1,  ALPHA),
        new FieldSpec("FM1A5-INVEST-IND",               189,   1,  ALPHA),
        new FieldSpec("FM1A5-RMRK-CD",                  190,   3,  ALPHA),
        new FieldSpec("FM1A5-FMT-DB-POST-DT-CC",        193,   2,  ALPHA),
        new FieldSpec("FM1A5-FMT-DB-POST-DT-YY",        195,   2,  ALPHA),
        new FieldSpec("FM1A5-FMT-DB-POST-DT-MM",        197,   2,  ALPHA),
        new FieldSpec("FM1A5-FMT-DB-POST-DT-DD",        199,   2,  ALPHA),
        new FieldSpec("FM1A5-CFA-CD",                   201,   1,  ALPHA),
        new FieldSpec("FM1A5-CNTL-PLAN-CFA-ACCT-CD",    202,   2,  ALPHA),
        new FieldSpec("FM1A5-AEA-CD",                   204,   1,  ALPHA),
        new FieldSpec("FM1A5-ACCESS-FEE-CD",            205,   1,  ALPHA),
        new FieldSpec("FM1A5-RESUB-DF-IND",             206,   1,  ALPHA),
        new FieldSpec("FM1A5-ADJ-RCPT-DT-CC",           207,   2,  ALPHA),
        new FieldSpec("FM1A5-ADJ-RCPT-DT-YY",           209,   2,  ALPHA),
        new FieldSpec("FM1A5-ADJ-RCPT-DT-MM",           211,   2,  ALPHA),
        new FieldSpec("FM1A5-ADJ-RCPT-DT-DD",           213,   2,  ALPHA),
        new FieldSpec("FM1A5-BCP-PROD-TYPE",            215,   1,  ALPHA),
        new FieldSpec("FM1A5-PLAN-OWNER-IND",           216,   1,  ALPHA),
        new FieldSpec("FM1A5-RECYC-CTR",                217,   1,  NUMERIC_TEXT),
        new FieldSpec("FM1A5-EXCLUDE-PER-USER",         218,   1,  ALPHA),
        new FieldSpec("FM1A5-EXCLUDE-PER-BLUECD",       219,   1,  ALPHA),
        new FieldSpec("FM1A5-INTL-CD",                  220,   1,  ALPHA),
        new FieldSpec("FM1A5-ECR-IND",                  221,   1,  ALPHA),
        new FieldSpec("FM1A5-ADMIN-EXP-ALLW",           222,   5,  PACKED_DECIMAL, 2),
        new FieldSpec("FM1A5-DEL-MTD",                  227,   1,  ALPHA),
        new FieldSpec("FM1A5-UPF-PRC-EDIT-CD",          228,   1,  ALPHA),
        new FieldSpec("FM1A5-835-IND",                  229,   1,  ALPHA),
        new FieldSpec("FM1A5-ACCT-TYPE-CD",             230,   1,  ALPHA),
        new FieldSpec("FM1A5-EST-IND",                  231,   1,  ALPHA),
        new FieldSpec("FM1A5-BOID",                     232,   4,  ALPHA),
        new FieldSpec("FM1A5-INIT-TRANSM-MODE-CD",      236,   1,  ALPHA),
        new FieldSpec("FM1A5-SYS-CRT-CD",               237,   1,  ALPHA),
        new FieldSpec("FM1A5-SYS-UPDT-CD",              238,   1,  ALPHA),
        new FieldSpec("FM1A5-ICD-PROV-SUB-VER-IND",     239,   1,  ALPHA),
        new FieldSpec("FM1A5-FMT-CONV-IND",             240,   1,  ALPHA),
        new FieldSpec("FM1A5-PRFX-ACCT-ID-IND",         241,   1,  ALPHA),
        new FieldSpec("FM1A5-FILLER-1",                 242,  14,  ALPHA)
    );

    // FM1B0 — Professional Provider (Record Type B0)
    public static final List<FieldSpec> FM1B0 = List.of(
        // KEY
        new FieldSpec("FM1B0-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1B0-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1B0-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1B0-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1B0-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1B0-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1B0-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1B0-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1B0-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1B0-SEQ-NUM",                  24,   2, BINARY),
        // DATA
        new FieldSpec("FM1B0-TYPE-BILL",                26,   3, ALPHA),
        new FieldSpec("FM1B0-FED-TAX-ID-NUM",           29,   9, ALPHA),
        new FieldSpec("FM1B0-TYPE-FED-TAX-ID-NUM",      38,   1, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-LAST-NAME",      39,  20, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-FIRST-NAME",     59,  10, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-MID-INIT",       69,   1, ALPHA),
        new FieldSpec("FM1B0-CLM-SUB-ORG-NAME",         70,  31, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-ADDR-LN1",      101,  25, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-ADDR-LN2",      126,  25, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-CITY",          151,  15, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-ST",            166,   2, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-ZIP-CD5",       168,   5, ALPHA),
        new FieldSpec("FM1B0-BILL-PROV-ZIP-CD4",       173,   4, ALPHA),
        new FieldSpec("FM1B0-BCBS-PROV-NUM",           177,  13, ALPHA),
        new FieldSpec("FM1B0-BILL-NAME-IND",           190,   1, ALPHA),
        new FieldSpec("FM1B0-PROV-CNTRY-CD",           191,   4, ALPHA),
        new FieldSpec("FM1B0-PERF-PROV-TXNMY-CD-CLM",  195,  15, ALPHA),
        new FieldSpec("FM1B0-PROV-NPI",                210,  10, ALPHA),
        new FieldSpec("FM1B0-CUST-PRIMY-NTWK",         220,   4, ALPHA),
        new FieldSpec("FM1B0-CUST-SCNDY-NTWK",         224,   4, ALPHA),
        new FieldSpec("FM1B0-PERF-PROV-IHS-CLM-IND",   228,   1, ALPHA),
        new FieldSpec("FM1B0-MKT-ID-CLM",              229,   4, ALPHA),
        new FieldSpec("FM1B0-TIER-DESIG-IND-CLM",      233,   1, ALPHA),
        new FieldSpec("FM1B0-FILLER-1",                234,  22, ALPHA)
    );

    // FM1B5 — Professional Appended Provider (Record Type B5)
    public static final List<FieldSpec> FM1B5 = List.of(
        // KEY
        new FieldSpec("FM1B5-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1B5-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1B5-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1B5-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1B5-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1B5-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1B5-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1B5-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1B5-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1B5-SEQ-NUM",                  24,   2, BINARY),
        // DATA
        new FieldSpec("FM1B5-PMT-RSTRCT-IND",           26,   1, ALPHA),
        new FieldSpec("FM1B5-AMB-PICK-UP-ADDR",         27,  25, ALPHA),
        new FieldSpec("FM1B5-AMB-PICK-UP-CITY",         52,  15, ALPHA),
        new FieldSpec("FM1B5-AMB-PICK-UP-ST",           67,   2, ALPHA),
        new FieldSpec("FM1B5-AMB-PICK-UP-ZIP",          69,   9, ALPHA),
        new FieldSpec("FM1B5-FILLER-1",                 78,  54, ALPHA),
        new FieldSpec("FM1B5-SERV-FAC-LOC-ST-CLM",     132,   2, ALPHA),
        new FieldSpec("FM1B5-SERV-FAC-LOC-ZIP5-CLM",   134,   5, ALPHA),
        new FieldSpec("FM1B5-SERV-FAC-LOC-ZIP4-CLM",   139,   4, ALPHA),
        new FieldSpec("FM1B5-SERV-FAC-LOC-NUM-CLM",    143,  13, ALPHA),
        new FieldSpec("FM1B5-SERV-LOC-NUM-QUAL-CLM",   156,   2, ALPHA),
        new FieldSpec("FM1B5-AMB-DROP-OFF-LOC-NAME",   158,  35, ALPHA),
        new FieldSpec("FM1B5-AMB-DROP-OFF-ADDR",       193,  25, ALPHA),
        new FieldSpec("FM1B5-AMB-DROP-OFF-CITY",       218,  15, ALPHA),
        new FieldSpec("FM1B5-AMB-DROP-OFF-ST",         233,   2, ALPHA),
        new FieldSpec("FM1B5-AMB-DROP-OFF-ZIP",        235,   9, ALPHA),
        new FieldSpec("FM1B5-FILLER-2",                244,  12, ALPHA)
    );

    // FM1C0 — Professional Patient (Record Type C0)
    public static final List<FieldSpec> FM1C0 = List.of(
        // KEY
        new FieldSpec("FM1C0-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1C0-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1C0-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1C0-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1C0-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1C0-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1C0-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1C0-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1C0-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1C0-SEQ-NUM",                  24,   2, BINARY),
        // DATA
        new FieldSpec("FM1C0-PAT-LAST-NAME",            26,  20, ALPHA),
        new FieldSpec("FM1C0-PAT-FIRST-NAME",           46,  10, ALPHA),
        new FieldSpec("FM1C0-PAT-MID-INIT",             56,   1, ALPHA),
        new FieldSpec("FM1C0-PAT-SEX",                  57,   1, ALPHA),
        new FieldSpec("FM1C0-PAT-BRT-DT-CC",            58,   2, ALPHA),
        new FieldSpec("FM1C0-PAT-BRT-DT-YY",            60,   2, ALPHA),
        new FieldSpec("FM1C0-PAT-BRT-DT-MM",            62,   2, ALPHA),
        new FieldSpec("FM1C0-PAT-BRT-DT-DD",            64,   2, ALPHA),
        new FieldSpec("FM1C0-PAT-MAR-STAT-CD",          66,   1, ALPHA),
        new FieldSpec("FM1C0-PAT-ADDR-LN-1",            67,  25, ALPHA),
        new FieldSpec("FM1C0-PAT-ADDR-LN-2",            92,  25, ALPHA),
        new FieldSpec("FM1C0-PAT-CITY",                117,  15, ALPHA),
        new FieldSpec("FM1C0-PAT-ST",                  132,   2, ALPHA),
        new FieldSpec("FM1C0-PAT-ZIP-CD5",             134,   5, ALPHA),
        new FieldSpec("FM1C0-PAT-ZIP-CD4",             139,   4, ALPHA),
        new FieldSpec("FM1C0-ADM-DT-CC",               143,   2, ALPHA),
        new FieldSpec("FM1C0-ADM-DT-YY",               145,   2, ALPHA),
        new FieldSpec("FM1C0-ADM-DT-MM",               147,   2, ALPHA),
        new FieldSpec("FM1C0-ADM-DT-DD",               149,   2, ALPHA),
        new FieldSpec("FM1C0-DISCHRG-DT-CC",           151,   2, ALPHA),
        new FieldSpec("FM1C0-DISCHRG-DT-YY",           153,   2, ALPHA),
        new FieldSpec("FM1C0-DISCHRG-DT-MM",           155,   2, ALPHA),
        new FieldSpec("FM1C0-DISCHRG-DT-DD",           157,   2, ALPHA),
        new FieldSpec("FM1C0-DT-ACC-ONSET-ILL-CC",     159,   2, ALPHA),
        new FieldSpec("FM1C0-DT-ACC-ONSET-ILL-YY",     161,   2, ALPHA),
        new FieldSpec("FM1C0-DT-ACC-ONSET-ILL-MM",     163,   2, ALPHA),
        new FieldSpec("FM1C0-DT-ACC-ONSET-ILL-DD",     165,   2, ALPHA),
        new FieldSpec("FM1C0-WORK-COMP-IND",           167,   1, ALPHA),
        new FieldSpec("FM1C0-AUTO-OTH-ACC-CD",         168,   1, ALPHA),
        new FieldSpec("FM1C0-EMER-CD",                 169,   1, ALPHA),
        new FieldSpec("FM1C0-OTH-CARR-IND",            170,   1, ALPHA),
        new FieldSpec("FM1C0-SERV-ELIG-CD-CLM-1",      171,   1, ALPHA),
        new FieldSpec("FM1C0-SERV-ELIG-CD-CLM-2",      172,   1, ALPHA),
        new FieldSpec("FM1C0-SERV-ELIG-CD-CLM-3",      173,   1, ALPHA),
        new FieldSpec("FM1C0-SERV-ELIG-CD-CLM-4",      174,   1, ALPHA),
        new FieldSpec("FM1C0-FILLER-1",                175,  81, ALPHA)
    );

    // FM1D0 — Professional Subscriber (Record Type D0)
    public static final List<FieldSpec> FM1D0 = List.of(
        new FieldSpec("FM1D0-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1D0-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1D0-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1D0-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1D0-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1D0-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1D0-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1D0-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1D0-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1D0-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1D0-PAT-REL-SUB",              26,   2, ALPHA),
        new FieldSpec("FM1D0-SUB-LAST-NAME",            28,  20, ALPHA),
        new FieldSpec("FM1D0-SUB-FIRST-NAME",           48,  10, ALPHA),
        new FieldSpec("FM1D0-SUB-MID-INIT",             58,   1, ALPHA),
        new FieldSpec("FM1D0-SUB-BRT-DT-CC",            59,   2, ALPHA),
        new FieldSpec("FM1D0-SUB-BRT-DT-YY",            61,   2, ALPHA),
        new FieldSpec("FM1D0-SUB-BRT-DT-MM",            63,   2, ALPHA),
        new FieldSpec("FM1D0-SUB-BRT-DT-DD",            65,   2, ALPHA),
        new FieldSpec("FM1D0-SUB-ID-PFX-1-3",           67,   3, ALPHA),
        new FieldSpec("FM1D0-SUB-ID-SUFX-4-17",         70,  14, ALPHA),
        new FieldSpec("FM1D0-SUB-ADDR-LN-1",            84,  25, ALPHA),
        new FieldSpec("FM1D0-SUB-ADDR-LN-2",           109,  25, ALPHA),
        new FieldSpec("FM1D0-SUB-CITY",                134,  15, ALPHA),
        new FieldSpec("FM1D0-SUB-ZIP-CD5",             149,   5, ALPHA),
        new FieldSpec("FM1D0-SUB-ZIP-CD4",             154,   4, ALPHA),
        new FieldSpec("FM1D0-SUB-GRP-NUM",             158,   9, ALPHA),
        new FieldSpec("FM1D0-SEC-PAYOR-PRC-QUAL",      167,   1, ALPHA),
        new FieldSpec("FM1D0-REL-INFO-CD",             168,   1, ALPHA),
        new FieldSpec("FM1D0-ASGNMT-BNFT-IND",         169,   1, ALPHA),
        new FieldSpec("FM1D0-MPPQ-CD-ASGN",            170,   1, ALPHA),
        new FieldSpec("FM1D0-MPPQ-CD-UNASGN",          171,   1, ALPHA),
        new FieldSpec("FM1D0-COND-CD-1",               172,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-2",               174,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-3",               176,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-4",               178,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-5",               180,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-6",               182,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-7",               184,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-8",               186,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-9",               188,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-10",              190,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-11",              192,   2, ALPHA),
        new FieldSpec("FM1D0-COND-CD-12",              194,   2, ALPHA),
        new FieldSpec("FM1D0-SF-INFO-ONLY-MSG-CD-1",   196,   4, ALPHA),
        new FieldSpec("FM1D0-SF-INFO-ONLY-MSG-CD-2",   200,   4, ALPHA),
        new FieldSpec("FM1D0-SF-INFO-ONLY-MSG-CD-3",   204,   4, ALPHA),
        new FieldSpec("FM1D0-SF-INFO-ONLY-MSG-CD-4",   208,   4, ALPHA),
        new FieldSpec("FM1D0-SF-INFO-ONLY-MSG-CD-5",   212,   4, ALPHA),
        new FieldSpec("FM1D0-MED-ASGNMT-IND",          216,   1, ALPHA),
        new FieldSpec("FM1D0-FILLER-1",                217,  39, ALPHA)
    );

    // FM1D1 — Professional Additional Subscriber (Record Type D1)
    public static final List<FieldSpec> FM1D1 = List.of(
        new FieldSpec("FM1D1-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1D1-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1D1-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1D1-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1D1-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1D1-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1D1-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1D1-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1D1-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1D1-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1D1-PAT-REL-OTH-INS",          26,   2, ALPHA),
        new FieldSpec("FM1D1-OTH-INS-LAST-NAME",        28,  20, ALPHA),
        new FieldSpec("FM1D1-OTH-INS-ID-NUM",           48,  17, ALPHA),
        new FieldSpec("FM1D1-OTH-PAYER-NAME",           65,  16, ALPHA),
        new FieldSpec("FM1D1-CLM-OTH-CARR-FNCL-CD",     81,   1, ALPHA),
        new FieldSpec("FM1D1-SRC-PMT-CD",               82,   2, ALPHA),
        new FieldSpec("FM1D1-CNTL-PLAN-CD",             84,   3, ALPHA),
        new FieldSpec("FM1D1-MED-CLM-NUM",              87,  12, ALPHA),
        new FieldSpec("FM1D1-MED-CLM-REJ-CD-CLM",       99,   5, ALPHA),
        new FieldSpec("FM1D1-FILLER-1",                104, 152, ALPHA)
    );

    // FM1D2 — Professional OPL Claim Level (Record Type D2)
    public static final List<FieldSpec> FM1D2 = List.of(
        new FieldSpec("FM1D2-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1D2-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1D2-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1D2-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1D2-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1D2-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1D2-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1D2-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1D2-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1D2-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-1",         26,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-2",         28,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-3",         30,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-4",         32,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-5",         34,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-6",         36,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-7",         38,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-8",         40,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-9",         42,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-10",        44,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-11",        46,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-12",        48,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-13",        50,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-14",        52,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-15",        54,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-CD-CLM-16",        56,   2, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-1",      58,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-2",      64,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-3",      70,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-4",      76,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-5",      82,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-6",      88,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-7",      94,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-8",     100,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-9",     106,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-10",    112,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-11",    118,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-12",    124,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-13",    130,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-14",    136,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-15",    142,   6, ALPHA),
        new FieldSpec("FM1D2-OPL-VAL-AMT-CLM-X-16",    148,   6, ALPHA),
        new FieldSpec("FM1D2-FILLER-1",                154, 102, ALPHA)
    );

    // FM1D3 — Professional CAS Claim Level (Record Type D3)
    public static final List<FieldSpec> FM1D3 = List.of(
        new FieldSpec("FM1D3-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1D3-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1D3-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1D3-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1D3-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1D3-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1D3-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1D3-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1D3-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1D3-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-1", 26,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-1",28,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-1",   33,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-2", 39,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-2",41,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-2",   46,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-3", 52,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-3",54,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-3",   59,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-4", 65,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-4",67,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-4",   72,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-5", 78,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-5",80,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-5",   85,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYA-6", 91,   2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYA-6",93,   5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYA-6",   98,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-FILLER-1",                 104, 26, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-1", 130,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-1",132,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-1",   137,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-2", 143,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-2",145,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-2",   150,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-3", 156,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-3",158,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-3",   163,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-4", 169,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-4",171,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-4",   176,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-5", 182,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-5",184,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-5",   189,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-CLM-ADJ-GP-CD-CLM-PAYB-6", 195,  2, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-RSN-CD-CLM-PAYB-6",197,  5, ALPHA),
        new FieldSpec("FM1D3-CLM-ADJ-AMT-CLM-PAYB-6",   202,  6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1D3-FILLER-2",                 208, 48, ALPHA)
    );

    // FM1E0 — Professional Claim (Record Type E0)
    public static final List<FieldSpec> FM1E0 = List.of(
        new FieldSpec("FM1E0-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1E0-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1E0-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1E0-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1E0-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1E0-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1E0-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1E0-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1E0-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1E0-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1E0-PAT-CNTL-NUM",             26,  20, ALPHA),
        new FieldSpec("FM1E0-SERV-FACIL-NAME",          46,  31, ALPHA),
        new FieldSpec("FM1E0-SERV-FACIL-NUM",           77,  13, ALPHA),
        new FieldSpec("FM1E0-TOT-CHRG",                 90,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E0-PAT-PAID-AMT",             96,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E0-UNABLE-WORK-FROM-DT-CC",  102,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-FROM-DT-YY",  104,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-FROM-DT-MM",  106,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-FROM-DT-DD",  108,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-TO-DT-CC",    110,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-TO-DT-YY",    112,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-TO-DT-MM",    114,   2, ALPHA),
        new FieldSpec("FM1E0-UNABLE-WORK-TO-DT-DD",    116,   2, ALPHA),
        new FieldSpec("FM1E0-BNFT-MGT-TRTMT-AUTH-NUM", 118,  25, ALPHA),
        new FieldSpec("FM1E0-SIGN-FILE-IND",           143,   1, ALPHA),
        new FieldSpec("FM1E0-ACCESS-FEE-PCT",          144,   3, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E0-POS-IND",                 147,   1, ALPHA),
        new FieldSpec("FM1E0-ATTACH-IND",              148,   1, ALPHA),
        new FieldSpec("FM1E0-SPEC-PGM-CD",             149,   2, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-NUM-CLM",       151,  13, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-NAME-CLM",      164,  31, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-OFC-ZIP-CD5",   195,   5, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-OFC-ZIP-CD4",   200,   4, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-SPEC-CLM",      204,   2, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-TYPE-CLM",      206,   2, ALPHA),
        new FieldSpec("FM1E0-CLASS-PROV-CLM",          208,   1, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-POS-IND-CLM",   209,   1, ALPHA),
        new FieldSpec("FM1E0-PPO-AVBL",                210,   1, ALPHA),
        new FieldSpec("FM1E0-INDV-CASE-MGMT-STAT",     211,   1, ALPHA),
        new FieldSpec("FM1E0-HOST-OPL-PROV-ARRNG-CD",  212,   1, ALPHA),
        new FieldSpec("FM1E0-TOOTH-NUM-CLM",           213,   2, ALPHA),
        new FieldSpec("FM1E0-TOOTH-STAT",              215,   2, ALPHA),
        new FieldSpec("FM1E0-PERF-PROV-NPI-CLM",       217,  10, ALPHA),
        new FieldSpec("FM1E0-FILLER-1",                227,  29, ALPHA)
    );

    // FM1E1 — Professional Claim Pricing (Record Type E1)
    public static final List<FieldSpec> FM1E1 = List.of(
        new FieldSpec("FM1E1-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1E1-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1E1-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1E1-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1E1-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1E1-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1E1-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1E1-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1E1-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1E1-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1E1-SF-MSG-CD-CLM-1",         26,   4, ALPHA),
        new FieldSpec("FM1E1-SF-MSG-CD-CLM-2",         30,   4, ALPHA),
        new FieldSpec("FM1E1-SF-MSG-CD-CLM-3",         34,   4, ALPHA),
        new FieldSpec("FM1E1-SF-MSG-CD-CLM-4",         38,   4, ALPHA),
        new FieldSpec("FM1E1-SF-MSG-CD-CLM-5",         42,   4, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-CD-CLM-1",   46,   3, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-CD-CLM-2",   49,   3, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-CD-CLM-3",   52,   3, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-CD-CLM-4",   55,   3, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-CD-CLM-5",   58,   3, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-AMT-CLM-1",  61,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E1-SPEC-PRC-COND-AMT-CLM-2",  67,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E1-SPEC-PRC-COND-AMT-CLM-3",  73,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E1-SPEC-PRC-COND-AMT-CLM-4",  79,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E1-SPEC-PRC-COND-AMT-CLM-5",  85,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1E1-SPEC-PRC-COND-PCT-CLM-1",  91,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1E1-SPEC-PRC-COND-PCT-CLM-2",  96,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1E1-SPEC-PRC-COND-PCT-CLM-3", 101,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1E1-SPEC-PRC-COND-PCT-CLM-4", 106,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1E1-SPEC-PRC-COND-PCT-CLM-5", 111,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1E1-PREDETRMN-BNFT-ID-CLM",   116,  30, ALPHA),
        new FieldSpec("FM1E1-SPEC-PRC-COND-DAYS-1",    146,   3, NUMERIC_TEXT),
        new FieldSpec("FM1E1-SPEC-PRC-COND-DAYS-2",    149,   3, NUMERIC_TEXT),
        new FieldSpec("FM1E1-SPEC-PRC-COND-DAYS-3",    152,   3, NUMERIC_TEXT),
        new FieldSpec("FM1E1-SPEC-PRC-COND-DAYS-4",    155,   3, NUMERIC_TEXT),
        new FieldSpec("FM1E1-SPEC-PRC-COND-DAYS-5",    158,   3, NUMERIC_TEXT),
        new FieldSpec("FM1E1-FILLER-2",                161,  95, ALPHA)
    );

    // FM1E2 — Professional Claim Detail (Record Type E2)
    public static final List<FieldSpec> FM1E2 = List.of(
        new FieldSpec("FM1E2-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1E2-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1E2-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1E2-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1E2-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1E2-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1E2-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1E2-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1E2-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1E2-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1E2-ICD-DIAG-CD-1",            26,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-2",            33,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-3",            40,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-4",            47,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-5",            54,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-6",            61,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-7",            68,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-8",            75,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-9",            82,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-10",           89,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-11",           96,   7, ALPHA),
        new FieldSpec("FM1E2-ICD-DIAG-CD-12",          103,   7, ALPHA),
        new FieldSpec("FM1E2-ANES-PROC-CD-1",          110,   5, ALPHA),
        new FieldSpec("FM1E2-ANES-PROC-CD-2",          115,   5, ALPHA),
        new FieldSpec("FM1E2-FILLER-1",                120, 136, ALPHA)
    );

    // FM1E6 — Professional Special Notations (Record Type E6)
    public static final List<FieldSpec> FM1E6 = List.of(
        new FieldSpec("FM1E6-SER-NUM-LOCAL-PLAN",        1,   3, ALPHA),
        new FieldSpec("FM1E6-SER-NUM-JULDT-CC",          4,   2, ALPHA),
        new FieldSpec("FM1E6-SER-NUM-JULDT-YY",          6,   2, ALPHA),
        new FieldSpec("FM1E6-SER-NUM-JULDT-DDD",         8,   3, ALPHA),
        new FieldSpec("FM1E6-SER-NUM-SEQUENCE",         11,   5, ALPHA),
        new FieldSpec("FM1E6-SER-NUM-SUFFIX",           16,   2, ALPHA),
        new FieldSpec("FM1E6-TRANS-ID",                 18,   2, ALPHA),
        new FieldSpec("FM1E6-TRANS-QUAL",               20,   2, ALPHA),
        new FieldSpec("FM1E6-REC-TYPE",                 22,   2, ALPHA),
        new FieldSpec("FM1E6-SEQ-NUM",                  24,   2, BINARY),
        new FieldSpec("FM1E6-SPEC-NOTE-TRNSM-IND",      26,   1, ALPHA),
        new FieldSpec("FM1E6-SPEC-NOTE-CD",             27,   5, ALPHA),
        new FieldSpec("FM1E6-SPEC-NOTE-DATA",           32, 215, ALPHA),
        new FieldSpec("FM1E6-FILLER-1",                247,   9, ALPHA)
    );

    // FM1F0 — Professional Line-of-Service (Record Type F0)
    public static final List<FieldSpec> FM1F0 = List.of(
        new FieldSpec("FM1F0-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1F0-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1F0-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1F0-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1F0-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1F0-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1F0-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1F0-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1F0-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1F0-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1F0-DT-SERV-START-CC",        26,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-START-YY",        28,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-START-MM",        30,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-START-DD",        32,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-END-CC",          34,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-END-YY",          36,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-END-MM",          38,   2, ALPHA),
        new FieldSpec("FM1F0-DT-SERV-END-DD",          40,   2, ALPHA),
        new FieldSpec("FM1F0-PLACE-SERV",              42,   2, ALPHA),
        new FieldSpec("FM1F0-TYPE-SERV",               44,   3, ALPHA),
        new FieldSpec("FM1F0-HCPCS-PROC-CD",           47,   5, ALPHA),
        new FieldSpec("FM1F0-HCPCS-PROC-CD-MOD-1",     52,   2, ALPHA),
        new FieldSpec("FM1F0-HCPCS-PROC-CD-MOD-2",     54,   2, ALPHA),
        new FieldSpec("FM1F0-ICD-DIAG-CD-IND-1",       56,   2, ALPHA),
        new FieldSpec("FM1F0-ICD-DIAG-CD-IND-2",       58,   2, ALPHA),
        new FieldSpec("FM1F0-ICD-DIAG-CD-IND-3",       60,   2, ALPHA),
        new FieldSpec("FM1F0-ICD-DIAG-CD-IND-4",       62,   2, ALPHA),
        new FieldSpec("FM1F0-NUM-SERV",                64,   4, NUMERIC_TEXT),
        new FieldSpec("FM1F0-SERV-HH",                 68,   2, NUMERIC_TEXT),
        new FieldSpec("FM1F0-SERV-MM",                 70,   2, NUMERIC_TEXT),
        new FieldSpec("FM1F0-SERV-CHRG",               72,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F0-PERF-PROV-NUM-LN",        78,  13, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-LAST-NAME",     91,  20, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-FIRST-NAME",   111,  10, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-MID-NAME",     121,   1, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-OFC-ZIP-CD5",  122,   5, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-OFC-ZIP-CD4",  127,   4, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-SPEC-LN",      131,   2, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-TYPE-LN",      133,   2, ALPHA),
        new FieldSpec("FM1F0-ADDL-ANES-OTH-COND-CD",  135,   1, ALPHA),
        new FieldSpec("FM1F0-TOOTH-CD-AREA-1",        136,   2, ALPHA),
        new FieldSpec("FM1F0-TOOTH-CD-AREA-2",        138,   2, ALPHA),
        new FieldSpec("FM1F0-TOOTH-CD-AREA-3",        140,   2, ALPHA),
        new FieldSpec("FM1F0-TOOTH-CD-AREA-4",        142,   2, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-POS-IND-LN",   144,   1, ALPHA),
        new FieldSpec("FM1F0-CLASS-PROV-LN",          145,   1, ALPHA),
        new FieldSpec("FM1F0-SERV-FAC-LOC-ZIP5-LN",   146,   5, ALPHA),
        new FieldSpec("FM1F0-SERV-FAC-LOC-ZIP4-LN",   151,   4, ALPHA),
        new FieldSpec("FM1F0-SERV-FAC-LOC-NUM-LN",    155,  13, ALPHA),
        new FieldSpec("FM1F0-SERV-LOC-NUM-QUAL-LN",   168,   2, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-TXNMY-CD-LN",  170,  15, ALPHA),
        new FieldSpec("FM1F0-SERV-FAC-LOC-ST-LN",     185,   2, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-NPI-LN",       187,  10, ALPHA),
        new FieldSpec("FM1F0-HCPCS-PROC-CD-MOD-3",    197,   2, ALPHA),
        new FieldSpec("FM1F0-HCPCS-PROC-CD-MOD-4",    199,   2, ALPHA),
        new FieldSpec("FM1F0-NDC-CD",                 201,  13, ALPHA),
        new FieldSpec("FM1F0-LN-ITEM-CNTL-NUM",       214,  30, ALPHA),
        new FieldSpec("FM1F0-SERV-ELIG-CD-LN-1",      244,   1, ALPHA),
        new FieldSpec("FM1F0-SERV-ELIG-CD-LN-2",      245,   1, ALPHA),
        new FieldSpec("FM1F0-SERV-ELIG-CD-LN-3",      246,   1, ALPHA),
        new FieldSpec("FM1F0-SERV-ELIG-CD-LN-4",      247,   1, ALPHA),
        new FieldSpec("FM1F0-PERF-PROV-IHS-LN-IND",   248,   1, ALPHA),
        new FieldSpec("FM1F0-FILLER-1",               249,   7, ALPHA)
    );

    // FM1F1 — Professional Dental Line-of-Service (Record Type F1)
    public static final List<FieldSpec> FM1F1 = List.of(
        new FieldSpec("FM1F1-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1F1-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1F1-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1F1-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1F1-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1F1-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1F1-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1F1-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1F1-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1F1-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1F1-DT-SERV-START-CC",        26,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-START-YY",        28,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-START-MM",        30,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-START-DD",        32,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-END-CC",          34,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-END-YY",          36,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-END-MM",          38,   2, ALPHA),
        new FieldSpec("FM1F1-DT-SERV-END-DD",          40,   2, ALPHA),
        new FieldSpec("FM1F1-PLACE-SERV",              42,   2, ALPHA),
        new FieldSpec("FM1F1-TYPE-SERV",               44,   3, ALPHA),
        new FieldSpec("FM1F1-HCPCS-PROC-CD",           47,   5, ALPHA),
        new FieldSpec("FM1F1-HCPCS-PROC-CD-MOD-1",     52,   2, ALPHA),
        new FieldSpec("FM1F1-HCPCS-PROC-CD-MOD-2",     54,   2, ALPHA),
        new FieldSpec("FM1F1-HCPCS-PROC-CD-MOD-3",     56,   2, ALPHA),
        new FieldSpec("FM1F1-HCPCS-PROC-CD-MOD-4",     58,   2, ALPHA),
        new FieldSpec("FM1F1-ICD-DIAG-CD-IND-1",       60,   1, ALPHA),
        new FieldSpec("FM1F1-ICD-DIAG-CD-IND-2",       61,   1, ALPHA),
        new FieldSpec("FM1F1-ICD-DIAG-CD-IND-3",       62,   1, ALPHA),
        new FieldSpec("FM1F1-ICD-DIAG-CD-IND-4",       63,   1, ALPHA),
        new FieldSpec("FM1F1-NUM-SERV",                64,   4, NUMERIC_TEXT),
        new FieldSpec("FM1F1-SERV-HH",                 68,   2, NUMERIC_TEXT),
        new FieldSpec("FM1F1-SERV-MM",                 70,   2, NUMERIC_TEXT),
        new FieldSpec("FM1F1-SERV-CHRG",               72,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F1-PERF-PROV-NUM-LN",        78,  13, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-LAST-NAME",     91,  20, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-FIRST-NAME",   111,  10, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-MID-NAME",     121,   1, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-OFC-ZIP-CD5",  122,   5, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-OFC-ZIP-CD4",  127,   4, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-SPEC-LN",      131,   2, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-TYPE-LN",      133,   2, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-TXNMY-CD-LN",  135,  15, ALPHA),
        new FieldSpec("FM1F1-SERV-FAC-LOC-NUM-LN",    150,  13, ALPHA),
        new FieldSpec("FM1F1-SERV-LOC-NUM-QUAL-LN",   163,   2, ALPHA),
        new FieldSpec("FM1F1-CLASS-PROV-LN",          165,   1, ALPHA),
        new FieldSpec("FM1F1-NDC-CD",                 166,  13, ALPHA),
        new FieldSpec("FM1F1-TOOTH-NUM-LN",           179,   2, ALPHA),
        new FieldSpec("FM1F1-TOOTH-SURFACE-CD-1",     181,   2, ALPHA),
        new FieldSpec("FM1F1-TOOTH-SURFACE-CD-2",     183,   2, ALPHA),
        new FieldSpec("FM1F1-TOOTH-SURFACE-CD-3",     185,   2, ALPHA),
        new FieldSpec("FM1F1-TOOTH-SURFACE-CD-4",     187,   2, ALPHA),
        new FieldSpec("FM1F1-TOOTH-SURFACE-CD-5",     189,   2, ALPHA),
        new FieldSpec("FM1F1-ORAL-CAVITY-DES-1",      191,   3, ALPHA),
        new FieldSpec("FM1F1-ORAL-CAVITY-DES-2",      194,   3, ALPHA),
        new FieldSpec("FM1F1-ORAL-CAVITY-DES-3",      197,   3, ALPHA),
        new FieldSpec("FM1F1-ORAL-CAVITY-DES-4",      200,   3, ALPHA),
        new FieldSpec("FM1F1-ORAL-CAVITY-DES-5",      203,   3, ALPHA),
        new FieldSpec("FM1F1-PROSTHETIC-CROWN",       206,   1, ALPHA),
        new FieldSpec("FM1F1-PREDETRMN-BNFT-IND",     207,   1, ALPHA),
        new FieldSpec("FM1F1-PREDETRMN-BNFT-ID-LN",   208,  30, ALPHA),
        new FieldSpec("FM1F1-PERF-PROV-NPI-LN",       238,  10, ALPHA),
        new FieldSpec("FM1F1-SERV-FAC-LOC-ST-LN",     248,   2, ALPHA),
        new FieldSpec("FM1F1-FILLER-1",               250,   6, ALPHA)
    );

    // FM1F5 — Professional Line-Level Pricing & POS (Record Type F5)
    public static final List<FieldSpec> FM1F5 = List.of(
        new FieldSpec("FM1F5-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1F5-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1F5-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1F5-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1F5-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1F5-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1F5-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1F5-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1F5-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1F5-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1F5-SERV-ALLW-AMT",           26,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-PCT-FCTR-LN",             32,   3, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-PRC-MTD-LN",              35,   2, ALPHA),
        new FieldSpec("FM1F5-RULE-NUM-LN-1-3",         37,   3, ALPHA),
        new FieldSpec("FM1F5-RULE-NUM-LN-4-6",         40,   3, ALPHA),
        new FieldSpec("FM1F5-CAP-IND",                 43,   1, ALPHA),
        new FieldSpec("FM1F5-LOC-PLAN-REF-NUM",        44,  25, ALPHA),
        new FieldSpec("FM1F5-NUM-APPV-SERV",           69,   3, NUMERIC_TEXT),
        new FieldSpec("FM1F5-POS-PRC-LEV-IND",         72,   1, ALPHA),
        new FieldSpec("FM1F5-PREATH-PRECRT-STAT-IND",  73,   1, ALPHA),
        new FieldSpec("FM1F5-REF-STAT-IND",            74,   1, ALPHA),
        new FieldSpec("FM1F5-PERF-PCP-IND",            75,   1, ALPHA),
        new FieldSpec("FM1F5-REF-PCP-IND",             76,   2, ALPHA),
        new FieldSpec("FM1F5-REVIEW-DETER-COND-CD",    78,   2, ALPHA),
        new FieldSpec("FM1F5-PROV-BASE-PEN-AMT-LN",    80,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-PROV-BASE-PEN-PCT-LN",    86,   3, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-PRV-BASE-PEN-APL-RLE-LN", 89,   4, ALPHA),
        new FieldSpec("FM1F5-SF-MSG-CD-LN-1",          93,   4, ALPHA),
        new FieldSpec("FM1F5-SF-MSG-CD-LN-2",          97,   4, ALPHA),
        new FieldSpec("FM1F5-SF-MSG-CD-LN-3",         101,   4, ALPHA),
        new FieldSpec("FM1F5-SF-MSG-CD-LN-4",         105,   4, ALPHA),
        new FieldSpec("FM1F5-SF-MSG-CD-LN-5",         109,   4, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-CD-LN-1",  113,   3, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-CD-LN-2",  116,   3, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-CD-LN-3",  119,   3, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-CD-LN-4",  122,   3, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-CD-LN-5",  125,   3, ALPHA),
        new FieldSpec("FM1F5-SPEC-PRC-COND-AMT-LN-1", 128,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-SPEC-PRC-COND-AMT-LN-2", 134,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-SPEC-PRC-COND-AMT-LN-3", 140,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-SPEC-PRC-COND-AMT-LN-4", 146,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-SPEC-PRC-COND-AMT-LN-5", 152,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-SPEC-PRC-COND-PCT-LN-1", 158,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1F5-SPEC-PRC-COND-PCT-LN-2", 163,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1F5-SPEC-PRC-COND-PCT-LN-3", 168,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1F5-SPEC-PRC-COND-PCT-LN-4", 173,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1F5-SPEC-PRC-COND-PCT-LN-5", 178,   5, PACKED_DECIMAL, 5),
        new FieldSpec("FM1F5-PPO-PRV-TYP-AVL-LN",     183,   1, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-GP-CD-LN-PAYB-1",184,   2, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-RSN-CD-LN-PAYB-1",186,   5, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-AMT-LN-PAYB-1",  191,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-CLM-ADJ-GP-CD-LN-PAYB-2",197,   2, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-RSN-CD-LN-PAYB-2",199,   5, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-AMT-LN-PAYB-2",  204,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-CLM-ADJ-GP-CD-LN-PAYB-3",210,   2, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-RSN-CD-LN-PAYB-3",212,   5, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-AMT-LN-PAYB-3",  217,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-CLM-ADJ-GP-CD-LN-PAYB-4",223,   2, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-RSN-CD-LN-PAYB-4",225,   5, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-AMT-LN-PAYB-4",  230,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-CLM-ADJ-GP-CD-LN-PAYB-5",236,   2, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-RSN-CD-LN-PAYB-5",238,   5, ALPHA),
        new FieldSpec("FM1F5-CLM-ADJ-AMT-LN-PAYB-5",  243,   6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F5-INCL-GROUPING-NBR",      249,   2, ALPHA),
        new FieldSpec("FM1F5-ACT-AMB-MILEAGE",        251,   3, PACKED_DECIMAL, 1),
        new FieldSpec("FM1F5-TIER-DESIG-IND-LN",      254,   1, ALPHA),
        new FieldSpec("FM1F5-FILLER-1",               255,   1, ALPHA)
    );

    // FM1F6 — Professional OPL Line-Level (Record Type F6)
    public static final List<FieldSpec> FM1F6 = List.of(
        new FieldSpec("FM1F6-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1F6-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1F6-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1F6-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1F6-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1F6-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1F6-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1F6-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1F6-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1F6-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-1",         26,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-2",         28,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-3",         30,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-4",         32,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-5",         34,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-6",         36,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-7",         38,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-8",         40,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-9",         42,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-10",        44,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-11",        46,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-12",        48,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-13",        50,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-14",        52,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-15",        54,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-CD-LN-16",        56,   2, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-1",      58,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-2",      64,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-3",      70,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-4",      76,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-5",      82,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-6",      88,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-7",      94,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-8",     100,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-9",     106,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-10",    112,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-11",    118,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-12",    124,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-13",    130,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-14",    136,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-15",    142,   6, ALPHA),
        new FieldSpec("FM1F6-OPL-VAL-AMT-LN-X-16",    148,   6, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-1",154, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-1",156, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-1",  161, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-2",167, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-2",169, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-2",  174, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-3",180, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-3",182, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-3",  187, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-4",193, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-4",195, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-4",  200, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-5",206, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-5",208, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-5",  213, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYA-6",219, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYA-6",221, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYA-6",  226, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-CLM-ADJ-GP-CD-LN-PAYB",  232, 2, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-RSN-CD-LN-PAYB",  234, 5, ALPHA),
        new FieldSpec("FM1F6-CLM-ADJ-AMT-LN-PAYB",    239, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM1F6-BDC-MSG-CD-LN",          245, 4, ALPHA),
        new FieldSpec("FM1F6-FILLER-1",               249, 3, ALPHA),
        new FieldSpec("FM1F6-MKT-ID-LN",              252, 4, ALPHA)
    );

    // FM1G0 — Professional Referring Physician (Record Type G0)
    public static final List<FieldSpec> FM1G0 = List.of(
        new FieldSpec("FM1G0-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1G0-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1G0-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1G0-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1G0-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1G0-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1G0-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1G0-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1G0-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1G0-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1G0-REF-PROV-LAST-NAME",      26,  20, ALPHA),
        new FieldSpec("FM1G0-REF-PROV-FIRST-NAME",     46,  10, ALPHA),
        new FieldSpec("FM1G0-REF-PROV-MID-INIT",       56,   1, ALPHA),
        new FieldSpec("FM1G0-REF-PROV-NUM",            57,  14, ALPHA),
        new FieldSpec("FM1G0-OTH-PHY-LAST-NAME",       71,  20, ALPHA),
        new FieldSpec("FM1G0-OTH-PHY-FIRST-NAME",      91,  10, ALPHA),
        new FieldSpec("FM1G0-OTH-PHY-MID-INIT",       101,   1, ALPHA),
        new FieldSpec("FM1G0-OTH-PHY-NUM",            102,  14, ALPHA),
        new FieldSpec("FM1G0-REF-PROV-NUM-QUAL",      116,   2, ALPHA),
        new FieldSpec("FM1G0-FILLER-1",               118, 138, ALPHA)
    );

    // FM1X0 — Professional Trailer (Record Type X0)
    public static final List<FieldSpec> FM1X0 = List.of(
        new FieldSpec("FM1X0-SER-NUM-LOCAL-PLAN",       1,   3, ALPHA),
        new FieldSpec("FM1X0-SER-NUM-JULDT-CC",         4,   2, ALPHA),
        new FieldSpec("FM1X0-SER-NUM-JULDT-YY",         6,   2, ALPHA),
        new FieldSpec("FM1X0-SER-NUM-JULDT-DDD",        8,   3, ALPHA),
        new FieldSpec("FM1X0-SER-NUM-SEQUENCE",        11,   5, ALPHA),
        new FieldSpec("FM1X0-SER-NUM-SUFFIX",          16,   2, ALPHA),
        new FieldSpec("FM1X0-TRANS-ID",                18,   2, ALPHA),
        new FieldSpec("FM1X0-TRANS-QUAL",              20,   2, ALPHA),
        new FieldSpec("FM1X0-REC-TYPE",                22,   2, ALPHA),
        new FieldSpec("FM1X0-SEQ-NUM",                 24,   2, BINARY),
        new FieldSpec("FM1X0-PHY-REC-CNT",             26,   4, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-BX-CNT",         30,   2, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-CX-CNT",         32,   2, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-DX-CNT",         34,   2, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-EX-CNT",         36,   4, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-FX-CNT",         40,   4, NUMERIC_TEXT),
        new FieldSpec("FM1X0-REC-TYPE-GX-CNT",         44,   2, NUMERIC_TEXT),
        new FieldSpec("FM1X0-BLUE2-USER-ID",           46,  40, ALPHA),
        new FieldSpec("FM1X0-FILLER-1",                86, 170, ALPHA)
    );
	
    /** Registry: record type -> layout. */
    public static Map<RecordType, List<FieldSpec>> all() {
        Map<RecordType, List<FieldSpec>> m = new LinkedHashMap<>();
        m.put(RecordType.RT_05, FM105);
        m.put(RecordType.RT_10, FM110);
        m.put(RecordType.RT_15, FM115);
        m.put(RecordType.RT_20, FM120);
        m.put(RecordType.RT_30, FM130);
        m.put(RecordType.RT_31, FM131);
        m.put(RecordType.RT_32, FM132);
        m.put(RecordType.RT_33, FM133);
        m.put(RecordType.RT_40, FM140);
        m.put(RecordType.RT_41, FM141);
        m.put(RecordType.RT_42, FM142);
        m.put(RecordType.RT_43, FM143);
        m.put(RecordType.RT_44, FM144);
        m.put(RecordType.RT_45, FM145);
        m.put(RecordType.RT_46, FM146);
        m.put(RecordType.RT_47, FM147);
        m.put(RecordType.RT_50, FM150);
        m.put(RecordType.RT_60, FM160);
        m.put(RecordType.RT_65, FM165);
        m.put(RecordType.RT_66, FM166);
        m.put(RecordType.RT_71, FM171);
        m.put(RecordType.RT_72, FM172);
        m.put(RecordType.RT_73, FM173);
        m.put(RecordType.RT_74, FM174);
        m.put(RecordType.RT_80, FM180);
        m.put(RecordType.RT_90, FM190);
        m.put(RecordType.RT_9D, FM9D);
        m.put(RecordType.RT_A5, FM1A5);
        m.put(RecordType.RT_B0, FM1B0);
        m.put(RecordType.RT_B5, FM1B5);
        m.put(RecordType.RT_C0, FM1C0);
        m.put(RecordType.RT_D0, FM1D0);
        m.put(RecordType.RT_D1, FM1D1);
        m.put(RecordType.RT_D2, FM1D2);
        m.put(RecordType.RT_D3, FM1D3);
        m.put(RecordType.RT_E0, FM1E0);
        m.put(RecordType.RT_E1, FM1E1);
        m.put(RecordType.RT_E2, FM1E2);
        m.put(RecordType.RT_E6, FM1E6);
        m.put(RecordType.RT_F0, FM1F0);
        m.put(RecordType.RT_F1, FM1F1);
        m.put(RecordType.RT_F5, FM1F5);
        m.put(RecordType.RT_F6, FM1F6);
        m.put(RecordType.RT_G0, FM1G0);
        m.put(RecordType.RT_X0, FM1X0);
        return m;
    }
}

