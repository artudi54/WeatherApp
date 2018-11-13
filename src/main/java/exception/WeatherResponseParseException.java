package main.java.exception;

import main.java.weather.types.ForecastType;

import java.io.IOException;
import java.util.Objects;

public class WeatherResponseParseException extends IOException {
    public WeatherResponseParseException(ForecastType forecastType, String message) {
        super(makeDescription(forecastType, message));
    }

    private static String makeDescription(ForecastType forecastType, String message) {
        Objects.requireNonNull(forecastType, "forecastType cannot be null");
        message = Objects.requireNonNullElse(message, "");

        if (forecastType == ForecastType.CURRENT_WEATHER)
            return "Could not parse current weather information" + " - " + message;
        if (forecastType == ForecastType.THREE_HOUR_FORECAST)
            return "Could not parse three hour forecast information" + " - " + message;
        return null;
    }
}
