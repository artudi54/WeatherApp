package main.java.config;

import main.java.exception.ConfigException;

import java.nio.file.Path;
import java.util.Objects;

public class WorldInformation {
    private Countries countries;
    private Cities cities;

    public WorldInformation(Path countryListJson, Path cityListJson) throws ConfigException {
        Objects.requireNonNull(countryListJson, "countryListJson cannot be null");
        Objects.requireNonNull(cityListJson, "cityListJson cannot be null");
        countries = new Countries(countryListJson);
        cities = new Cities(cityListJson, countries);
    }

    public Countries getCountries() {
        return countries;
    }

    public Cities getCities() {
        return cities;
    }
}
