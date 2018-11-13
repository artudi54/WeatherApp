package main.java.config;

import main.java.exception.ConfigException;
import main.java.weather.types.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Countries {
    private Map<String, Country> countriesByIso2Code;
    private Map<String, Country> countriesByName;

    public Countries(Path countryListJson) throws ConfigException {
        countriesByIso2Code = new TreeMap<>();
        countriesByName = new TreeMap<>();
        parseCountryList(countryListJson);
    }

    public Country getCountryByIso2Code(String iso2Code) {
        return countriesByIso2Code.get(iso2Code);
    }

    public Country getCountryByName(String countryName) {
        return countriesByName.get(countryName);
    }

    private void parseCountryList(Path countryListJson) throws ConfigException {
        Objects.requireNonNull(countryListJson, "countryListJson cannot be null");

        JSONParser parser = new JSONParser();
        try {
            String contents = new String(Files.readAllBytes(countryListJson));
            JSONArray array = (JSONArray)parser.parse(contents);

            for (Object obj : array) {
                JSONObject jsonObject = (JSONObject)obj;
                String iso2Code = (String)jsonObject.get("Code");
                String name = (String)jsonObject.get("Name");
                Country country = new Country(iso2Code, name);
                countriesByIso2Code.put(iso2Code, country);
                countriesByName.put(name, country);
            }
        }
        catch (ParseException | ClassCastException ignore) {
            throw new ConfigException(countryListJson, ConfigException.Type.PARSE);
        }
        catch (IOException ignore) {
            throw new ConfigException(countryListJson, ConfigException.Type.READ);
        }
    }
}
