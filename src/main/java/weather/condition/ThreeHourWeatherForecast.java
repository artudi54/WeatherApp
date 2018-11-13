package main.java.weather.condition;

import main.java.weather.types.City;

import java.util.List;
import java.util.Objects;

public class ThreeHourWeatherForecast {
    private City city;
    private List<WeatherCondition> weatherConditions;

    public ThreeHourWeatherForecast(City city, List<WeatherCondition> weatherConditions) {
        this.city = Objects.requireNonNull(city, "city cannot be null");
        this.weatherConditions = Objects.requireNonNull(weatherConditions, "weatherConditions cannot be null");
    }

    public City getCity() {
        return city;
    }

    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public int getSize() {
        return weatherConditions.size();
    }
}
