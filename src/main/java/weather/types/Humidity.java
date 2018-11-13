package main.java.weather.types;

import java.security.InvalidParameterException;

public class Humidity {
    public static final Humidity NA = new Humidity(-1.0);
    private double value;

    public Humidity(double value) {
        if (value != -1 && (value < 0 || value > 100))
            throw new InvalidParameterException("invalid humidity value");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == -1.0)
            return "N/A";
        return value + " %";
    }
}
