package main.java.parser;

import main.java.config.WorldInformation;
import main.java.exception.WeatherResponseParseException;
import main.java.networking.WeatherResponse;
import main.java.weather.condition.CurrentWeatherCondition;
import main.java.weather.condition.ThreeHourWeatherForecast;
import main.java.weather.condition.WeatherCondition;
import main.java.weather.types.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherJSONParser {
    private final WorldInformation worldInformation;

    public WeatherJSONParser(WorldInformation worldInformation) {
        this.worldInformation = Objects.requireNonNull(worldInformation, "worldInformation cannot be null");
    }
    private int parseInteger(JSONObject object, String name) {
        return ((Number)object.get(name)).intValue();
    }

    private double parseDouble(JSONObject object, String name) {
        return ((Number)object.get(name)).doubleValue();
    }

    private WeatherDescription parseWeatherDescription(JSONArray array) {
        JSONObject object = (JSONObject)array.get(0);
        return new WeatherDescription(
                (String)object.get("main"),
                (String)object.get("description"),
                (String)object.get("icon"));
    }

    private Temperature parseTemperature(JSONObject object, UnitsFormat unitsFormat) {
        if (object == null || !object.containsKey("temp"))
            return Temperature.NA;
        return new Temperature(parseDouble(object, "temp"), unitsFormat);
    }

    private Temperature parseMinimalTemperature(JSONObject object, UnitsFormat unitsFormat) {
        if (object == null || !object.containsKey("temp_min"))
            return Temperature.NA;
        return new Temperature(parseDouble(object, "temp_min"), unitsFormat);
    }

    private Temperature parseMaximalTemperature(JSONObject object, UnitsFormat unitsFormat) {
        if (object == null || !object.containsKey("temp_max"))
            return Temperature.NA;
        return new Temperature(parseDouble(object, "temp_max"), unitsFormat);
    }

    private Pressure parsePressure(JSONObject object) {
        if (object == null || !object.containsKey("pressure"))
            return Pressure.NA;
        return new Pressure(parseDouble(object, "pressure"));
    }

    private Pressure parseGroundPressure(JSONObject object) {
        if (object == null || !object.containsKey("grnd_level"))
            return Pressure.NA;
        return new Pressure(parseDouble(object, "grnd_level"));
    }

    private Pressure parseSeaPressure(JSONObject object) {
        if (object == null || !object.containsKey("sea_level"))
            return Pressure.NA;
        return new Pressure(parseDouble(object, "sea_level"));
    }

    private Humidity parseHumidity(JSONObject object) {
        if (object == null || !object.containsKey("humidity"))
            return Humidity.NA;
        return new Humidity(parseDouble(object, "humidity"));
    }

    private Wind parseWind(JSONObject object, UnitsFormat unitsFormat) {
        if (object.containsKey("deg"))
            return new Wind(
                parseDouble(object, "speed"),
                parseDouble(object, "deg"),
                unitsFormat);
        return new Wind(
                parseDouble(object, "speed"),
                -1,
                unitsFormat);
    }

    private Cloudiness parseCloudiness(JSONObject object) {
        if (object == null || !object.containsKey("all"))
            return Cloudiness.NA;
        return new Cloudiness(parseDouble(object, "all"));
    }

    private PrecipitationVolume parsePrecipitationVolume(JSONObject object) {
        if (object == null || !object.containsKey("3h"))
            return PrecipitationVolume.ZERO;
        return new PrecipitationVolume(parseDouble(object, "3h"));
    }

    private SunTime parseSunrise(JSONObject object) {
        if (object == null || !object.containsKey("sunrise"))
            return SunTime.NA;
        return new SunTime(parseInteger(object, "sunrise"));
    }

    private SunTime parseSunset(JSONObject object) {
        if (object == null || !object.containsKey("sunset"))
            return SunTime.NA;
        return new SunTime(parseInteger(object, "sunset"));
    }

    private ConditionDate parseCalculationDate(JSONObject object) {
        return new ConditionDate(parseInteger(object, "dt"));
    }

    private Coordinates parseCoordinates(JSONObject object) {
        return new Coordinates(parseDouble(object, "lat"),
                               parseDouble(object, "lon"));
    }

    private City parseCity(JSONObject object) {
        int id = parseInteger(object, "id");
        String name = (String)object.get("name");
        Country country = worldInformation.getCountries().getCountryByIso2Code((String)object.get("country"));
        Coordinates coordinates = parseCoordinates((JSONObject)object.get("coord"));
        return new City(id, name, country, coordinates);
    }

    private WeatherCondition parseWeatherCondition(JSONObject object, UnitsFormat unitsFormat) {
        JSONObject mainObject = (JSONObject)object.get("main");
        JSONObject sysObject = (JSONObject)object.get("sys");
        JSONObject rainObject = (JSONObject)object.get("rain");
        JSONObject snowObject = (JSONObject)object.get("snow");

        WeatherDescription weatherDescription = parseWeatherDescription((JSONArray)object.get("weather"));
        Temperature temperature = parseTemperature(mainObject, unitsFormat);
        Temperature minimalTemperature = parseMinimalTemperature(mainObject, unitsFormat);
        Temperature maximalTemperature = parseMaximalTemperature(mainObject, unitsFormat);
        Pressure pressure = parsePressure(mainObject);
        Pressure groundPressure = parseGroundPressure(mainObject);
        Pressure seaPressure = parseSeaPressure(mainObject);
        Humidity humidity = parseHumidity(mainObject);
        Wind wind = parseWind((JSONObject)object.get("wind"), unitsFormat);
        Cloudiness cloudiness = parseCloudiness((JSONObject)object.get("clouds"));
        PrecipitationVolume rainfallVolume = parsePrecipitationVolume(rainObject);
        PrecipitationVolume snowfallVolume = parsePrecipitationVolume(snowObject);
        SunTime sunrise = parseSunrise(sysObject);
        SunTime sunset = parseSunset(sysObject);
        ConditionDate conditionDate = parseCalculationDate(object);

        return WeatherCondition.builder()
                .weatherDescription(weatherDescription)
                .temperature(temperature)
                .minimalTemperature(minimalTemperature)
                .maximalTemperature(maximalTemperature)
                .pressure(pressure)
                .groundPressure(groundPressure)
                .seaPressure(seaPressure)
                .humidity(humidity)
                .wind(wind)
                .cloudiness(cloudiness)
                .rainfallVolume(rainfallVolume)
                .snowfallVolume(snowfallVolume)
                .sunrise(sunrise)
                .sunset(sunset)
                .calculationDate(conditionDate)
                .build();
    }

    public CurrentWeatherCondition parseCurrentWeatherCondition(WeatherResponse response) throws WeatherResponseParseException {
        Objects.requireNonNull(response, "response cannot be null");
        try {
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject)parser.parse(response.getValue());

            WeatherCondition  weatherCondition = parseWeatherCondition(responseObject, response.getUnitsFormat());

            int id = ((Number)responseObject.get("id")).intValue();
            City city = worldInformation.getCities().getCityById(id);
            if (city == null)
                throw new Exception("parse error - city is unknown");

            return new CurrentWeatherCondition(weatherCondition, city);
        }
        catch (Exception exc) {
            throw new WeatherResponseParseException(ForecastType.CURRENT_WEATHER, exc.getMessage());
        }
    }

    public ThreeHourWeatherForecast parseThreeHourForecast(WeatherResponse response)  throws WeatherResponseParseException  {
        Objects.requireNonNull(response, "response cannot be null");
        try {
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject)parser.parse(response.getValue());
            JSONArray responseList = (JSONArray)responseObject.get("list");

            int conditionCount = parseInteger(responseObject, "cnt");

            List<WeatherCondition> weatherConditions = new ArrayList<>(conditionCount);
            for (Object obj : responseList) {
                JSONObject weatherConditionObject = (JSONObject)obj;
                weatherConditions.add(parseWeatherCondition(weatherConditionObject, response.getUnitsFormat()));
            }

            City city = parseCity((JSONObject)responseObject.get("city"));

            return new ThreeHourWeatherForecast(city, weatherConditions);
        }
        catch (Exception exc) {
            throw new WeatherResponseParseException(ForecastType.THREE_HOUR_FORECAST, exc.getMessage());
        }
    }
}
