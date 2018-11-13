package main.java.weather.types;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SunTime {
    public static final SunTime NA = new SunTime(null);
    private LocalTime value;

    private SunTime(LocalTime value) {
        this.value = value;
    }

    public SunTime(int unixTime) {
        if (unixTime < 0)
            throw new InvalidParameterException("unixTime cannot be negative");
        value = LocalDateTime.ofEpochSecond(unixTime, 0, OffsetDateTime.now().getOffset()).toLocalTime();
    }

    public LocalTime getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null)
            return "N/A";
        return value.format(DateTimeFormatter.ISO_TIME);
    }
}
