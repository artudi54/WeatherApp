package main.java.gui.input.event;

import javafx.event.EventType;
import main.java.weather.types.ForecastType;

import java.security.InvalidParameterException;

public class LocationChosenByCityIdEvent extends LocationChosenEvent {
    public static final EventType<LocationChosenByCityIdEvent> LOCATION_CHOSEN_BY_CITY_ID =
            new EventType<>(LocationChosenEvent.LOCATION_CHOSEN, "LOCATION_CHOSEN_BY_CITY_ID");

    private int cityId;

    public LocationChosenByCityIdEvent(ForecastType forecastType, int cityId) {
        super(forecastType, LOCATION_CHOSEN_BY_CITY_ID);
        if (cityId < 0)
            throw new InvalidParameterException("cityId cannot be negative");
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }
}
