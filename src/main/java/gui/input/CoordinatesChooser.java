package main.java.gui.input;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.java.AppUtils;
import main.java.weather.types.Coordinates;

import java.io.IOException;
import java.util.Objects;

public class CoordinatesChooser extends GridPane {
    @FXML
    private TextField latitudeInputField;
    @FXML
    private TextField longitudeInputField;
    @FXML
    private Button getForecastButton;

    public CoordinatesChooser() {
        AppUtils.loadFXML(getClass().getResource("/fxml/CoordinatesChooser.fxml"), this);
    }

    public Coordinates getCoordinates() {
        try {
            return new Coordinates(
              Double.parseDouble(latitudeInputField.getText().trim()),
              Double.parseDouble(longitudeInputField.getText().trim())
            );
        }
        catch (Exception ignore) {
            return null;
        }
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            latitudeInputField.clear();
            longitudeInputField.clear();
        } else {
            latitudeInputField.setText(Double.toString(coordinates.getLatitude()));
            longitudeInputField.setText(Double.toString(coordinates.getLongitude()));
        }
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return getForecastButton.onActionProperty();
    }

    public final void setOnAction(EventHandler<ActionEvent> onAction) {
        getForecastButton.setOnAction(onAction);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return getForecastButton.getOnAction();
    }
}
