package com.its255.schema;

import static com.its255.schema.DispositionSchemas.FM21A;
import static com.its255.schema.DispositionSchemas.FM22A;
import static com.its255.schema.DispositionSchemas.FM22B;
import static com.its255.schema.DispositionSchemas.FM22C;
import static com.its255.schema.DispositionSchemas.FM22E;
import static com.its255.schema.DispositionSchemas.FM22F;
import static com.its255.schema.DispositionSchemas.FM22G;
import static com.its255.schema.DispositionSchemas.FM22H;
import static com.its255.schema.DispositionSchemas.FM23A;
import static com.its255.schema.DispositionSchemas.FM23C;
import static com.its255.schema.DispositionSchemas.FM23D;
import static com.its255.schema.DispositionSchemas.FM24A;
import static com.its255.schema.DispositionSchemas.FM24B;
import static com.its255.schema.DispositionSchemas.FM24C;
import static com.its255.schema.DispositionSchemas.FM24D;
import static com.its255.schema.DispositionSchemas.FM28A;
import static com.its255.schema.DispositionSchemas.FM29A;
import static com.its255.schema.SFInstitutionalSchemas.FM105;
import static com.its255.schema.SFInstitutionalSchemas.FM110;
import static com.its255.schema.SFInstitutionalSchemas.FM115;
import static com.its255.schema.SFInstitutionalSchemas.FM120;
import static com.its255.schema.SFInstitutionalSchemas.FM130;
import static com.its255.schema.SFInstitutionalSchemas.FM131;
import static com.its255.schema.SFInstitutionalSchemas.FM132;
import static com.its255.schema.SFInstitutionalSchemas.FM133;
import static com.its255.schema.SFInstitutionalSchemas.FM140;
import static com.its255.schema.SFInstitutionalSchemas.FM141;
import static com.its255.schema.SFInstitutionalSchemas.FM142;
import static com.its255.schema.SFInstitutionalSchemas.FM143;
import static com.its255.schema.SFInstitutionalSchemas.FM144;
import static com.its255.schema.SFInstitutionalSchemas.FM145;
import static com.its255.schema.SFInstitutionalSchemas.FM146;
import static com.its255.schema.SFInstitutionalSchemas.FM147;
import static com.its255.schema.SFInstitutionalSchemas.FM150;
import static com.its255.schema.SFInstitutionalSchemas.FM160;
import static com.its255.schema.SFInstitutionalSchemas.FM165;
import static com.its255.schema.SFInstitutionalSchemas.FM166;
import static com.its255.schema.SFInstitutionalSchemas.FM171;
import static com.its255.schema.SFInstitutionalSchemas.FM172;
import static com.its255.schema.SFInstitutionalSchemas.FM173;
import static com.its255.schema.SFInstitutionalSchemas.FM174;
import static com.its255.schema.SFInstitutionalSchemas.FM180;
import static com.its255.schema.SFInstitutionalSchemas.FM190;
import static com.its255.schema.SFInstitutionalSchemas.FM9D;
import static com.its255.schema.SFProfessionalSchemas.FM1A5;
import static com.its255.schema.SFProfessionalSchemas.FM1B0;
import static com.its255.schema.SFProfessionalSchemas.FM1B5;
import static com.its255.schema.SFProfessionalSchemas.FM1C0;
import static com.its255.schema.SFProfessionalSchemas.FM1D0;
import static com.its255.schema.SFProfessionalSchemas.FM1D1;
import static com.its255.schema.SFProfessionalSchemas.FM1D2;
import static com.its255.schema.SFProfessionalSchemas.FM1D3;
import static com.its255.schema.SFProfessionalSchemas.FM1E0;
import static com.its255.schema.SFProfessionalSchemas.FM1E1;
import static com.its255.schema.SFProfessionalSchemas.FM1E2;
import static com.its255.schema.SFProfessionalSchemas.FM1E6;
import static com.its255.schema.SFProfessionalSchemas.FM1F0;
import static com.its255.schema.SFProfessionalSchemas.FM1F1;
import static com.its255.schema.SFProfessionalSchemas.FM1F5;
import static com.its255.schema.SFProfessionalSchemas.FM1F6;
import static com.its255.schema.SFProfessionalSchemas.FM1G0;
import static com.its255.schema.SFProfessionalSchemas.FM1X0;

import static com.its255.schema.ValueBasedProgramSchemas.CBFBD;

import static com.its255.schema.PlanProfileSchemas.FM51A;
import static com.its255.schema.PlanProfileSchemas.FM52A;
import static com.its255.schema.PlanProfileSchemas.FM53A;
import static com.its255.schema.PlanProfileSchemas.FM54A;
import static com.its255.schema.PlanProfileSchemas.FM55A;
import static com.its255.schema.PlanProfileSchemas.FM56A;
import static com.its255.schema.PlanProfileSchemas.FM57A;
import static com.its255.schema.PlanProfileSchemas.FM59A;
import static com.its255.schema.PlanProfileSchemas.FM61A;
import static com.its255.schema.PlanProfileSchemas.FM62A;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SchemaRegistry {

    private static final Map<String, EnumMap<RecordType, List<FieldSpec>>> registry = new HashMap<>();

    private static final Map<String, Integer> recordLengths = new HashMap<>();

    static {
        registerAll();
    }

    public static List<FieldSpec> getSchema(String transactionType, String recordTypeCode) {
        RecordType recordType = RecordType.from(recordTypeCode);

        Map<RecordType, List<FieldSpec>> byTxn = registry.get(transactionType);

        if (byTxn == null) {
            throw new IllegalArgumentException(
                    "Unknown transaction type: " + transactionType);
        }

        List<FieldSpec> schema = byTxn.get(recordType);

        return schema;
    }

    private static void registerAll() {
        registerReconciliation();
        registerSF();
        registerDisposition();
        registerCapitatedBilling();
        registerVBPCBFBD();
        registerPlanProfileUpdate();
        registerPlanProfileAcknowledgment();
    }

    private static void registerCapitatedBilling() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);
        m.put(RecordType.RT_5A, CapitatedBillingSchemas.FM35A);
        m.put(RecordType.RT_6A, CapitatedBillingSchemas.FM36A);
        m.put(RecordType.RT_6B, CapitatedBillingSchemas.FM36B);
        m.put(RecordType.RT_7A, CapitatedBillingSchemas.FM37A);
        m.put(RecordType.RT_7B, CapitatedBillingSchemas.FM37B);
        m.put(RecordType.RT_9A, CapitatedBillingSchemas.FM39A);
        registry.put("CBF", m);
        recordLengths.put("CBF", 255);

    }

    private static void registerReconciliation() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        m.put(RecordType.RT_1A, ReconciliationSchemas.FM31A);
        m.put(RecordType.RT_2A, ReconciliationSchemas.FM32A);

        registry.put("RF", m);
        recordLengths.put("RF", 255);
    }

    private static void registerSF() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        // Institutional record types (05–9D)
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

        // Professional record types (A5–X0)
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

        // Register under "SF" (primary) + backward-compatible aliases
        registry.put("SF", m);
        registry.put("SFI", m);
        registry.put("SFP", m);
        recordLengths.put("SF", 255);
        recordLengths.put("SFI", 255);
        recordLengths.put("SFP", 255);
    }

    private static void registerDisposition() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        m.put(RecordType.RT_1A, FM21A);
        m.put(RecordType.RT_2A, FM22A);
        m.put(RecordType.RT_2B, FM22B);
        m.put(RecordType.RT_2C, FM22C);
        m.put(RecordType.RT_2E, FM22E);
        m.put(RecordType.RT_2F, FM22F);
        m.put(RecordType.RT_2G, FM22G);
        m.put(RecordType.RT_2H, FM22H);
        m.put(RecordType.RT_3A, FM23A);
        m.put(RecordType.RT_3C, FM23C);
        m.put(RecordType.RT_3D, FM23D);
        m.put(RecordType.RT_4A, FM24A);
        m.put(RecordType.RT_4B, FM24B);
        m.put(RecordType.RT_4C, FM24C);
        m.put(RecordType.RT_4D, FM24D);
        m.put(RecordType.RT_8A, FM28A);
        m.put(RecordType.RT_9A, FM29A);

        registry.put("DF", m);
        recordLengths.put("DF", 255);
    }

    private static void registerVBPCBFBD() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        m.put(RecordType.RT_CBFBD, CBFBD);
        registry.put("CBFBD", m);

        recordLengths.put("CBFBD", 804);
    }

    private static void registerPlanProfileUpdate() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        m.put(RecordType.RT_1A, FM51A);
        m.put(RecordType.RT_2A, FM52A);
        m.put(RecordType.RT_3A, FM53A);
        m.put(RecordType.RT_4A, FM54A);
        m.put(RecordType.RT_5A, FM55A);
        m.put(RecordType.RT_6A, FM56A);
        m.put(RecordType.RT_7A, FM57A);
        m.put(RecordType.RT_9A, FM59A);
        m.put(RecordType.RT_61, FM61A);
        m.put(RecordType.RT_62, FM62A);

        registry.put("PPU", m);
        recordLengths.put("PPU", 255);
    }

    private static void registerPlanProfileAcknowledgment() {
        EnumMap<RecordType, List<FieldSpec>> m = new EnumMap<>(RecordType.class);

        m.put(RecordType.RT_1A, FM61A);
        m.put(RecordType.RT_2A, FM62A);

        registry.put("PPA", m);
        recordLengths.put("PPA", 255);
    }

    public static Map<RecordType, List<FieldSpec>> all() {
        EnumMap<RecordType, List<FieldSpec>> allSchemas = new EnumMap<>(RecordType.class);

        for (EnumMap<RecordType, List<FieldSpec>> perTxn : registry.values()) {
            for (Map.Entry<RecordType, List<FieldSpec>> entry : perTxn.entrySet()) {
                allSchemas.put(entry.getKey(), entry.getValue());
            }
        }

        return allSchemas;
    }

    public static int getRecordLength(String transactionType) {
        Integer len = recordLengths.getOrDefault(transactionType, 255);
        return len;
    }
}