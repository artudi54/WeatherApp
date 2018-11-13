package main.java.gui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.AppUtils;
import main.java.Resources;
import main.java.config.Config;
import main.java.config.WeatherIcons;
import main.java.config.WorldInformation;
import main.java.config.gui.CompleteConfig;
import main.java.config.gui.ConfigLoaderDialog;
import main.java.config.gui.OptionsDialog;
import main.java.exception.ConfigException;
import main.java.exception.WeatherRequestException;
import main.java.exception.WeatherResponseParseException;
import main.java.gui.display.CityDisplay;
import main.java.gui.display.WeatherDisplay;
import main.java.gui.input.LocationChooser;
import main.java.gui.input.event.LocationChosenByCityIdEvent;
import main.java.gui.input.event.LocationChosenByCityNameEvent;
import main.java.gui.input.event.LocationChosenByCoordinatesEvent;
import main.java.networking.WeatherRequest;
import main.java.networking.WeatherRequestMaker;
import main.java.networking.WeatherResponse;
import main.java.parser.WeatherJSONParser;
import main.java.weather.condition.CurrentWeatherCondition;
import main.java.weather.condition.ThreeHourWeatherForecast;
import org.controlsfx.control.StatusBar;

import java.util.Optional;

public class WeatherApplication extends Stage {
    private enum State {
        NONE("OK"),
        DOWNLOADING("Downloading weather information"),
        DOWNLOADED("Weather information downloaded"),
        FAILED("Failed to get weather information");

        private String description;

        State(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    @FXML
    private LocationChooser locationChooser;
    @FXML
    private WeatherDisplay weatherDisplay;
    @FXML
    private CityDisplay cityDisplay;
    @FXML
    private StatusBar statusBar;
    @FXML
    private Button refreshButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button optionsButton;

    private Config config;
    private WeatherJSONParser weatherJSONParser;
    private WeatherRequestMaker weatherRequestMaker;

    private ObjectProperty<State> stateProperty;
    private WeatherRequest lastRequest;


    public WeatherApplication() {
        initStage();
        loadConfig();
        setNoneState();
    }

    private void initStage() {
        Group content = new Group();
        AppUtils.loadFXML(getClass().getResource("/fxml/WeatherApplication.fxml"), this, content);

        setScene(new Scene(content));

        getIcons().add(new Image(Resources.getResourceAsStream("/icon.png")));
        AppUtils.setGroupMinSize(content);

        setOnCloseRequest(event -> handleCloseRequest());
        setTitle("WeatherApp");
        setResizable(false);
    }

    private void loadConfig() {
        ConfigLoaderDialog dialog = new ConfigLoaderDialog(
                Resources.getPath("/country.list.json"),
                Resources.getPath("/city.list.json"),
                Resources.getPath("/icons"),
                Resources.getRelativePath("config.ini"));

        Stage dialogStage = (Stage)dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(Resources.getResourceAsStream("/icon.png")));
        AppUtils.setDialogMinSize(dialog);


        Optional<CompleteConfig> completeConfig = dialog.showAndWait();
        if (!completeConfig.isPresent()) {
            Platform.exit();
            System.exit(1);
        }

        WorldInformation worldInformation = completeConfig.get().getWorldInformation();
        WeatherIcons weatherIcons = completeConfig.get().getWeatherIcons();
        config = completeConfig.get().getConfig();

        weatherJSONParser = new WeatherJSONParser(worldInformation);
        weatherRequestMaker = new WeatherRequestMaker(config);

        stateProperty = new SimpleObjectProperty<>(this, "StateProperty");

        locationChooser.setCities(worldInformation.getCities());
        weatherDisplay.setWeatherIcons(weatherIcons);
        stateProperty.addListener(((observableValue, oldV, newV) -> handleStateChange(newV)));

        locationChooser.setOnLocationChosenByCityId(this::handleWeatherRequestByCityId);
        locationChooser.setOnLocationChosenByCityName(this::handleWeatherRequestByCityName);
        locationChooser.setOnLocationChosenByCoordinates(this::handleWeatherRequestByCoordinates);

        clearButton.setOnAction(event -> setNoneState());
        refreshButton.setOnAction(event -> handleRefresh());
        optionsButton.setOnAction(event -> handleOptions());

        if (config.isSaveLastInput()) {
            locationChooser.setCityName(config.getLastCityName());
            locationChooser.setCoordinates(config.getLastCoordinates());
            locationChooser.setCityId(config.getLastCityId());
        }
    }

    private void setNoneState() {
        stateProperty.setValue(State.NONE);
        weatherDisplay.clearWeather();
        cityDisplay.clearCity();
        lastRequest = null;
    }

    private void setDownloadingState() {
        stateProperty.setValue(State.DOWNLOADING);
        locationChooser.setDisable(true);
        refreshButton.setDisable(true);
        clearButton.setDisable(true);
    }

    private void setDownloadedState() {
        stateProperty.setValue(State.DOWNLOADED);
        locationChooser.setDisable(false);
        refreshButton.setDisable(false);
        clearButton.setDisable(false);
    }

    private void setFailedState() {
        stateProperty.setValue(State.FAILED);
        locationChooser.setDisable(false);
        refreshButton.setDisable(false);
        clearButton.setDisable(false);
    }

    private void handleStateChange(State state) {
        statusBar.setText("Status: " + stateProperty.get().toString());
    }

    private void showError(String title, Exception exc) {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Input error - " + exc.getMessage(),
                ButtonType.OK);
        alert.setHeaderText(title);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.initOwner(getScene().getWindow());
        AppUtils.setDialogMinSize(alert);
        alert.showAndWait();
    }

    private void handleWeatherRequestError(Exception exc) {
        showError("Weather request error", exc);
        setFailedState();
    }

    private void handleWeatherRequestByCityId(LocationChosenByCityIdEvent event) {
        if (config.isSaveLastInput())
            config.setLastCityId(event.getCityId());

        try {
            switch (event.getForecastType()) {
                case CURRENT_WEATHER:
                    lastRequest = weatherRequestMaker.currentWeatherById(event.getCityId());
                    lastRequest.setOnResponseReady(this::handleCurrentWeatherResponse);
                    break;
                case THREE_HOUR_FORECAST:
                    lastRequest = weatherRequestMaker.threeHourWeatherForecastById(event.getCityId());
                    lastRequest.setOnResponseReady(this::handleThreeHourWeatherForecast);
                    break;
            }
            lastRequest.setOnError(this::handleWeatherRequestError);
            lastRequest.asyncRun();
            setDownloadingState();
        }
        catch (WeatherRequestException exc) {
            showError("Weather request error", exc);
        }
    }

    private void handleWeatherRequestByCityName(LocationChosenByCityNameEvent event) {
        if (config.isSaveLastInput())
            config.setLastCityName(event.getCityName());

        String[] nameCountry = event.getCityName().split(",");
        try {
            switch (event.getForecastType()) {
                case CURRENT_WEATHER:
                    if (nameCountry.length == 1)
                        lastRequest = weatherRequestMaker.currentWeatherByCity(nameCountry[0]);
                    else
                        lastRequest = weatherRequestMaker.currentWeatherByCity(nameCountry[0], nameCountry[1]);
                    lastRequest.setOnResponseReady(this::handleCurrentWeatherResponse);
                    break;
                case THREE_HOUR_FORECAST:
                    if (nameCountry.length == 1)
                        lastRequest = weatherRequestMaker.threeHourWeatherForecastByCity(nameCountry[0]);
                    else
                        lastRequest = weatherRequestMaker.threeHourWeatherForecastByCity(nameCountry[0], nameCountry[1]);
                    lastRequest.setOnResponseReady(this::handleThreeHourWeatherForecast);
                    break;
            }
            lastRequest.setOnError(this::handleWeatherRequestError);
            lastRequest.asyncRun();
            setDownloadingState();
        }
        catch (WeatherRequestException exc) {
            showError("Weather request error", exc);
        }
    }

    private void handleWeatherRequestByCoordinates(LocationChosenByCoordinatesEvent event) {
        if (config.isSaveLastInput())
            config.setLastCoordinates(event.getCoordinates());

        try {
            switch (event.getForecastType()) {
                case CURRENT_WEATHER:
                    lastRequest = weatherRequestMaker.currentWeatherByCoordiantes(event.getCoordinates());
                    lastRequest.setOnResponseReady(this::handleCurrentWeatherResponse);
                    break;
                case THREE_HOUR_FORECAST:
                    lastRequest = weatherRequestMaker.threeHourWeatherForecastByCoordiantes(event.getCoordinates());
                    lastRequest.setOnResponseReady(this::handleThreeHourWeatherForecast);
                    break;
            }
            lastRequest.setOnError(this::handleWeatherRequestError);
            lastRequest.asyncRun();
            setDownloadingState();
        }
        catch (WeatherRequestException exc) {
            showError("Weather request error", exc);
        }
    }

    private void handleCurrentWeatherResponse(WeatherResponse weatherResponse) {
        try {
            CurrentWeatherCondition currentWeatherCondition = weatherJSONParser.parseCurrentWeatherCondition(weatherResponse);
            cityDisplay.setCity(currentWeatherCondition.getCity());
            weatherDisplay.setWeather(currentWeatherCondition);
            setDownloadedState();
        }
        catch (WeatherResponseParseException exc) {
            showError("Response parse error", exc);
            setFailedState();
        }
    }

    private void handleThreeHourWeatherForecast(WeatherResponse weatherResponse) {
        ThreeHourWeatherForecast threeHourWeatherForecast;
        try {
            threeHourWeatherForecast = weatherJSONParser.parseThreeHourForecast(weatherResponse);
            cityDisplay.setCity(threeHourWeatherForecast.getCity());
            weatherDisplay.setWeather(threeHourWeatherForecast);
            setDownloadedState();
        }
        catch (WeatherResponseParseException exc) {
            showError("Response parse error", exc);
            setFailedState();
        }
    }

    private void handleRefresh() {
        if (lastRequest != null) {
            try {
                lastRequest.asyncRun();
                setDownloadingState();
            }
            catch (WeatherRequestException exc) {
                showError("Weather request error", exc);
            }
        }
    }

    private void handleOptions() {
        OptionsDialog optionsDialog = new OptionsDialog(config);
        optionsDialog.initModality(Modality.WINDOW_MODAL);
        optionsDialog.initOwner(getScene().getWindow());
        AppUtils.setDialogMinSize(optionsDialog);
        optionsDialog.showAndWait();
    }

    private void handleCloseRequest() {
        try {
            config.saveTo(Resources.getRelativePath("config.ini"));
        }
        catch (ConfigException exc) {
            showError("Config save error", exc);
        }
        weatherRequestMaker.close();
    }
}
