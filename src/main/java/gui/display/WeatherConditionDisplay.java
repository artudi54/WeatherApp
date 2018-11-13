package main.java.gui.display;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.java.AppUtils;
import main.java.config.WeatherIcons;
import main.java.weather.condition.WeatherCondition;
import main.java.weather.types.ConditionDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class WeatherConditionDisplay extends VBox {
    private static final DateFormat TODAY_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DateFormat DAY_FORMAT = new SimpleDateFormat("EEEEE HH:mm");

    @FXML
    private Label dateLabel;
    @FXML
    private ImageView weatherIconView;
    @FXML
    private TextField descriptionLabel;
    @FXML
    private TextField temperatureLabel;
    @FXML
    private TextField pressureLabel;
    @FXML
    private TextField seaPressureLabel;
    @FXML
    private TextField groundPressureLabel;
    @FXML
    private TextField humidityLabel;
    @FXML
    private TextField windLabel;
    @FXML
    private TextField cloudinessLabel;
    @FXML
    private TextField rainfallSnowfallVolumeLabel;
    @FXML
    private TextField sunriseLabel;
    @FXML
    private TextField sunsetLabel;

    private WeatherIcons weatherIcons;
    private WeatherCondition weatherCondition;

    public WeatherConditionDisplay() {
        AppUtils.loadFXML(getClass().getResource("/fxml/WeatherConditionDisplay.fxml"), this);
    }

    public WeatherIcons getWeatherIcons() {
        return weatherIcons;
    }

    public void setWeatherIcons(WeatherIcons weatherIcons) {
        Objects.requireNonNull(weatherIcons, "weatherIcons cannot be null");
        this.weatherIcons = weatherIcons;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }


    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;

        if (weatherCondition == null)
            clearWeather();
        else {
            dateLabel.setText(makeDateDescription(weatherCondition.getConditionDate()));
            if (weatherIcons != null)
                weatherIconView.setImage(weatherIcons.getIcon(weatherCondition.getWeatherDescription().getIconName()));
            else
                weatherIconView.setImage(null);
            descriptionLabel.setText(weatherCondition.getWeatherDescription().toString());
            temperatureLabel.setText(
                    weatherCondition.getTemperature() + ", min(" +
                    weatherCondition.getMinimalTemperature() + "), max(" + weatherCondition.getMaximalTemperature() + ")");
            pressureLabel.setText(weatherCondition.getPressure().toString());
            seaPressureLabel.setText(weatherCondition.getSeaPressure().toString());
            groundPressureLabel.setText(weatherCondition.getGroundPressure().toString());
            humidityLabel.setText(weatherCondition.getHumidity().toString());
            windLabel.setText(weatherCondition.getWind().toString());
            cloudinessLabel.setText(weatherCondition.getCloudiness().toString());
            rainfallSnowfallVolumeLabel.setText(weatherCondition.getRainfallVolume() + ", " +
                                        weatherCondition.getSnowfallVolume());
            sunriseLabel.setText(weatherCondition.getSunrise().toString());
            sunsetLabel.setText(weatherCondition.getSunset().toString());
        }
    }

    public void clearWeather() {
        dateLabel.setText("");
        weatherIconView.setImage(null);
        descriptionLabel.clear();
        temperatureLabel.clear();
        pressureLabel.clear();
        seaPressureLabel.clear();
        groundPressureLabel.clear();
        humidityLabel.clear();
        windLabel.clear();
        cloudinessLabel.clear();
        rainfallSnowfallVolumeLabel.clear();
        sunriseLabel.clear();
        sunsetLabel.clear();
    }

    public String getDateDescription() {
        return dateLabel.getText();
    }

    private String makeDateDescription(ConditionDate conditionDate) {
        Date date = conditionDate.getValue();
        Date todayDate = Calendar.getInstance().getTime();

        if (date.getYear() == todayDate.getYear() &&
            date.getMonth() == todayDate.getMonth() &&
            date.getDay() == todayDate.getDay())
            return "Today: " + TODAY_FORMAT.format(date);
        return DAY_FORMAT.format(date);
    }
}