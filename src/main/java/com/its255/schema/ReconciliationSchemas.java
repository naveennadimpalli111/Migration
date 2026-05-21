package com.its255.schema;

import static com.its255.schema.FieldType.ALPHA;
import static com.its255.schema.FieldType.BINARY;
import static com.its255.schema.FieldType.NUMERIC_TEXT;
import static com.its255.schema.FieldType.PACKED_DECIMAL;

import java.util.List;

public final class ReconciliationSchemas {

    // =========================
    // FM31A — Reconciliation Header (Record Type 31)
    // =========================
    public static final List<FieldSpec> FM31A = List.of(
        new FieldSpec("FM31A-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
        new FieldSpec("FM31A-SER-NUM-JULDT-CC", 4, 2, ALPHA),
        new FieldSpec("FM31A-SER-NUM-JULDT-YY", 6, 2, ALPHA),
        new FieldSpec("FM31A-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
        new FieldSpec("FM31A-SER-NUM-SEQUENCE", 11, 5, ALPHA),
        new FieldSpec("FM31A-SER-NUM-SUFFIX", 16, 2, ALPHA),
        new FieldSpec("FM31A-TRANS-ID", 18, 2, ALPHA),
        new FieldSpec("FM31A-TRANS-QUAL", 20, 2, ALPHA),
        new FieldSpec("FM31A-REC-TYPE", 22, 2, ALPHA),
        new FieldSpec("FM31A-SEQ-NUM", 24, 2, BINARY),

        new FieldSpec("FM31A-PROC-SITE-PLAN-CD", 26, 3, ALPHA),
        new FieldSpec("FM31A-LOC-PLAN-CD", 29, 3, ALPHA),
        new FieldSpec("FM31A-CNTL-PLAN-CD", 32, 3, ALPHA),
        new FieldSpec("FM31A-PROC-SITE-STA-CD", 35, 4, ALPHA),
        new FieldSpec("FM31A-LOC-PLAN-STA-CD", 39, 4, ALPHA),
        new FieldSpec("FM31A-TRANSM-MODE-CD", 43, 1, ALPHA),
        new FieldSpec("FM31A-REL-NUM", 44, 3, ALPHA),
        new FieldSpec("FM31A-CLM-TYPE", 47, 2, ALPHA),
        new FieldSpec("FM31A-DISP-CD", 49, 1, ALPHA),

        new FieldSpec("FM31A-DT-PAID-CC", 50, 2, ALPHA),
        new FieldSpec("FM31A-DT-PAID-YY", 52, 2, ALPHA),
        new FieldSpec("FM31A-DT-PAID-MM", 54, 2, ALPHA),
        new FieldSpec("FM31A-DT-PAID-DD", 56, 2, ALPHA),

        new FieldSpec("FM31A-LOC-PLAN-CNTL-NUM", 58, 17, ALPHA),
        new FieldSpec("FM31A-LOC-PLAN-CLM-REF-NUM", 75, 17, ALPHA),
        new FieldSpec("FM31A-PROC-SITE-CNTL-NUM", 92, 17, ALPHA),
        new FieldSpec("FM31A-CHK-VCHR-NUM", 109, 10, ALPHA),

        new FieldSpec("FM31A-CREATE-DT-CC", 119, 2, ALPHA),
        new FieldSpec("FM31A-CREATE-DT-YY", 121, 2, ALPHA),
        new FieldSpec("FM31A-CREATE-DT-MM", 123, 2, ALPHA),
        new FieldSpec("FM31A-CREATE-DT-DD", 125, 2, ALPHA),

        new FieldSpec("FM31A-TRANS-IND", 127, 1, ALPHA),
        new FieldSpec("FM31A-STAT-CD", 128, 1, ALPHA),

        new FieldSpec("FM31A-ERR-CD-1", 129, 5, ALPHA),
        new FieldSpec("FM31A-ERR-CD-2", 134, 5, ALPHA),
        new FieldSpec("FM31A-ERR-CD-3", 139, 5, ALPHA),
        new FieldSpec("FM31A-ERR-CD-4", 144, 5, ALPHA),
        new FieldSpec("FM31A-ERR-CD-5", 149, 5, ALPHA),

        new FieldSpec("FM31A-EDIT-DT-CC", 154, 2, ALPHA),
        new FieldSpec("FM31A-EDIT-DT-YY", 156, 2, ALPHA),
        new FieldSpec("FM31A-EDIT-DT-MM", 158, 2, ALPHA),
        new FieldSpec("FM31A-EDIT-DT-DD", 160, 2, ALPHA),

        new FieldSpec("FM31A-NET-LIAB-AMT", 162, 6, PACKED_DECIMAL, 2),

        new FieldSpec("FM31A-DISP-DT-CC", 168, 2, ALPHA),
        new FieldSpec("FM31A-DISP-DT-YY", 170, 2, ALPHA),
        new FieldSpec("FM31A-DISP-DT-MM", 172, 2, ALPHA),
        new FieldSpec("FM31A-DISP-DT-DD", 174, 2, ALPHA),

        new FieldSpec("FM31A-CFA-STAT-CD", 176, 1, ALPHA),

        new FieldSpec("FM31A-BAT-NUM-LOCAL-PLAN", 177, 3, ALPHA),
        new FieldSpec("FM31A-BAT-NUM-JULDT-CC", 180, 2, ALPHA),
        new FieldSpec("FM31A-BAT-NUM-JULDT-YY", 182, 2, ALPHA),
        new FieldSpec("FM31A-BAT-NUM-JULDT-DDD", 184, 3, ALPHA),
        new FieldSpec("FM31A-BAT-NUM-SEQUENCE", 187, 4, ALPHA),

        new FieldSpec("FM31A-TYPE-RECON", 191, 1, ALPHA),
        new FieldSpec("FM31A-RECYC-CTR", 192, 1, NUMERIC_TEXT),
        new FieldSpec("FM31A-PLAN-PAYER-CD", 193, 1, ALPHA),
        new FieldSpec("FM31A-PMT-DISP-CD", 194, 1, ALPHA),
        new FieldSpec("FM31A-NAT-OOA-CD", 195, 1, ALPHA),
        new FieldSpec("FM31A-CNTL-PLAN-CFA-ACCT-CD", 196, 2, ALPHA),
        new FieldSpec("FM31A-LOC-PLAN-CFA-ACCT-CD", 198, 2, ALPHA),
        new FieldSpec("FM31A-CFA-BILL-IND", 200, 1, ALPHA),
        new FieldSpec("FM31A-PGM-CD", 201, 1, ALPHA),

        new FieldSpec("FM31A-FMT-DB-POST-DT-CC", 202, 2, ALPHA),
        new FieldSpec("FM31A-FMT-DB-POST-DT-YY", 204, 2, ALPHA),
        new FieldSpec("FM31A-FMT-DB-POST-DT-MM", 206, 2, ALPHA),
        new FieldSpec("FM31A-FMT-DB-POST-DT-DD", 208, 2, ALPHA),

        new FieldSpec("FM31A-BCP-PROD-TYPE", 210, 1, ALPHA),
        new FieldSpec("FM31A-PLAN-OWNER-IND", 211, 1, ALPHA),
        new FieldSpec("FM31A-EXCLUDE-PER-USER", 212, 1, ALPHA),
        new FieldSpec("FM31A-EXCLUDE-PER-BLUECD", 213, 1, ALPHA),
        new FieldSpec("FM31A-INTL-CD", 214, 1, ALPHA),
        new FieldSpec("FM31A-ECR-IND", 215, 1, ALPHA),
        new FieldSpec("FM31A-ACCT-TYPE-CD", 216, 1, ALPHA),
        new FieldSpec("FM31A-BOID", 217, 4, ALPHA),
        new FieldSpec("FM31A-DEFLT-CLM-RSLTN-IND", 221, 1, ALPHA),

        new FieldSpec("FM31A-FILLER-1", 222, 34, ALPHA)
    );


    // =========================
    // FM32A — Reconciliation Data (Record Type 32)
    // =========================
    public static final List<FieldSpec> FM32A = List.of(
        new FieldSpec("FM32A-SER-NUM-LOCAL-PLAN", 1, 3, ALPHA),
        new FieldSpec("FM32A-SER-NUM-JULDT-CC", 4, 2, ALPHA),
        new FieldSpec("FM32A-SER-NUM-JULDT-YY", 6, 2, ALPHA),
        new FieldSpec("FM32A-SER-NUM-JULDT-DDD", 8, 3, ALPHA),
        new FieldSpec("FM32A-SER-NUM-SEQUENCE", 11, 5, ALPHA),
        new FieldSpec("FM32A-SER-NUM-SUFFIX", 16, 2, ALPHA),
        new FieldSpec("FM32A-TRANS-ID", 18, 2, ALPHA),
        new FieldSpec("FM32A-TRANS-QUAL", 20, 2, ALPHA),
        new FieldSpec("FM32A-REC-TYPE", 22, 2, ALPHA),
        new FieldSpec("FM32A-SEQ-NUM", 24, 2, BINARY),

        new FieldSpec("FM32A-SUB-ID-PFX-1-3", 26, 3, ALPHA),
        new FieldSpec("FM32A-SUB-ID-SUFX-4-17", 29, 14, ALPHA),
        new FieldSpec("FM32A-SUB-GRP-NUM", 43, 9, ALPHA),

        new FieldSpec("FM32A-ADM-SERV-DT-CC", 52, 2, ALPHA),
        new FieldSpec("FM32A-ADM-SERV-DT-YY", 54, 2, ALPHA),
        new FieldSpec("FM32A-ADM-SERV-DT-MM", 56, 2, ALPHA),
        new FieldSpec("FM32A-ADM-SERV-DT-DD", 58, 2, ALPHA),

        new FieldSpec("FM32A-TOT-AMT-APPV-PMT", 60, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM32A-TOT-PMT-AMT", 66, 6, PACKED_DECIMAL, 2),
        new FieldSpec("FM32A-PMT-MOD", 72, 2, ALPHA),
        new FieldSpec("FM32A-ADDL-STAT-DATA-CD", 74, 1, ALPHA),

        new FieldSpec("FM32A-LOC-PLAN-RCPT-DT-CC", 75, 2, ALPHA),
        new FieldSpec("FM32A-LOC-PLAN-RCPT-DT-YY", 77, 2, ALPHA),
        new FieldSpec("FM32A-LOC-PLAN-RCPT-DT-MM", 79, 2, ALPHA),
        new FieldSpec("FM32A-LOC-PLAN-RCPT-DT-DD", 81, 2, ALPHA),

        new FieldSpec("FM32A-BATCH-CREATE-DT-CC", 83, 2, ALPHA),
        new FieldSpec("FM32A-BATCH-CREATE-DT-YY", 85, 2, ALPHA),
        new FieldSpec("FM32A-BATCH-CREATE-DT-MM", 87, 2, ALPHA),
        new FieldSpec("FM32A-BATCH-CREATE-DT-DD", 89, 2, ALPHA),

        new FieldSpec("FM32A-HOME-AUTH-DEN-DT-CC", 91, 2, ALPHA),
        new FieldSpec("FM32A-HOME-AUTH-DEN-DT-YY", 93, 2, ALPHA),
        new FieldSpec("FM32A-HOME-AUTH-DEN-DT-MM", 95, 2, ALPHA),
        new FieldSpec("FM32A-HOME-AUTH-DEN-DT-DD", 97, 2, ALPHA),

        new FieldSpec("FM32A-CFA-DISP-DT-CC", 99, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DISP-DT-YY", 101, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DISP-DT-MM", 103, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DISP-DT-DD", 105, 2, ALPHA),

        new FieldSpec("FM32A-CFA-DEFLT-PMT-DT-CC", 107, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DEFLT-PMT-DT-YY", 109, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DEFLT-PMT-DT-MM", 111, 2, ALPHA),
        new FieldSpec("FM32A-CFA-DEFLT-PMT-DT-DD", 113, 2, ALPHA),

        new FieldSpec("FM32A-CFA-PRCSS-DT-CC", 115, 2, ALPHA),
        new FieldSpec("FM32A-CFA-PRCSS-DT-YY", 117, 2, ALPHA),
        new FieldSpec("FM32A-CFA-PRCSS-DT-MM", 119, 2, ALPHA),
        new FieldSpec("FM32A-CFA-PRCSS-DT-DD", 121, 2, ALPHA),

        new FieldSpec("FM32A-ADMIN-EXP-ALLW", 123, 5, PACKED_DECIMAL, 2),
        new FieldSpec("FM32A-ACCESS-FEE-AMT", 128, 5, PACKED_DECIMAL, 2),
        new FieldSpec("FM32A-CLM-LIAB-AMT", 133, 6, PACKED_DECIMAL, 2),

        new FieldSpec("FM32A-PRFX-ACCT-ID-IND", 139, 1, ALPHA),
        new FieldSpec("FM32A-PRFX-ACCT-ID", 140, 20, ALPHA),

        new FieldSpec("FM32A-FILLER-1", 160, 96, ALPHA)
    );
}