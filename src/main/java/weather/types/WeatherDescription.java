package main.java.weather.types;

import java.util.Objects;

public class WeatherDescription {
    private String description;
    private String detailedDescription;
    private String iconName;

    public WeatherDescription(String description, String detailedDescription, String iconName) {
        this.description = Objects.requireNonNull(description, "description cannot be null");
        this.detailedDescription = Objects.requireNonNull(detailedDescription, "description cannot be null");
        this.iconName = Objects.requireNonNull(iconName, "description cannot be null");
    }

    public String getDescription() {
        return description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public String getIconName() {
        return iconName;
    }

    @Override
    public String toString() {
        return description + " (" + detailedDescription + ')';
    }
}
