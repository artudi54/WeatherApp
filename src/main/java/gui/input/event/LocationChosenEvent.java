package main.java.gui.input.event;

import javafx.event.Event;
import javafx.event.EventType;
import main.java.weather.types.ForecastType;

import java.util.Objects;

public abstract class LocationChosenEvent extends Event {
    public static final EventType<LocationChosenEvent> LOCATION_CHOSEN =
            new EventType<>(Event.ANY, "LOCATION_CHOSEN");

    private ForecastType forecastType;

    protected LocationChosenEvent(ForecastType forecastType,
                                     EventType<? extends LocationChosenEvent> eventType) {
        super(eventType);
        this.forecastType = Objects.requireNonNull(forecastType, "forecastType cannot be null");
    }

    public ForecastType getForecastType() {
        return forecastType;
    }
}
