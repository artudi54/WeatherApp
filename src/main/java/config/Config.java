package main.java.config;

import main.java.exception.ConfigException;
import main.java.weather.types.Coordinates;
import main.java.weather.types.UnitsFormat;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.ini4j.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.spec.ECField;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Config {
    private static final String WEATHER_SECTION = "Weather";
    private static final String LAST_CITY_NAME = "LastCityName";
    private static final String LAST_COORDINATES = "LastCoordinates";
    private static final String LAST_CITY_ID = "LastCityId";
    private static final String UNITS_FORMAT = "UnitsFormat";
    private static final String WEATHER_API_KEY = "WeatherApiKey";
    private static final String SAVE_LAST_INPUT = "SaveLastInput";

    private String lastCityName = "";
    private Coordinates lastCoordinates = null;
    private int lastCityId = 0;
    private UnitsFormat unitsFormat = UnitsFormat.METRIC;
    private String weatherApiKey = "b7e86275bda27422cd3181e6419a42a7";
    private boolean saveLastInput = true;

    public String getLastCityName() {
        return lastCityName;
    }

    public void setLastCityName(String lastCityName) {
        this.lastCityName = lastCityName;
    }

    public Coordinates getLastCoordinates() {
        return lastCoordinates;
    }

    public void setLastCoordinates(Coordinates lastCoordinates) {
        this.lastCoordinates = lastCoordinates;
    }

    public int getLastCityId() {
        return lastCityId;
    }

    public void setLastCityId(int lastCityId) {
        this.lastCityId = lastCityId;
    }

    public UnitsFormat getUnitsFormat() {
        return unitsFormat;
    }

    public void setUnitsFormat(UnitsFormat unitsFormat) {
        this.unitsFormat = unitsFormat;
    }

    public String getWeatherApiKey() {
        return weatherApiKey;
    }

    public void setWeatherApiKey(String weatherApiKey) {
        this.weatherApiKey = weatherApiKey;
    }

    public boolean isSaveLastInput() {
        return saveLastInput;
    }

    public void setSaveLastInput(boolean saveLastInput) {
        this.saveLastInput = saveLastInput;
    }


    private void parseIni(Ini ini) {
        Objects.requireNonNull(ini, "ini cannot be null");
        Preferences preferences = new IniPreferences(ini);
        lastCityName = preferences.node(WEATHER_SECTION).get(LAST_CITY_NAME, "");
        lastCoordinates = Coordinates.fromString(preferences.node(WEATHER_SECTION).get(LAST_COORDINATES, ""));
        lastCityId = Integer.parseInt(preferences.node(WEATHER_SECTION).get(LAST_CITY_ID, "0"));
        unitsFormat = UnitsFormat.fromString(preferences.node(WEATHER_SECTION).get(UNITS_FORMAT, "metric"));
        weatherApiKey = preferences.node(WEATHER_SECTION).get(WEATHER_API_KEY, weatherApiKey);
        saveLastInput = Boolean.parseBoolean(preferences.node(WEATHER_SECTION).get(SAVE_LAST_INPUT, "true"));
    }

    private void fillIni(Ini ini) {
        org.ini4j.Config iniConfig = new org.ini4j.Config();
        iniConfig.setStrictOperator(true);
        iniConfig.setEmptyOption(true);
        ini.setConfig(iniConfig);

        ini.put(WEATHER_SECTION, LAST_CITY_NAME, lastCityName);
        ini.put(WEATHER_SECTION, LAST_COORDINATES, lastCoordinates);
        ini.put(WEATHER_SECTION, LAST_CITY_ID, lastCityId);
        ini.put(WEATHER_SECTION, UNITS_FORMAT, unitsFormat);
        ini.put(WEATHER_SECTION, WEATHER_API_KEY, weatherApiKey);
        ini.put(WEATHER_SECTION, SAVE_LAST_INPUT, saveLastInput);
    }

    public static Config loadFrom(Path iniPath) throws ConfigException {
        Objects.requireNonNull(iniPath, "iniPath cannot be null");

        Config config = new Config();
        try {
            if (!Files.exists(iniPath))
                Files.createFile(iniPath);
            Ini ini = new Ini(iniPath.toFile());
            config.parseIni(ini);
            return config;
        }



        catch (InvalidFileFormatException ignore) {
            throw new ConfigException(iniPath, ConfigException.Type.PARSE);
        }
        catch (IOException ignore) {
            throw new ConfigException(iniPath, ConfigException.Type.READ);
        }
    }

    public void saveTo(Path iniPath) throws ConfigException {
        Objects.requireNonNull(iniPath, "iniPath cannot be null");

        try {
            if (!Files.exists(iniPath))
                Files.createFile(iniPath);
            Ini ini = new Ini(iniPath.toFile());
            fillIni(ini);
            ini.store();
        }

        catch (InvalidFileFormatException ignore) {
            throw new ConfigException(iniPath, ConfigException.Type.PARSE);
        }
        catch (IOException ignore) {
            throw new ConfigException(iniPath, ConfigException.Type.WRITE);
        }
    }
}
