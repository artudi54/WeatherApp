package main.java.weather.types;

import java.util.Comparator;
import java.util.Objects;

public class Country implements Comparable<Country> {
    private static final Comparator<Country> COMPARATOR = Comparator.comparing(Country::getName)
                                                                    .thenComparing(Country::getIso2Code);

    private String iso2Code;
    private String name;

    public Country(String iso2Code, String name) {
        this.iso2Code = Objects.requireNonNull(iso2Code, "iso2Code cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Country))
            return false;
        Country other = (Country)obj;
        return iso2Code.equals(other.iso2Code) &&
               name.equals(other.name);
    }


    @Override
    public int compareTo(Country other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public String toString() {
        return name + ',' + iso2Code;
    }
}
