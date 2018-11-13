package main.java.gui.input;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.java.AppUtils;

import java.util.regex.Pattern;

public class IDChooser extends GridPane {
    private final Pattern DIGIT_PATTERN = Pattern.compile("[^\\d]+");

    @FXML
    private TextField idInputField;
    @FXML
    private Button getForecastButton;

    public IDChooser() {
        AppUtils.loadFXML(getClass().getResource("/fxml/IDChooser.fxml"), this);

        idInputField.textProperty().addListener((observableValue, oldV, newV) -> digitFilter(oldV, newV));
    }

    public int getCityId() {
        try {
            return Integer.parseInt(idInputField.getText().trim());
        }
        catch (Exception ignore) {
            return -1;
        }
    }

    public void setCityId(int cityId) {
        if (cityId <= 0)
            idInputField.clear();
        else
            idInputField.setText(Integer.toString(cityId));
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

    private void digitFilter(String oldValue, String newValue) {
        if (DIGIT_PATTERN.matcher(newValue).find())
            idInputField.setText(oldValue);
    }
}
