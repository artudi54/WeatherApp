package main.java.gui.input.event;

import javafx.event.EventType;
import main.java.weather.types.ForecastType;

import java.util.Objects;

public class LocationChosenByCityNameEvent extends LocationChosenEvent {
    public static final EventType<LocationChosenByCityNameEvent> LOCATION_CHOSEN_BY_CITY_NAME =
            new EventType<>(LocationChosenEvent.LOCATION_CHOSEN, "LOCATION_CHOSEN_BY_CITY_NAME");

    private String cityName;

    public LocationChosenByCityNameEvent(ForecastType forecastType, String cityName) {
        super(forecastType, LOCATION_CHOSEN_BY_CITY_NAME);
        this.cityName = Objects.requireNonNull(cityName, "cityName cannot be null");
    }

    public String getCityName() {
        return cityName;
    }
}
