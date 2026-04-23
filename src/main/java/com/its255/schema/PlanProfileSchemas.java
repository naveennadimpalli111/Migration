package com.its255.schema;

import static java.util.List.of;
import java.util.List;

/**
 * SCCF Plan Profile Schemas
 * Source: National Programs Release 25.5 – Plan Profile Record Layouts
 */
public final class PlanProfileSchemas {

    private PlanProfileSchemas() {}

    /** FM51A – Plan Profile Update Header */
    public static final List<FieldSpec> FM51A = of(
        new FieldSpec("TRANS_ID",                1,   2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE",                3,   2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM",                 5,   2, FieldType.NUMERIC_TEXT),

        new FieldSpec("INSTALL_PLAN_CD",          7,   3, FieldType.ALPHA),
        new FieldSpec("DEST_PLAN_STA_CD",        10,   4, FieldType.ALPHA),
        new FieldSpec("INSTALL_PLAN_STA_CD",     14,   4, FieldType.ALPHA),

        new FieldSpec("BILL_CLM_TYPE",            18,  2, FieldType.ALPHA),
        new FieldSpec("TRANSM_MODE_CD_PP",        20,  1, FieldType.ALPHA),
        new FieldSpec("VER_NUM_PP_UPDT",          21,  3, FieldType.ALPHA),
        new FieldSpec("TRANS_IND",                24,  1, FieldType.ALPHA),

        // -------- FULL RECORD KEY --------
        new FieldSpec("PRFX_ACCT_ID",            195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY",        215,  3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1",            218,  1, FieldType.ALPHA),

        new FieldSpec("SUB_PFX_ID",              219,  3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD",             231,  3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2",            234,  1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE",                235,  2, FieldType.ALPHA),

        // -------- EFFECTIVE START DATE (nines‑complement) --------
        new FieldSpec("EFF_ST_DT_C",              237,  1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY",             238,  2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM",             240,  2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD",             242,  2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM",               244,  2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3",            246,  1, FieldType.ALPHA),

        // -------- RECEIPT / INCUR START DATE --------
        new FieldSpec("RCPT_INCUR_DT_C",          247,  1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_DT_YY",         248,  2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_DT_MM",         250,  2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_DT_DD",         252,  2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R",               254,  2, FieldType.ALPHA)
    );

    /** FM52A – Plan Profile Update Control Data */
    public static final List<FieldSpec> FM52A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        // ---- EFFECTIVE END DATE ----
        new FieldSpec("EFF_END_DT_C", 7, 1, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_MM", 8, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_DD", 10, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_YY", 12, 2, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_DT_CD", 14, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_STA_CD", 15, 4, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_STA_CD", 19, 4, FieldType.ALPHA),
        new FieldSpec("TRANSM_MODE_CD_PP", 23, 1, FieldType.ALPHA),

        new FieldSpec("ACCT_PLAN_NAME", 24, 50, FieldType.ALPHA),
        new FieldSpec("STAT_CD", 74, 1, FieldType.ALPHA),
        new FieldSpec("STAT_QUAL", 75, 2, FieldType.ALPHA),

        // ---- ONLINE LAST ACTIVITY DATE ----
        new FieldSpec("ONL_LAST_ACT_C", 77, 1, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_MM", 78, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_DD", 80, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_YY", 82, 2, FieldType.ALPHA),

        // ---- CONTROL PLAN CREATE DATE ----
        new FieldSpec("CNTL_PLAN_CREATE_C", 84, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_MM", 85, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_DD", 87, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_YY", 89, 2, FieldType.ALPHA),

        // ---- CONTROL PLAN VERIFICATION DATE ----
        new FieldSpec("CNTL_PLAN_VER_C", 91, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_MM", 92, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_DD", 94, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_YY", 96, 2, FieldType.ALPHA),

        // ---- CONTROL PLAN DISTRIBUTION DATE ----
        new FieldSpec("CNTL_PLAN_DIST_C", 98, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_MM", 99, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_DD", 101, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_YY", 103, 2, FieldType.ALPHA),

        // ---- LOCAL PLAN PP RECEPTION DATE ----
        new FieldSpec("LOC_PLAN_PP_RCP_C", 105, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_MM", 106, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_DD", 108, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_YY", 110, 2, FieldType.ALPHA),

        // ---- LOCAL PLAN CANCELLATION DATE ----
        new FieldSpec("LOC_PLAN_CAN_C", 112, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_MM", 113, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_DD", 115, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_YY", 117, 2, FieldType.ALPHA),

        // ---- LOCAL PLAN VERIFICATION DATE ----
        new FieldSpec("LOC_PLAN_VER_C", 119, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_MM", 120, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_DD", 122, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_YY", 124, 2, FieldType.ALPHA),

        // ---- LOCAL PLAN DISTRIBUTION DATE ----
        new FieldSpec("LOC_PLAN_DIST_C", 126, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_MM", 127, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_DD", 129, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_YY", 131, 2, FieldType.ALPHA),

        // ---- CONTROL PLAN RECEIPT DATE ----
        new FieldSpec("CNTL_PLAN_RCPT_C", 133, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_MM", 134, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_DD", 136, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_YY", 138, 2, FieldType.ALPHA),

        // ---- CONTROL PLAN CANCELLATION DATE ----
        new FieldSpec("CNTL_PLAN_CAN_C", 140, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_MM", 141, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_DD", 143, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_YY", 145, 2, FieldType.ALPHA),

        new FieldSpec("OOA_LMT_BNFT_IND", 147, 1, FieldType.ALPHA),

        // ---- FULL RECORD KEY ----
        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        // ---- EFFECTIVE START DATE ----
        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        // ---- RECEIPT / INCUR START DATE ----
        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );

    /** FM53A – Plan Profile Update Submission Data */
    public static final List<FieldSpec> FM53A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        // ---- RECEIPT / INCUR END DATE ----
        new FieldSpec("RCPT_INCUR_END_DT_C", 7, 1, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_MM", 8, 2, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_DD", 10, 2, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_YY", 12, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_CD", 14, 3, FieldType.ALPHA),

        new FieldSpec("SUBM_PROC_IND", 24, 1, FieldType.ALPHA),
        new FieldSpec("SUBM_EDIT_IND", 25, 1, FieldType.ALPHA),
        new FieldSpec("PROV_DATA_IND", 26, 1, FieldType.ALPHA),
        new FieldSpec("PRC_DATA_IND", 27, 1, FieldType.ALPHA),
        new FieldSpec("PGM_CD", 30, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_STA_CD", 32, 4, FieldType.ALPHA),
        new FieldSpec("PROC_SITE_STA_CD", 36, 4, FieldType.ALPHA),
        new FieldSpec("PROC_SITE_PLAN_CD", 40, 3, FieldType.ALPHA),
        new FieldSpec("MNG_CARE_CD", 43, 1, FieldType.ALPHA),
        new FieldSpec("UPF_PRC_EDIT_CD", 44, 1, FieldType.ALPHA),
        new FieldSpec("DEL_MTH", 45, 1, FieldType.ALPHA),
        new FieldSpec("PRFX_ACCT_ID_IND", 46, 1, FieldType.ALPHA),
        new FieldSpec("NAT_OOA_CD", 47, 1, FieldType.ALPHA),
        new FieldSpec("FLXBL_NETW_IND", 48, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CFA_ACCT_CD", 49, 2, FieldType.ALPHA),

        // ---- CFA GROUP 1 ----
        new FieldSpec("CFA_REIMB_1", 51, 1, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_1", 52, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_2", 54, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_3", 56, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_4", 58, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_5", 60, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_6", 62, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_7", 64, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_8", 66, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_9", 68, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_10", 70, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_11", 72, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_1_12", 74, 2, FieldType.ALPHA),

        // ---- CFA GROUP 2 ----
        new FieldSpec("CFA_REIMB_2", 76, 1, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_1", 77, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_2", 79, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_3", 81, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_4", 83, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_5", 85, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_6", 87, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_7", 89, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_8", 91, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_9", 93, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_10", 95, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_11", 97, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_2_12", 99, 2, FieldType.ALPHA),

        // ---- CFA GROUP 3 ----
        new FieldSpec("CFA_REIMB_3", 101, 1, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_1", 102, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_2", 104, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_3", 106, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_4", 108, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_5", 110, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_6", 112, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_7", 114, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_8", 116, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_9", 118, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_10", 120, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_11", 122, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_3_12", 124, 2, FieldType.ALPHA),

        // ---- CFA GROUP 4 ----
        new FieldSpec("CFA_REIMB_4", 126, 1, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_1", 127, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_2", 129, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_3", 131, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_4", 133, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_5", 135, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_6", 137, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_7", 139, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_8", 141, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_9", 143, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_10", 145, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_11", 147, 2, FieldType.ALPHA),
        new FieldSpec("CFA_TRANS_4_12", 149, 2, FieldType.ALPHA),

        // ---- TAIL ----
        new FieldSpec("ACCT_TYPE_CD", 151, 1, FieldType.ALPHA),
        new FieldSpec("CUST_PRIMY_NTWK", 152, 4, FieldType.ALPHA),
        new FieldSpec("CUST_SCNDY_NTWK", 156, 4, FieldType.ALPHA),
        new FieldSpec("WRLD_WIDE_BNFT_IND", 160, 1, FieldType.ALPHA),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    /** FM54A – Plan Profile Update Disposition Data */
    public static final List<FieldSpec> FM54A = of(
        // ---- HEADER ----
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("PLAN_PAYER_CD", 7, 1, FieldType.ALPHA),

        // ---- DISPOSITION DETAILS ----
        new FieldSpec("SCDF_TYPE_CD", 18, 1, FieldType.ALPHA),
        new FieldSpec("ADDL_STAT_DATA_CD", 19, 1, FieldType.ALPHA),
        new FieldSpec("EOB_GEN_CD", 21, 1, FieldType.ALPHA),

        new FieldSpec("PROC_SITE_STA_CD", 27, 4, FieldType.ALPHA),

        new FieldSpec("CFA_CD", 31, 1, FieldType.ALPHA),
        new FieldSpec("AEA_CD", 32, 1, FieldType.ALPHA),

        // ---- AEA NON‑STANDARD AMOUNT (SAME BYTES, TWO VIEWS) ----
        new FieldSpec("AEA_NON_STD_AMT_X", 33, 5, FieldType.ALPHA),
        new FieldSpec("AEA_NON_STD_AMT",   33, 5, FieldType.NUMERIC_TEXT),

        new FieldSpec("ACCESS_FEE_CD", 38, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CFA_ACCT_CD", 39, 2, FieldType.ALPHA),
        new FieldSpec("RESUB_DF_IND", 42, 1, FieldType.ALPHA),

        // ---- FULL RECORD KEY ----
        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        // ---- EFFECTIVE START DATE ----
        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        // ---- RECEIPT / INCUR START DATE ----
        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    /** FM55A – Plan Profile Update Account Fee Control Data */
    public static final List<FieldSpec> FM55A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("EFF_END_DT_C", 7, 1, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_MM", 8, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_DD", 10, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_YY", 12, 2, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_DT_CD", 14, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_STA_CD", 15, 4, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_STA_CD", 19, 4, FieldType.ALPHA),
        new FieldSpec("TRANSM_MODE_CD_PP", 23, 1, FieldType.ALPHA),

        new FieldSpec("ACCT_PLAN_NAME", 24, 50, FieldType.ALPHA),
        new FieldSpec("STAT_CD", 74, 1, FieldType.ALPHA),
        new FieldSpec("STAT_QUAL", 75, 2, FieldType.ALPHA),

        new FieldSpec("ONL_LAST_ACT_C", 77, 1, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_MM", 78, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_DD", 80, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_YY", 82, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_CREATE_C", 84, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_MM", 85, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_DD", 87, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_YY", 89, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_VER_C", 91, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_MM", 92, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_DD", 94, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_YY", 96, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_DIST_C", 98, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_MM", 99, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_DD", 101, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_YY", 103, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_PP_RCP_C", 105, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_MM", 106, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_DD", 108, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_YY", 110, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CAN_C", 112, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_MM", 113, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_DD", 115, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_YY", 117, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_VER_C", 119, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_MM", 120, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_DD", 122, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_YY", 124, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_DIST_C", 126, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_MM", 127, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_DD", 129, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_YY", 131, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_RCPT_C", 133, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_MM", 134, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_DD", 136, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_YY", 138, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_CAN_C", 140, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_MM", 141, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_DD", 143, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_YY", 145, 2, FieldType.ALPHA),

        new FieldSpec("OOA_LMT_BNFT_IND", 147, 1, FieldType.ALPHA),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );

    /** FM56A – Plan Profile Update Fee Data */
    public static final List<FieldSpec> FM56A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("RCPT_INCUR_END_DT_C", 7, 1, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_MM", 8, 2, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_DD", 10, 2, FieldType.ALPHA),
        new FieldSpec("RCPT_INCUR_END_DT_YY", 12, 2, FieldType.ALPHA),

        // ---- PCPM RATE (SAME BYTES, TWO VIEWS) ----
        new FieldSpec("PCPM_RATE_X", 14, 13, FieldType.ALPHA),
        new FieldSpec("PCPM_RATE",   14, 13, FieldType.PACKED_DECIMAL, 2),

        new FieldSpec("INST_AEA_CD", 27, 1, FieldType.ALPHA),

        new FieldSpec("INST_AEA_NON_STD_AMT_X", 28, 5, FieldType.ALPHA),
        new FieldSpec("INST_AEA_NON_STD_AMT",   28, 5, FieldType.NUMERIC_TEXT),

        new FieldSpec("INST_ACCESS_FEE_CD", 33, 1, FieldType.ALPHA),

        new FieldSpec("PROF_AEA_CD", 34, 1, FieldType.ALPHA),

        new FieldSpec("PROF_AEA_NON_STD_AMT_X", 35, 5, FieldType.ALPHA),
        new FieldSpec("PROF_AEA_NON_STD_AMT",   35, 5, FieldType.NUMERIC_TEXT),

        new FieldSpec("PROF_ACCESS_FEE_CD", 40, 1, FieldType.ALPHA),

        new FieldSpec("NON_STD_ACC_FEE_PCT_X", 41, 7, FieldType.ALPHA),
        new FieldSpec("NON_STD_ACC_FEE_PCT",   41, 7, FieldType.NUMERIC_TEXT),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    /** FM57A – Plan Profile Update CHP Fee Type Data */
    public static final List<FieldSpec> FM57A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("LOC_PLAN_CD_01", 7, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_01", 10, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_02", 11, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_02", 14, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_03", 15, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_03", 18, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_04", 19, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_04", 22, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_05", 23, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_05", 26, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_06", 27, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_06", 30, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_07", 31, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_07", 34, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_08", 35, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_08", 38, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_09", 39, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_09", 42, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_10", 43, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_10", 46, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_11", 47, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_11", 50, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_12", 51, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_12", 54, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_13", 55, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_13", 58, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_14", 59, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_14", 62, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_15", 63, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_15", 66, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_16", 67, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_16", 70, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_17", 71, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_17", 74, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_18", 75, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_18", 78, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_19", 79, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_19", 82, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_20", 83, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_20", 86, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_21", 87, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_21", 90, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_22", 91, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_22", 94, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_23", 95, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_23", 98, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_24", 99, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_24", 102, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_25", 103, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_25", 106, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_26", 107, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_26", 110, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_27", 111, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_27", 114, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_28", 115, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_28", 118, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_29", 119, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_29", 122, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_30", 123, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_30", 126, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_31", 127, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_31", 130, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_32", 131, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_32", 134, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_33", 135, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_33", 138, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_34", 139, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_34", 142, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_35", 143, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_35", 146, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_36", 147, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_36", 150, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_37", 151, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_37", 154, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_38", 155, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_38", 158, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_39", 159, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_39", 162, 1, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CD_40", 163, 3, FieldType.ALPHA),
        new FieldSpec("CHP_FEE_ARRNGMNT_CD_40", 166, 1, FieldType.ALPHA),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 231, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 232, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 234, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 235, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 237, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 239, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 241, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 243, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 244, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 245, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 247, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 249, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 251, 2, FieldType.ALPHA)
    );
    
    /** FM59A – Plan Profile Update Trailer */
    public static final List<FieldSpec> FM59A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("TOT_HDR_REC_CNT", 7, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_CTL_REC_CNT", 12, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_SUB_REC_CNT", 17, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_DISP_REC_CNT", 22, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_ACCT_FEE_CTL_REC_CNT", 27, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_FEE_REC_CNT", 32, 5, FieldType.NUMERIC_TEXT),
        new FieldSpec("TOT_CHP_FEE_TYPE_REC_CNT", 37, 5, FieldType.NUMERIC_TEXT),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    /** FM61A – Plan Profile Acknowledgment Header */
    public static final List<FieldSpec> FM61A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("INSTALL_PLAN_CD", 7, 3, FieldType.ALPHA),
        new FieldSpec("DEST_PLAN_STA_CD", 10, 4, FieldType.ALPHA),
        new FieldSpec("INSTALL_PLAN_STA_CD", 14, 4, FieldType.ALPHA),

        new FieldSpec("BILL_CLM_TYPE", 18, 2, FieldType.ALPHA),
        new FieldSpec("TRANSM_MODE_CD_PP", 20, 1, FieldType.ALPHA),
        new FieldSpec("VER_NUM_PP_UPDT", 21, 3, FieldType.ALPHA),
        new FieldSpec("TRANS_IND", 24, 1, FieldType.ALPHA),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    /** FM62A – Plan Profile Acknowledgment Control */
    public static final List<FieldSpec> FM62A = of(
        new FieldSpec("TRANS_ID", 1, 2, FieldType.ALPHA),
        new FieldSpec("REC_TYPE", 3, 2, FieldType.ALPHA),
        new FieldSpec("SEQ_NUM", 5, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("EFF_END_DT_C", 7, 1, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_MM", 8, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_DD", 10, 2, FieldType.ALPHA),
        new FieldSpec("EFF_END_DT_YY", 12, 2, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_DT_CD", 14, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_STA_CD", 15, 4, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_STA_CD", 19, 4, FieldType.ALPHA),
        new FieldSpec("TRANSM_MODE_CD_PP", 23, 1, FieldType.ALPHA),

        new FieldSpec("ACCT_PLAN_NAME", 24, 50, FieldType.ALPHA),
        new FieldSpec("STAT_CD", 74, 1, FieldType.ALPHA),
        new FieldSpec("STAT_QUAL", 75, 2, FieldType.ALPHA),

        new FieldSpec("ONL_LAST_ACT_C", 77, 1, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_MM", 78, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_DD", 80, 2, FieldType.ALPHA),
        new FieldSpec("ONL_LAST_ACT_YY", 82, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_CREATE_C", 84, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_MM", 85, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_DD", 87, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CREATE_YY", 89, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_VER_C", 91, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_MM", 92, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_DD", 94, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_VER_YY", 96, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_DIST_C", 98, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_MM", 99, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_DD", 101, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_DIST_YY", 103, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_PP_RCP_C", 105, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_MM", 106, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_DD", 108, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_PP_RCP_YY", 110, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_CAN_C", 112, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_MM", 113, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_DD", 115, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CAN_YY", 117, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_VER_C", 119, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_MM", 120, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_DD", 122, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_VER_YY", 124, 2, FieldType.ALPHA),

        new FieldSpec("LOC_PLAN_DIST_C", 126, 1, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_MM", 127, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_DD", 129, 2, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_DIST_YY", 131, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_RCPT_C", 133, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_MM", 134, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_DD", 136, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_RCPT_YY", 138, 2, FieldType.ALPHA),

        new FieldSpec("CNTL_PLAN_CAN_C", 140, 1, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_MM", 141, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_DD", 143, 2, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CAN_YY", 145, 2, FieldType.ALPHA),

        new FieldSpec("OOA_LMT_BNFT_IND", 147, 1, FieldType.ALPHA),

        new FieldSpec("PRFX_ACCT_ID", 195, 20, FieldType.ALPHA),
        new FieldSpec("CNTL_PLAN_CD_KEY", 215, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_1", 218, 1, FieldType.ALPHA),
        new FieldSpec("SUB_PFX_ID", 219, 3, FieldType.ALPHA),
        new FieldSpec("LOC_PLAN_CD", 231, 3, FieldType.ALPHA),
        new FieldSpec("FUNC_CD_LVL_2", 234, 1, FieldType.ALPHA),
        new FieldSpec("CLM_TYPE", 235, 2, FieldType.ALPHA),

        new FieldSpec("EFF_ST_DT_C", 237, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_YY", 238, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_MM", 240, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("EFF_ST_DT_DD", 242, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("DT_SEQ_NUM", 244, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("FUNC_CD_LVL_3", 246, 1, FieldType.ALPHA),

        new FieldSpec("RCPT_INCUR_ST_C", 247, 1, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_YY", 248, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_MM", 250, 2, FieldType.NUMERIC_TEXT),
        new FieldSpec("RCPT_INCUR_ST_DD", 252, 2, FieldType.NUMERIC_TEXT),

        new FieldSpec("REC_TYPE_R", 254, 2, FieldType.ALPHA)
    );
    
    
}
