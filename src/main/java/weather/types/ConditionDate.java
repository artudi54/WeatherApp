package main.java.weather.types;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConditionDate {
    private static final DateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private Date value;

    public ConditionDate(int unixTime) {
        if (unixTime < 0)
            throw new InvalidParameterException("unixTime cannot be negative");
        this.value = new Date((long)unixTime * 1000); // ms
    }

    public Date getValue() {
        return value;
    }

    @Override
    public String toString() {
        return FORMAT.format(value);
    }
}
