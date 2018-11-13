package main.java.networking;

import main.java.weather.types.UnitsFormat;

import java.util.Objects;

public class WeatherResponse {
    private String value;
    private AcquisitionType acquisitionType;
    private UnitsFormat unitsFormat;

    public WeatherResponse(String value, AcquisitionType acquisitionType, UnitsFormat unitsFormat) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
        this.acquisitionType = Objects.requireNonNull(acquisitionType, "acquisitionType cannot be null");
        this.unitsFormat = Objects.requireNonNull(unitsFormat, "unitsFormat cannot be null");
    }

    public String getValue() {
        return value;
    }

    public AcquisitionType getAcquisitionType() {
        return acquisitionType;
    }

    public UnitsFormat getUnitsFormat() {
        return unitsFormat;
    }

}
