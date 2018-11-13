package main.java.gui.input;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import main.java.AppUtils;
import main.java.weather.types.ForecastType;

public class ForecastTypeChooser extends TitledPane {
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton currentWeatherButton;
    @FXML
    private RadioButton threeHourForecastButton;

    public ForecastTypeChooser() {
        AppUtils.loadFXML(getClass().getResource("/fxml/ForecastTypeChooser.fxml"), this);
    }

    public ForecastType getForecastType() {
        Toggle button = toggleGroup.getSelectedToggle();
        if (button == currentWeatherButton)
            return ForecastType.CURRENT_WEATHER;
        if (button == threeHourForecastButton)
            return ForecastType.THREE_HOUR_FORECAST;
        return null;
    }
}
