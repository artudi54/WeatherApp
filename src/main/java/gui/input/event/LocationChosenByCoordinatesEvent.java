package main.java.gui.input.event;

import javafx.event.EventType;
import main.java.weather.types.Coordinates;
import main.java.weather.types.ForecastType;

import java.util.Objects;

public class LocationChosenByCoordinatesEvent extends LocationChosenEvent {
    public static final EventType<LocationChosenByCoordinatesEvent> LOCATION_CHOSEN_BY_COORDINATES=
            new EventType<>(LocationChosenEvent.LOCATION_CHOSEN, "LOCATION_CHOSEN_BY_COORDINATES");

    private Coordinates coordinates;

    public LocationChosenByCoordinatesEvent(ForecastType forecastType, Coordinates coordinates) {
        super(forecastType, LOCATION_CHOSEN_BY_COORDINATES);
        this.coordinates = Objects.requireNonNull(coordinates, "coordinates cannot be null");
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
