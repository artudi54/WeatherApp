package main.java.weather.types;

import java.security.InvalidParameterException;
import java.util.Comparator;

public class Coordinates implements Comparable<Coordinates> {
    private static final Comparator<Coordinates> COMPARATOR = Comparator.comparing(Coordinates::getLatitude)
                                                                                .thenComparing(Coordinates::getLongitude);

    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distanceSq(Coordinates other) {
        return (latitude - other.latitude) * (latitude - other.latitude) +
               (longitude - other.longitude) * (longitude - other.longitude);
    }

    public double distance(Coordinates other) {
        return Math.sqrt(distanceSq(other));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinates))
            return false;
        Coordinates other = (Coordinates)obj;
        return latitude == other.latitude && longitude == other.longitude;
    }

    @Override
    public int compareTo(Coordinates other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%.3f, %.3f",latitude, longitude);
    }

    public static Coordinates fromString(String value) {
        if (value == null || value.equals(""))
            return null;

        String[] coords = value.split(", ");
        if (coords.length != 2)
            return null;

        return new Coordinates(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
    }
}
