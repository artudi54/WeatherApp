package main.java.weather.types;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.Objects;

public class City implements Comparable<City> {
    private static final Comparator<City> COMPARATOR =Comparator.comparing(City::getName)
                                                                .thenComparing(City::getCountry)
                                                                .thenComparing(City::getCoordinates)
                                                                .thenComparing(City::getId);
    private int id;
    private String name;
    private Country country;
    private Coordinates coordinates;

    public City(int id, String name, Country country, Coordinates coordinates) {
        if (id < 0)
            throw new InvalidParameterException("id cannot be negative");
        this.id = id;
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.country = Objects.requireNonNull(country, "country cannot be null");
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates cannot be null");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof City))
            return false;
        City other = (City)obj;
        return id == other.id;
    }

    @Override
    public int compareTo(City other) {
        if (id == other.id)
            return 0;
        return COMPARATOR.compare(this, other);
    }
}
