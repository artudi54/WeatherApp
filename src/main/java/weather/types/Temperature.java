package main.java.weather.types;

import java.util.Objects;

public class Temperature {
    public static final Temperature NA = new Temperature(Double.MIN_VALUE, UnitsFormat.DEFAULT);

    private double value;
    private UnitsFormat unitsFormat;

    public Temperature(double value, UnitsFormat unitsFormat) {
        this.value = value;
        this.unitsFormat = Objects.requireNonNull(unitsFormat, "unitsFormat cannot be null");
    }

    public double getValue() {
        return value;
    }

    public UnitsFormat getUnitsFormat() {
        return unitsFormat;
    }

    private String appendix() {
        if (unitsFormat == null)
            throw new NullPointerException("unitsFormat cannot be null");
        switch (unitsFormat) {
            case DEFAULT:
                return " K";
            case IMPERIAL:
                return " °F";
            case METRIC:
                return " °C";
        }
        return null;
    }

    @Override
    public String toString() {
        if (value == Double.MIN_VALUE)
            return "N/A";
        return value + appendix();
    }
}
