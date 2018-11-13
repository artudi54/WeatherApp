package main.java.networking;

import main.java.AppUtils;
import main.java.config.Config;
import main.java.exception.WeatherRequestException;
import main.java.weather.types.Coordinates;

import java.io.Closeable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherRequestMaker implements Closeable {
    private static final String CURRENT_PREFIX = "https://api.openweathermap.org/data/2.5/weather";
    private static final String THREE_HOUR_FORECAST_PREFIX = "https://api.openweathermap.org/data/2.5/forecast";

    private Config config;
    private ExecutorService executorService;

    public WeatherRequestMaker(Config config) {
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.executorService = Executors.newSingleThreadExecutor();
    }

    private WeatherRequest weatherById(String prefix, int id) throws WeatherRequestException {
        String urlStr = String.format(
                "%s?id=%d&units=%s&APPID=%s",
                prefix, id, config.getUnitsFormat(), config.getWeatherApiKey());
        try {
            return new WeatherRequest(new URL(urlStr), executorService);
        }
        catch (MalformedURLException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private WeatherRequest weatherByCity(String prefix, String cityName) throws WeatherRequestException {
        String urlStr = String.format(
                "%s?q=%s&units=%s&APPID=%s",
                prefix, cityName, config.getUnitsFormat(), config.getWeatherApiKey());
        try {
            return new WeatherRequest(new URL(urlStr), executorService);
        }
        catch (MalformedURLException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private WeatherRequest weatherByCity(String prefix, String cityName, String countryIso2Code) throws WeatherRequestException {
        String urlStr = String.format(
                "%s?q=%s,%s&units=%s&APPID=%s",
                prefix, cityName, countryIso2Code, config.getUnitsFormat(), config.getWeatherApiKey());
        try {
            return new WeatherRequest(new URL(urlStr), executorService);
        }
        catch (MalformedURLException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private WeatherRequest weatherByCoordinates(String prefix, Coordinates coordinates) throws WeatherRequestException {
        Objects.requireNonNull(coordinates, "coordinates cannot be null");
        String urlStr = String.format(
                "%s?lat=%f&lon=%f&units=%s&APPID=%s",
                prefix, coordinates.getLatitude(), coordinates.getLongitude(), config.getUnitsFormat(), config.getWeatherApiKey());
        try {
            return new WeatherRequest(new URL(urlStr), executorService);
        }
        catch (MalformedURLException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public WeatherRequest currentWeatherById(int id) throws WeatherRequestException {
        return weatherById(CURRENT_PREFIX, id);
    }

    public WeatherRequest currentWeatherByCity(String cityName) throws WeatherRequestException {
        return weatherByCity(CURRENT_PREFIX, cityName);
    }
    public WeatherRequest currentWeatherByCity(String cityName, String countryIso2Code) throws WeatherRequestException {
        return weatherByCity(CURRENT_PREFIX, cityName, countryIso2Code);
    }

    public WeatherRequest currentWeatherByCoordiantes(Coordinates coordinates) throws WeatherRequestException {
        return weatherByCoordinates(CURRENT_PREFIX, coordinates);
    }

    public WeatherRequest threeHourWeatherForecastById(int id) throws WeatherRequestException {
        return weatherById(THREE_HOUR_FORECAST_PREFIX, id);
    }

    public WeatherRequest threeHourWeatherForecastByCity(String cityName) throws WeatherRequestException {
        return weatherByCity(THREE_HOUR_FORECAST_PREFIX, cityName);
    }

    public WeatherRequest threeHourWeatherForecastByCity(String cityName, String countryIso2Code) throws WeatherRequestException {
        return weatherByCity(THREE_HOUR_FORECAST_PREFIX, cityName, countryIso2Code);
    }

    public WeatherRequest threeHourWeatherForecastByCoordiantes(Coordinates coordinates) throws WeatherRequestException {
        return weatherByCoordinates(THREE_HOUR_FORECAST_PREFIX, coordinates);
    }

    @Override
    public void close() {
        AppUtils.closeExecutorService(executorService);
    }
}
