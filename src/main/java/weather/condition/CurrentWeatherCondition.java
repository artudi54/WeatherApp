package main.java.weather.condition;

import main.java.weather.types.City;

import java.util.Objects;

public class CurrentWeatherCondition {
    private WeatherCondition weatherCondition;
    private City city;

    public CurrentWeatherCondition(WeatherCondition weatherCondition, City city) {
        this.weatherCondition = Objects.requireNonNull(weatherCondition, "weatherCondition cannot be null");
        this.city = Objects.requireNonNull(city, "city cannot be null");
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public City getCity() {
        return city;
    }
}
