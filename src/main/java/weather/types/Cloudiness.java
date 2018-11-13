package main.java.weather.types;

import java.security.InvalidParameterException;

public class Cloudiness {
    public static final Cloudiness NA = new Cloudiness(-1.0);
    private double value;

    public Cloudiness(double value) {
        if (value > 100 || (value != -1 && value < 0))
            throw new InvalidParameterException("invalid cloudiness value");
        this.value = value;
    }

    public boolean hasValue() {
        return value != -1;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == -1)
            return "N/A";
        return value + " %";
    }
}
