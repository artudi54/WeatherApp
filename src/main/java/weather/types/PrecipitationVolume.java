package main.java.weather.types;

import java.security.InvalidParameterException;

public class PrecipitationVolume {
    public static final PrecipitationVolume ZERO = new PrecipitationVolume(0.0);

    private double value;

    public PrecipitationVolume(double value) {
        if (value < 0)
            throw new InvalidParameterException("precipitation volume cannot be negative");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.2f", value) + " mm";
    }
}
