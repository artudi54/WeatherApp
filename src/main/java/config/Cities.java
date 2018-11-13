package main.java.config;

import main.java.exception.ConfigException;
import main.java.weather.types.City;
import main.java.weather.types.Coordinates;
import main.java.weather.types.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Cities {
    private Set<City> cities;
    private Map<Integer, City> citiesById;

    public Cities(Path cityListJson, Countries countries) throws ConfigException  {
        cities = new TreeSet<>();
        citiesById = new TreeMap<>();
        parseCityList(cityListJson, countries);
    }

    public City getClosestCity(Coordinates coordinates) {
        if (cities.isEmpty())
            return null;

        City closest = cities.iterator().next();
        for (City city : cities) {
            if (coordinates.distanceSq(city.getCoordinates()) < coordinates.distanceSq(closest.getCoordinates()))
                closest = city;
        }
        return closest;
    }

    public City getCityById(int id) {
        return citiesById.get(id);
    }

    public List<String> asCityList() {
        return cities.stream()
                .map(city -> city.getName() + ',' + city.getCountry().getIso2Code())
                .distinct()
                .collect(Collectors.toList());
    }


    private void parseCityList(Path cityListJson, Countries countries) throws ConfigException {
        Objects.requireNonNull(cityListJson, "cityListJson cannot be null");
        Objects.requireNonNull(countries, "countries cannot be null");

        JSONParser parser = new JSONParser();
        try {
            String contents = new String(Files.readAllBytes(cityListJson));
            JSONArray array = (JSONArray)parser.parse(contents);

            for (Object obj : array) {
                JSONObject jsonObject = (JSONObject)obj;
                JSONObject coordinatesObject = (JSONObject)jsonObject.get("coord");

                int id = ((Number)jsonObject.get("id")).intValue();
                String name = (String)jsonObject.get("name");
                Country country = countries.getCountryByIso2Code((String)jsonObject.get("country"));
                Coordinates coordinates = new Coordinates(
                        ((Number)coordinatesObject.get("lat")).doubleValue(),
                        ((Number)coordinatesObject.get("lon")).doubleValue()
                );
                City city = new City(id, name, country, coordinates);
                cities.add(city);
                citiesById.put(city.getId(), city);
            }
        }
        catch (ParseException | ClassCastException ignore) {
            throw new ConfigException(cityListJson, ConfigException.Type.PARSE);
        }
        catch (IOException ignore) {
            throw new ConfigException(cityListJson, ConfigException.Type.READ);
        }
    }
}
