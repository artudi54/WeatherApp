package main.java.config.gui;

import main.java.config.Config;
import main.java.config.WeatherIcons;
import main.java.config.WorldInformation;

import java.util.Objects;

public class CompleteConfig {
    private WorldInformation worldInformation;
    private WeatherIcons weatherIcons;
    private Config config;

    public CompleteConfig(WorldInformation worldInformation, WeatherIcons weatherIcons, Config config) {
        this.worldInformation = Objects.requireNonNull(worldInformation, "worldInformation cannot be null");
        this.weatherIcons = Objects.requireNonNull(weatherIcons, "weatherIcons cannot be null");
        this.config = Objects.requireNonNull(config, "config cannot be null");
    }

    public WorldInformation getWorldInformation() {
        return worldInformation;
    }

    public WeatherIcons getWeatherIcons() {
        return weatherIcons;
    }

    public Config getConfig() {
        return config;
    }
}