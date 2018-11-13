package main.java.weather.types;

import java.security.InvalidParameterException;
import java.util.Objects;

public class Wind {
    private double speed;
    private double degree;
    private UnitsFormat unitsFormat;

    public Wind(double speed, double degree, UnitsFormat unitsFormat) {
        if (speed < 0)
            throw new InvalidParameterException("speed cannot be negative");
        if (degree != -1 && (degree < 0 || degree > 360))
            throw new InvalidParameterException("invalid degree value");

        this.speed = speed;
        this.degree = degree;
        this.unitsFormat = Objects.requireNonNull(unitsFormat, "unitsFormat cannot be null");
    }

    public double getSpeed() {
        return speed;
    }

    public boolean hasDegree() {
        return degree != -1;
    }

    public double getDegree() {
        return degree;
    }

    public UnitsFormat getUnitsFormat() {
        return unitsFormat;
    }

    public String getDirection() {
        if (degree > 337.5  || degree < 22.5)
            return "Northerly";
        if (degree < 67.5)
            return "North-easterly";
        if (degree < 112.5)
            return "East";
        if (degree < 157.5)
            return "South-easterly";
        if (degree < 202.5)
            return "Southerly";
        if (degree < 247.5)
            return "South-westerly";
        if (degree < 292.5)
            return "Westerly";
        return "North-westerly";
    }

    private String speedAppendix() {
        switch (unitsFormat) {
            case DEFAULT:
            case METRIC:
                return " m/s";
            case IMPERIAL:
                return " mps";
        }

        return null;
    }

    @Override
    public String toString() {
        if (degree != -1)
            return speed + speedAppendix() + ", " + getDirection() + " (" + degree + " °)";
        return speed + speedAppendix();
    }
}
