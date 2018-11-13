package main.java.config.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import main.java.AppUtils;
import main.java.config.Config;
import main.java.weather.types.UnitsFormat;

import java.util.Optional;

public class OptionsDialog extends Dialog {
    @FXML
    private TextField weatherApiKeyField;
    @FXML
    private ComboBox<UnitsFormat> unitsFormatComboBox;
    @FXML
    private CheckBox saveLastInputCheckBox;
    @FXML
    private Button okButton;
    @FXML
    private Button defaultsButton;

    private Config config;


    public OptionsDialog(Config config) {
        try {
            AppUtils.loadFXML(getClass().getResource("/fxml/OptionsDialog.fxml"), this, getDialogPane());
            setTitle("WeatherApp");
            this.config = config;

            weatherApiKeyField.setText(config.getWeatherApiKey());
            unitsFormatComboBox.setItems(FXCollections.observableArrayList(UnitsFormat.values()));
            unitsFormatComboBox.setValue(config.getUnitsFormat());
            saveLastInputCheckBox.setSelected(config.isSaveLastInput());

            okButton.setOnAction(event -> handleAccepted());
            defaultsButton.setOnAction(event -> setDefaults());
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    private boolean showQuestionDialog() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Changing API key may cause application to stop working properly. Do you want to continue?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("API key");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.initOwner(getDialogPane().getScene().getWindow());
        AppUtils.setDialogMinSize(alert);

        Optional<ButtonType> response = alert.showAndWait();
        return response.isPresent() && response.get() == ButtonType.YES;
    }

    private void handleAccepted() {
        if (!weatherApiKeyField.getText().trim().equals(config.getWeatherApiKey()) && !showQuestionDialog())
            return;
        config.setWeatherApiKey(weatherApiKeyField.getText().trim());
        config.setUnitsFormat(unitsFormatComboBox.getValue());
        config.setSaveLastInput(saveLastInputCheckBox.isSelected());
        if (!config.isSaveLastInput()) {
            config.setLastCoordinates(null);
            config.setLastCityName("");
            config.setLastCityId(0);
        }
        close();
    }

    private void setDefaults() {
        Config defaultConfig = new Config();
        weatherApiKeyField.setText(defaultConfig.getWeatherApiKey());
        unitsFormatComboBox.setValue(defaultConfig.getUnitsFormat());
        saveLastInputCheckBox.setSelected(defaultConfig.isSaveLastInput());
    }
}
