package main.java.gui.display;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import main.java.AppUtils;
import main.java.config.WeatherIcons;
import main.java.weather.condition.CurrentWeatherCondition;
import main.java.weather.condition.ThreeHourWeatherForecast;
import main.java.weather.condition.WeatherCondition;

import java.util.List;
import java.util.stream.Collectors;

public class WeatherDisplay extends BorderPane {
    private static final ConditionContainer EMPTY_CONDITION_CONTAINER = new ConditionContainer(new WeatherConditionDisplay());

    private static class ConditionContainer {
        private WeatherConditionDisplay weatherConditionDisplay;

        public ConditionContainer(WeatherConditionDisplay weatherConditionDisplay) {
            this.weatherConditionDisplay = weatherConditionDisplay;
        }

        public WeatherConditionDisplay getWeatherConditionDisplay() {
            return weatherConditionDisplay;
        }

        @Override
        public String toString() {
            return weatherConditionDisplay.getDateDescription();
        }
    }

    @FXML
    private ComboBox<ConditionContainer> conditionDisplayComboBox;
    @FXML
    private HBox contentPane;

    private WeatherIcons weatherIcons;

    public WeatherDisplay() {
        AppUtils.loadFXML(getClass().getResource("/fxml/WeatherDisplay.fxml"), this);
        conditionDisplayComboBox.valueProperty().addListener(((observableValue, oldV, newV) -> updateWeather(newV)));
        clearWeather();
    }

    public WeatherIcons getWeatherIcons() {
        return weatherIcons;
    }

    public void setWeatherIcons(WeatherIcons weatherIcons) {
        this.weatherIcons = weatherIcons;
    }

    public void setWeather(CurrentWeatherCondition currentWeatherCondition) {
        if (currentWeatherCondition == null) {
            clearWeather();
            return;
        }

        conditionDisplayComboBox.setItems(FXCollections.observableArrayList(
                new ConditionContainer(makeWeatherConditionDisplay(currentWeatherCondition.getWeatherCondition()))));
        conditionDisplayComboBox.getSelectionModel().select(0);
    }

    public void setWeather(ThreeHourWeatherForecast threeHourWeatherForecast) {
        if (threeHourWeatherForecast == null) {
            clearWeather();
            return;
        }

        List<ConditionContainer> conditionDisplayList = threeHourWeatherForecast.getWeatherConditions().stream()
                .map(this::makeWeatherConditionDisplay)
                .map(ConditionContainer::new)
                .collect(Collectors.toList());
        conditionDisplayComboBox.setItems(FXCollections.observableList(conditionDisplayList));
        conditionDisplayComboBox.getSelectionModel().select(0);
    }

    public void clearWeather() {
        conditionDisplayComboBox.setItems(FXCollections.observableArrayList(EMPTY_CONDITION_CONTAINER));
        conditionDisplayComboBox.getSelectionModel().select(0);
    }

    private WeatherConditionDisplay makeWeatherConditionDisplay(WeatherCondition weatherCondition) {
        WeatherConditionDisplay weatherConditionDisplay = new WeatherConditionDisplay();
        weatherConditionDisplay.setWeatherIcons(weatherIcons);
        weatherConditionDisplay.setWeatherCondition(weatherCondition);
        return weatherConditionDisplay;
    }

    private void updateWeather(ConditionContainer conditionContainer) {
        if (conditionContainer == null)
            contentPane.getChildren().setAll(EMPTY_CONDITION_CONTAINER.getWeatherConditionDisplay());
        else
            contentPane.getChildren().setAll(conditionContainer.getWeatherConditionDisplay());
    }
}
