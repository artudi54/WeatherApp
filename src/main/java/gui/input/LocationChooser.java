package main.java.gui.input;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import main.java.AppUtils;
import main.java.config.Cities;
import main.java.gui.input.event.LocationChosenByCityIdEvent;
import main.java.gui.input.event.LocationChosenByCityNameEvent;
import main.java.gui.input.event.LocationChosenByCoordinatesEvent;
import main.java.gui.input.event.LocationChosenEvent;
import main.java.weather.types.Coordinates;

public class LocationChooser extends VBox {
    @FXML
    private CityChooser cityChooser;
    @FXML
    private CoordinatesChooser coordinatesChooser;
    @FXML
    private IDChooser idChooser;
    @FXML
    private ForecastTypeChooser forecastTypeChooser;

    private ObjectProperty<EventHandler<LocationChosenEvent>> onLocationChosen;
    private ObjectProperty<EventHandler<LocationChosenByCityNameEvent>> onLocationChosenByCityName;
    private ObjectProperty<EventHandler<LocationChosenByCoordinatesEvent>> onLocationChosenByCoordinates;
    private ObjectProperty<EventHandler<LocationChosenByCityIdEvent>> onLocationChosenByCityId;

    public LocationChooser() {
        AppUtils.loadFXML(getClass().getResource("/fxml/LocationChooser.fxml"), this);

        onLocationChosen = new SimpleObjectProperty<>(this, "OnLocationChosen");
        onLocationChosenByCityName = new SimpleObjectProperty<>(this, "OnLocationChosenByCityName");
        onLocationChosenByCoordinates = new SimpleObjectProperty<>(this, "OnLocationChosenByCoordinates");
        onLocationChosenByCityId = new SimpleObjectProperty<>(this, "OnLocationChosenByCityId");

        onLocationChosen.addListener(((observableValue, oldV, newV) ->
                setEventHandler(LocationChosenEvent.LOCATION_CHOSEN, newV)));
        onLocationChosenByCityName.addListener(((observableValue, oldV, newV) ->
                setEventHandler(LocationChosenByCityNameEvent.LOCATION_CHOSEN_BY_CITY_NAME, newV)));
        onLocationChosenByCoordinates.addListener(((observableValue, oldV, newV) ->
                setEventHandler(LocationChosenByCoordinatesEvent.LOCATION_CHOSEN_BY_COORDINATES, newV)));
        onLocationChosenByCityId.addListener(((observableValue, oldV, newV) ->
                setEventHandler(LocationChosenByCityIdEvent.LOCATION_CHOSEN_BY_CITY_ID, newV)));

        cityChooser.setOnAction(this::handleInputByCityName);
        coordinatesChooser.setOnAction(this::handleInputByCoordinates);
        idChooser.setOnAction(this::handleInputByCityId);
    }

    public void setCities(Cities cities) {
        cityChooser.setCities(cities);
    }

    public EventHandler<LocationChosenEvent> getOnLocationChosen() {
        return onLocationChosen.get();
    }

    public ObjectProperty<EventHandler<LocationChosenEvent>> onLocationChosenProperty() {
        return onLocationChosen;
    }

    public void setOnLocationChosen(EventHandler<LocationChosenEvent> onLocationChosen) {
        this.onLocationChosen.set(onLocationChosen);
    }

    public EventHandler<LocationChosenByCityNameEvent> getOnLocationChosenByCityName() {
        return onLocationChosenByCityName.get();
    }

    public ObjectProperty<EventHandler<LocationChosenByCityNameEvent>> onLocationChosenByCityNameProperty() {
        return onLocationChosenByCityName;
    }

    public void setOnLocationChosenByCityName(EventHandler<LocationChosenByCityNameEvent> onLocationChosenById) {
        this.onLocationChosenByCityName.set(onLocationChosenById);
    }

    public EventHandler<LocationChosenByCoordinatesEvent> getOnLocationChosenByCoordinates() {
        return onLocationChosenByCoordinates.get();
    }

    public ObjectProperty<EventHandler<LocationChosenByCoordinatesEvent>> onLocationChosenByCoordinatesProperty() {
        return onLocationChosenByCoordinates;
    }

    public void setOnLocationChosenByCoordinates(EventHandler<LocationChosenByCoordinatesEvent> onLocationChosenByCoordinates) {
        this.onLocationChosenByCoordinates.set(onLocationChosenByCoordinates);
    }

    public EventHandler<LocationChosenByCityIdEvent> getOnLocationChosenByCityId() {
        return onLocationChosenByCityId.get();
    }

    public ObjectProperty<EventHandler<LocationChosenByCityIdEvent>> onLocationChosenByCityIdProperty() {
        return onLocationChosenByCityId;
    }

    public void setOnLocationChosenByCityId(EventHandler<LocationChosenByCityIdEvent> onLocationChosenByCityId) {
        this.onLocationChosenByCityId.set(onLocationChosenByCityId);
    }

    public String getCityName() {
        return cityChooser.getCityName();
    }

    public void setCityName(String cityName) {
        cityChooser.setCityName(cityName);
    }

    public Coordinates getCoordinates() {
        return coordinatesChooser.getCoordinates();
    }

    public void setCoordinates(Coordinates coordinates) {
        coordinatesChooser.setCoordinates(coordinates);
    }

    public int getCityId() {
        return idChooser.getCityId();
    }

    public void setCityId(int id) {
        idChooser.setCityId(id);
    }

    private void showInvalidInputAlert(String message) {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Input error - " + message,
                ButtonType.OK);
        alert.setHeaderText("Input error");
        alert.initOwner(getScene().getWindow());
        AppUtils.setDialogMinSize(alert);
        alert.showAndWait();
    }

    private void handleInputByCityName(ActionEvent event) {
        String cityName = cityChooser.getCityName();
        if (cityName.isEmpty()) {
            showInvalidInputAlert("city name input field is empty");
            return;
        }
        fireEvent(new LocationChosenByCityNameEvent(forecastTypeChooser.getForecastType(), cityName));
    }

    private void handleInputByCoordinates(ActionEvent event) {
        Coordinates coordinates = coordinatesChooser.getCoordinates();
        if (coordinates == null) {
            showInvalidInputAlert("invalid or empty coordinates specified");
            return;
        }
        fireEvent(new LocationChosenByCoordinatesEvent(forecastTypeChooser.getForecastType(), coordinates));
    }

    private void handleInputByCityId(ActionEvent event) {
        int cityId = idChooser.getCityId();
        if (cityId == -1) {
            showInvalidInputAlert("invalid or empty city id specified");
            return;
        }
        fireEvent(new LocationChosenByCityIdEvent(forecastTypeChooser.getForecastType(), cityId));

    }
}
