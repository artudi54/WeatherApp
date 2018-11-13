package main.java.weather.types;

import java.security.InvalidParameterException;

public class Pressure {
    public static final Pressure NA = new Pressure(-1.0);
    private double value;

    public Pressure(double value) {
        if (value != -1 && value < 0)
            throw new InvalidParameterException("pressure cannot be negative");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == -1.0)
            return "N/A";
        return value + " hPa";
    }
}
