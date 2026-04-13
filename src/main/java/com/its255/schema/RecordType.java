package com.its255.schema;

import java.util.HashMap;
import java.util.Map;

public enum RecordType {
	RT_05("05"),
	RT_10("10"),
    RT_15("15"),
    RT_20("20"),
    RT_30("30"),
    RT_31("31"),
    RT_32("32"),
    RT_33("33"),
    RT_40("40"),
    RT_40D("40D"),
    RT_41("41"),
    RT_41A("41A"),
    RT_41B("41B"),
    RT_41C("41C"),
    RT_42("42"),
    RT_42A("42A"),
    RT_42C("42C"),
    RT_43("43"),
    RT_43A("43A"),
    RT_43B("43B"),
    RT_43C("43C"),
    RT_44("44"),
    RT_45("45"),
    RT_46("46"),
    RT_47("47"),
    RT_49A("49A"),
    RT_50("50"),
    RT_60("60"),
    RT_65("65"),
    RT_66("66"),
    RT_71("71"),
    RT_72("72"),
    RT_73("73"),
    RT_74("74"),
    RT_80("80"),
    RT_81("81"),
    RT_82("82"),
    RT_83("83"),
    RT_84("84"),
    RT_90("90"),
    RT_9D("9D"),
    RT_A5("A5"),
    RT_B0("B0"),
    RT_B5("B5"),
    RT_C0("C0"),
    RT_D0("D0"),
    RT_D1("D1"),
    RT_D2("D2"),
    RT_D3("D3"),
    RT_E0("E0"),
    RT_E1("E1"),
    RT_E2("E2"),
    RT_E6("E6"),
    RT_F0("F0"),
    RT_F1("F1"),
    RT_F5("F5"),
    RT_F6("F6"),
    RT_G0("G0"),
    RT_X0("X0"),
    RT_1A("1A"),
    RT_2A("2A"),
    RT_2B("2B"),
    RT_2C("2C"),
    RT_2E("2E"),
    RT_2F("2F"),
    RT_2G("2G"),
    RT_2H("2H"),
    RT_3A("3A"),
    RT_3C("3C"),
    RT_3D("3D"),
    RT_4A("4A"),
    RT_4B("4B"),
    RT_4C("4C"),
    RT_4D("4D"),
    RT_8A("8A"),
    RT_9A("9A"),
    RT_5A("5A"),    // CBF → FM35A
    RT_6A("6A"),    // CBF → FM36A
    RT_6B("6B"),    // CBF → FM36B
    RT_7A("7A"),    // CBF → FM37A
    RT_7B("7B"),    // CBF → FM37B 
    RT_CBFBD("CBFBD"),
    UNKNOWN("??");


    public final String code;
    private static final Map<String, RecordType> BY_CODE = new HashMap<>();
    static { for (RecordType r : values()) BY_CODE.put(r.code, r); }

    RecordType(String code) { this.code = code; }
    public static RecordType from(String code) { return BY_CODE.getOrDefault(code, UNKNOWN); }
}