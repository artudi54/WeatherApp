package main.java.weather.condition;

import main.java.weather.types.*;

import java.util.Objects;

public class WeatherCondition {
    private WeatherDescription weatherDescription;
    private Temperature temperature;
    private Temperature minimalTemperature;
    private Temperature maximalTemperature;
    private Pressure pressure;
    private Pressure seaPressure;
    private Pressure groundPressure;
    private Humidity humidity;
    private Wind wind;
    private Cloudiness cloudiness;
    private PrecipitationVolume rainfallVolume;
    private PrecipitationVolume snowfallVolume;
    private SunTime sunrise;
    private SunTime sunset;
    private ConditionDate conditionDate;

    public WeatherCondition(WeatherDescription weatherDescription, Temperature temperature,
                            Temperature minimalTemperature, Temperature maximalTemperature,
                            Pressure pressure, Pressure seaPressure, Pressure groundPressure,
                            Humidity humidity, Wind wind, Cloudiness cloudiness,
                            PrecipitationVolume rainfallVolume, PrecipitationVolume snowfallVolume,
                            SunTime sunrise, SunTime sunset, ConditionDate conditionDate) {
        this.weatherDescription = Objects.requireNonNull(weatherDescription, "weatherDescription cannot be null");
        this.temperature = Objects.requireNonNull(temperature, "temperature cannot be null");
        this.minimalTemperature = Objects.requireNonNull(minimalTemperature, "minimalTemperature cannot be null");
        this.maximalTemperature = Objects.requireNonNull(maximalTemperature, "maximalTemperature cannot be null");
        this.pressure = Objects.requireNonNull(pressure, "pressure cannot be null");
        this.seaPressure = Objects.requireNonNull(seaPressure, "seaPressure cannot be null");
        this.groundPressure = Objects.requireNonNull(groundPressure, "groundPressure cannot be null");
        this.humidity = Objects.requireNonNull(humidity, "humidity cannot be null");
        this.wind = Objects.requireNonNull(wind, "wind cannot be null");
        this.cloudiness = Objects.requireNonNull(cloudiness, "cloudiness cannot be null");
        this.rainfallVolume = Objects.requireNonNull(rainfallVolume, "rainfallVolume cannot be null");
        this.snowfallVolume = Objects.requireNonNull(snowfallVolume, "snowfallVolume cannot be null");
        this.sunrise = Objects.requireNonNull(sunrise, "sunrise cannot be null");
        this.sunset = Objects.requireNonNull(sunset, "sunset cannot be null");
        this.conditionDate = Objects.requireNonNull(conditionDate, "conditionDate cannot be null");
    }

    public WeatherDescription getWeatherDescription() {
        return weatherDescription;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Temperature getMinimalTemperature() {
        return minimalTemperature;
    }

    public Temperature getMaximalTemperature() {
        return maximalTemperature;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public Pressure getSeaPressure() {
        return seaPressure;
    }

    public Pressure getGroundPressure() {
        return groundPressure;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public Wind getWind() {
        return wind;
    }

    public Cloudiness getCloudiness() {
        return cloudiness;
    }

    public PrecipitationVolume getRainfallVolume() {
        return rainfallVolume;
    }

    public PrecipitationVolume getSnowfallVolume() {
        return snowfallVolume;
    }

    public SunTime getSunrise() {
        return sunrise;
    }

    public SunTime getSunset() {
        return sunset;
    }

    public ConditionDate getConditionDate() {
        return conditionDate;
    }


    public static final class Builder {
        private WeatherDescription weatherDescription;
        private Temperature temperature;
        private Temperature minimalTemperature;
        private Temperature maximalTemperature;
        private Pressure pressure;
        private Pressure seaPressure;
        private Pressure groundPressure;
        private Humidity humidity;
        private Wind wind;
        private Cloudiness cloudiness;
        private PrecipitationVolume rainfallVolume;
        private PrecipitationVolume snowfallVolume;
        private SunTime sunrise;
        private SunTime sunset;
        private ConditionDate conditionDate;

        private Builder() {
        }

        public Builder weatherDescription(WeatherDescription weatherDescription) {
            this.weatherDescription = weatherDescription;
            return this;
        }

        public Builder temperature(Temperature temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder minimalTemperature(Temperature minimalTemperature) {
            this.minimalTemperature = minimalTemperature;
            return this;
        }

        public Builder maximalTemperature(Temperature maximalTemperature) {
            this.maximalTemperature = maximalTemperature;
            return this;
        }

        public Builder pressure(Pressure pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder seaPressure(Pressure seaPressure) {
            this.seaPressure = seaPressure;
            return this;
        }

        public Builder groundPressure(Pressure groundPressure) {
            this.groundPressure = groundPressure;
            return this;
        }

        public Builder humidity(Humidity humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder wind(Wind wind) {
            this.wind = wind;
            return this;
        }

        public Builder cloudiness(Cloudiness cloudiness) {
            this.cloudiness = cloudiness;
            return this;
        }

        public Builder rainfallVolume(PrecipitationVolume rainfallVolume) {
            this.rainfallVolume = rainfallVolume;
            return this;
        }

        public Builder snowfallVolume(PrecipitationVolume snowfallVolume) {
            this.snowfallVolume = snowfallVolume;
            return this;
        }

        public Builder sunrise(SunTime sunrise) {
            this.sunrise = sunrise;
            return this;
        }

        public Builder sunset(SunTime sunset) {
            this.sunset = sunset;
            return this;
        }

        public Builder calculationDate(ConditionDate conditionDate) {
            this.conditionDate = conditionDate;
            return this;
        }

        public WeatherCondition build() {
            return new WeatherCondition(weatherDescription, temperature, minimalTemperature, maximalTemperature,
                                         pressure, seaPressure, groundPressure, humidity, wind, cloudiness,
                                         rainfallVolume, snowfallVolume, sunrise, sunset, conditionDate);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
