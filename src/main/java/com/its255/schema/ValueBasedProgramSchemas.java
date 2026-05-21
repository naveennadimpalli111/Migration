package com.its255.schema;

import static com.its255.schema.FieldType.ALPHA;
import static com.its255.schema.FieldType.BINARY;
import static com.its255.schema.FieldType.NUMERIC_TEXT;
import static com.its255.schema.FieldType.PACKED_DECIMAL;

import java.util.List;

public class ValueBasedProgramSchemas {
	
	public static final List<FieldSpec> CBFBD = List.of(

		    // Key
		    new FieldSpec("CBFBD-SCCF-SER-NUM", 1, 17, ALPHA),
		    new FieldSpec("CBFBD-HOME-PLAN-MEM-ID", 18, 22, ALPHA),
		    new FieldSpec("CBFBD-CONS-MEM-ID", 40, 22, ALPHA),
		    new FieldSpec("CBFBD-SUB-ID-NUM-ACTUAL", 62, 17, ALPHA),
		    new FieldSpec("CBFBD-CB-BILL-START-DT", 79, 8, ALPHA),
		    new FieldSpec("CBFBD-CB-BILL-END-DT", 87, 8, ALPHA),

		    // Control Data
		    new FieldSpec("CBFBD-NDW-HOME-PLAN-ID", 95, 3, ALPHA),
		    new FieldSpec("CBFBD-HOME-CORP-PLAN-CD", 98, 3, ALPHA),
		    new FieldSpec("CBFBD-CNTL-PLAN-CD", 101, 3, ALPHA),
		    new FieldSpec("CBFBD-NDW-HOST-PLAN-ID", 104, 3, ALPHA),
		    new FieldSpec("CBFBD-HOST-CORP-PLAN-CD", 107, 3, ALPHA),
		    new FieldSpec("CBFBD-LOC-PLAN-CD", 110, 3, ALPHA),
		    new FieldSpec("CBFBD-LOC-PLAN-STA-CD", 113, 4, ALPHA),
		    new FieldSpec("CBFBD-LOC-PLAN-CFA-ACCT-CD", 117, 2, ALPHA),
		    new FieldSpec("CBFBD-CNTL-PLAN-STA-CD", 119, 4, ALPHA),
		    new FieldSpec("CBFBD-CNTL-PLAN-CFA-ACCT-CD", 123, 2, ALPHA),
		    new FieldSpec("CBFBD-BATCH-CREATE-DT", 125, 8, ALPHA),
		    new FieldSpec("CBFBD-TRANS-IND", 133, 1, ALPHA),
		    new FieldSpec("CBFBD-STAT-CD", 134, 1, ALPHA),
		    new FieldSpec("CBFBD-BILL-DET-POST-DT", 135, 8, ALPHA),
		    new FieldSpec("CBFBD-EDIT-DT", 143, 8, ALPHA),

		    // Error Codes
		    new FieldSpec("CBFBD-ERR-CD-1", 151, 5, ALPHA),
		    new FieldSpec("CBFBD-ERR-CD-2", 156, 5, ALPHA),
		    new FieldSpec("CBFBD-ERR-CD-3", 161, 5, ALPHA),
		    new FieldSpec("CBFBD-ERR-CD-4", 166, 5, ALPHA),
		    new FieldSpec("CBFBD-ERR-CD-5", 171, 5, ALPHA),
		    new FieldSpec("CBFBD-BILL-DET-EXT-STA-CD", 176, 1, ALPHA),

		    // Capitated Billing Format Data
		    new FieldSpec("CBFBD-CB-RATE-AMT", 177, 6, PACKED_DECIMAL, 2),
		    new FieldSpec("CBFBD-CROSS-REF-SCCF-NUM", 183, 17, ALPHA),
		    new FieldSpec("CBFBD-HOME-AUTH-DEN-DT", 200, 8, ALPHA),
		    new FieldSpec("CBFBD-CFA-DEFLT-PMT-DT", 208, 8, ALPHA),
		    new FieldSpec("CBFBD-DISP-DT", 216, 8, ALPHA),
		    new FieldSpec("CBFBD-CFA-PRCSS-DT", 224, 8, ALPHA),
		    new FieldSpec("CBFBD-TYPE-CFA-REIMB", 232, 1, ALPHA),
		    new FieldSpec("CBFBD-TYPE-CFA-TRANS", 233, 2, ALPHA),
		    new FieldSpec("CBFBD-BATCH-STAT-CD", 235, 1, ALPHA),

		    // Billing Data
		    new FieldSpec("CBFBD-INCUR-PER-START-DT", 236, 8, ALPHA),
		    new FieldSpec("CBFBD-INCUR-PER-END-DT", 244, 8, ALPHA),
		    new FieldSpec("CBFBD-MEAS-PER-START-DT", 252, 8, ALPHA),
		    new FieldSpec("CBFBD-MEAS-PER-END-DT", 260, 8, ALPHA),
		    new FieldSpec("CBFBD-MEM-LEVEL-CHG-AMT", 268, 5, PACKED_DECIMAL, 2),
		    new FieldSpec("CBFBD-VAL-BASED-PGM-CD", 273, 3, ALPHA),
		    new FieldSpec("CBFBD-BILL-DET-PROV-GRP-NM", 276, 25, ALPHA),
		    new FieldSpec("CBFBD-ATTRIB-PROV-PYMT-TYPE-CD", 301, 4, ALPHA),
		    new FieldSpec("CBFBD-BILL-DET-SETT-DT", 305, 8, ALPHA),
		    new FieldSpec("CBFBD-VAL-BASED-PROG-PYMT-TP", 313, 3, ALPHA),
		    new FieldSpec("CBFBD-EPISODE-TYPE-CD", 316, 2, ALPHA),
		    new FieldSpec("CBFBD-VAR-ACCT-ID", 318, 10, ALPHA),
		    new FieldSpec("CBFBD-ACC-FEE-ADJ-AMT", 328, 5, PACKED_DECIMAL, 2),

		    // Member / Provider Data
		    new FieldSpec("CBFBD-FILLER-2", 333, 3, ALPHA),
		    new FieldSpec("CBFBD-SUB-GRP-NUM", 336, 20, ALPHA),
		    new FieldSpec("CBFBD-ATTRIB-BCBS-PROV-ID", 356, 13, ALPHA),
		    new FieldSpec("CBFBD-ATTRIB-NPI-PROV-ID", 369, 10, ALPHA),
		    new FieldSpec("CBFBD-VBP-ID", 379, 7, ALPHA),

		    new FieldSpec("CBFBD-FILLER-3", 386, 93, ALPHA),
		    new FieldSpec("CBFBD-MASTER-MEMB-INDEX-ID", 479, 22, ALPHA),
		    new FieldSpec("CBFBD-BOID", 501, 4, ALPHA),
		    new FieldSpec("CBFBD-ORIG-ITS-SCCF-NUM", 505, 17, ALPHA),
		    new FieldSpec("CBFBD-ORIG-BDF-SCCF-NUM", 522, 17, ALPHA),
		    new FieldSpec("CBFBD-ELIG-START-DT", 539, 8, ALPHA),
		    new FieldSpec("CBFBD-ELIG-END-DT", 547, 8, ALPHA),
		    new FieldSpec("CBFBD-DISPUTED-REC-IND", 555, 1, ALPHA),
		    new FieldSpec("CBFBD-HOME-DSPT-ACTN-CD", 556, 1, ALPHA),
		    new FieldSpec("CBFBD-CMNT-TXT", 557, 75, ALPHA),
		    new FieldSpec("CBFBD-HOST-DSPT-ACTN-CD", 632, 1, ALPHA),
		    new FieldSpec("CBFBD-LOC-PLAN-CNTL-ID", 633, 17, ALPHA),
		    new FieldSpec("CBFBD-PRCSG-SITE-CNTL-ID", 650, 17, ALPHA),
		    new FieldSpec("CBFBD-BDF-RCD-TYP-CD", 667, 1, ALPHA),
		    new FieldSpec("CBFBD-HOST-ADJ-CD", 668, 1, ALPHA),
		    new FieldSpec("CBFBD-HOST-CMNT-TXT", 669, 40, ALPHA),
		    new FieldSpec("CBFBD-FILLER-1", 709, 96, ALPHA)
		);

}
