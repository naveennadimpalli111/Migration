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
    RT_41("41"),
    RT_42("42"),
    RT_43("43"),
    RT_44("44"),
    RT_45("45"),
    RT_46("46"),
    RT_47("47"),
    RT_50("50"),
    RT_60("60"),
    RT_65("65"),
    RT_66("66"),
    RT_71("71"),
    RT_72("72"),
    RT_73("73"),
    RT_74("74"),
    RT_80("80"),
    RT_90("90"),
    RT_9D("9D"),
    UNKNOWN("??");

    public final String code;
    private static final Map<String, RecordType> BY_CODE = new HashMap<>();
    static { for (RecordType r : values()) BY_CODE.put(r.code, r); }

    RecordType(String code) { this.code = code; }
    public static RecordType from(String code) { return BY_CODE.getOrDefault(code, UNKNOWN); }
}