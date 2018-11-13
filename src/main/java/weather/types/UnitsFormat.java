package main.java.weather.types;

import java.util.HashMap;
import java.util.Map;

public enum UnitsFormat {
    DEFAULT(""),
    IMPERIAL("imperial"),
    METRIC("metric");

    private String name;

    UnitsFormat(String name) {
        this.name = name;
    }

    public static UnitsFormat fromString(String name) {
        return STRING_UNITS_FORMAT_MAP.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    private static final Map<String, UnitsFormat> STRING_UNITS_FORMAT_MAP;
    static {
        STRING_UNITS_FORMAT_MAP = new HashMap<>();
        STRING_UNITS_FORMAT_MAP.put(DEFAULT.name, DEFAULT);
        STRING_UNITS_FORMAT_MAP.put(IMPERIAL.name, IMPERIAL);
        STRING_UNITS_FORMAT_MAP.put(METRIC.name, METRIC);
    }
}
