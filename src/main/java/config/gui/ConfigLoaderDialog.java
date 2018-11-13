package main.java.config.gui;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import main.java.AppUtils;
import main.java.config.Config;
import main.java.config.WeatherIcons;
import main.java.config.WorldInformation;
import main.java.exception.ConfigException;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigLoaderDialog extends Dialog<CompleteConfig> {
    private static class ConfigLoader extends Task<CompleteConfig> {
        private Path countryListJson;
        private Path cityListJson;
        private Path iconsDirectory;
        private Path iniPath;
        private String errorString;

        public ConfigLoader(Path countryListJson, Path cityListJson, Path iconsDirectory, Path iniPath) {
            this.countryListJson = countryListJson;
            this.cityListJson = cityListJson;
            this.iconsDirectory = iconsDirectory;
            this.iniPath = iniPath;
            this.errorString = null;
        }

        @Override
        protected CompleteConfig call() throws Exception {
            WorldInformation worldInformation = new WorldInformation(countryListJson, cityListJson);
            WeatherIcons weatherIcons = new WeatherIcons(iconsDirectory);
            Config config;
            try {
                // non failing error
                config = Config.loadFrom(iniPath);
            }
            catch (ConfigException exc) {
                errorString = exc.getMessage();
                config = new Config();
            }
            return new CompleteConfig(worldInformation, weatherIcons, config);
        }

        public boolean hasError() {
            return errorString != null;
        }

        public String getErrorString() {
            return errorString;
        }
    }

    private ExecutorService executorService;
    private ConfigLoader configLoader;

    public ConfigLoaderDialog(Path countryListJson, Path cityListJson, Path iconsDirectory, Path iniPath) {
        AppUtils.loadFXML(getClass().getResource("/fxml/ConfigLoaderDialog.fxml"), this, getDialogPane());
        setTitle("WeatherApp");

        startExecution(countryListJson, cityListJson, iconsDirectory, iniPath);
    }

    private void startExecution(Path countryListJson, Path cityListJson, Path iconsDirectory, Path iniPath) {
        Objects.requireNonNull(countryListJson, "countryCityListJson cannot be null");
        Objects.requireNonNull(cityListJson, "cityListJson cannot be null");
        Objects.requireNonNull(iconsDirectory, "iconsDirectory cannot be null");
        Objects.requireNonNull(iniPath, "iniPath cannot be null");

        executorService = Executors.newSingleThreadExecutor();
        configLoader = new ConfigLoader(countryListJson, cityListJson, iconsDirectory, iniPath);
        configLoader.setOnSucceeded((event)-> handleSucceeded());
        configLoader.setOnFailed((event) -> handleError());
        executorService.execute(configLoader);
    }

    private void handleSucceeded() {
        if (configLoader.hasError()) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Error occured - " + configLoader.getErrorString() + " - using default configuration",
                    ButtonType.OK);
            alert.setHeaderText("Configuration load error");
            alert.initOwner(getDialogPane().getScene().getWindow());
            AppUtils.setDialogMinSize(alert);
            alert.showAndWait();
        }

        AppUtils.closeExecutorService(executorService);
        setResult(configLoader.getValue());
    }

    private void handleError() {
        Alert alert = new Alert(
                Alert.AlertType.ERROR,
                "Critical error occurred - " + configLoader.getException().getMessage() + " - program will now exit",
                ButtonType.OK);
        alert.setHeaderText("Critical error");
        alert.initOwner(getDialogPane().getScene().getWindow());
        AppUtils.setDialogMinSize(alert);
        alert.showAndWait();

        AppUtils.closeExecutorService(executorService);
        getDialogPane().getScene().getWindow().hide();
    }
}
