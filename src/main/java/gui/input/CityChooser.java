package main.java.gui.input;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import main.java.AppUtils;
import main.java.config.Cities;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CityChooser extends GridPane {
    @FXML
    private TextField cityInputField;
    @FXML
    private Button getForecastButton;
    private Cities cities;
    private List<String> cityList;
    private ContextMenu entriesMenu;


    public CityChooser() {
        AppUtils.loadFXML(getClass().getResource("/fxml/CityChooser.fxml"), this);

        cityList = null;
        entriesMenu = new ContextMenu();

        cityInputField.textProperty().addListener((observableValue, oldV, newV) -> handleAutocompletion(newV));
        cityInputField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTabKeyCompletion);
        cityInputField.focusedProperty().addListener(((observableValue, oldV, newV) -> entriesMenu.hide()));
        entriesMenu.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTabKeyCompletion);
    }

    public void setCities(Cities cities) {
        Objects.requireNonNull(cities, "cities cannot be null");
        cityList = cities.asCityList();
    }

    public String getCityName() {
        return cityInputField.getText().trim();
    }

    public void setCityName(String cityName) {
        if (cityName == null)
            cityInputField.clear();
        else
            cityInputField.setText(cityName);
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

    private void handleAutocompletion(String enteredText) {
        if (!cityInputField.isFocused())
            return;

        entriesMenu.getItems().clear();

        if (enteredText == null || enteredText.equals("")) {
            entriesMenu.hide();
            return;
        }

        List<String> autocompletionList = cityList.stream()
                .filter(s -> s.toLowerCase().startsWith(enteredText.toLowerCase()))
                .collect(Collectors.toList());

        if (autocompletionList.isEmpty() ||
            (autocompletionList.size() == 1 && autocompletionList.get(0).equals(enteredText))) {
            entriesMenu.hide();
            return;
        }

        int autocompletionSize = Math.min(autocompletionList.size(), 10);
        for (int i = 0; i < autocompletionSize; ++i) {
            String completion = autocompletionList.get(i);
            MenuItem item = new MenuItem(completion);
            item.setOnAction(actionEvent -> useCompletion(completion));
            entriesMenu.getItems().add(item);
        }
        entriesMenu.show(cityInputField, Side.BOTTOM, 0 ,0);
    }

    private void useCompletion(String completion) {
        cityInputField.setText(completion);
        cityInputField.positionCaret(completion.length());
    }

    private void handleTabKeyCompletion(KeyEvent event) {
        if (!entriesMenu.isShowing() || entriesMenu.getItems().isEmpty())
            return;
        if (event.getCode() == KeyCode.TAB) {
            Set<Node> entries = entriesMenu.getSkin().getNode().lookupAll(".menu-item");
            Node node = entries.stream()
                    .filter(Node::isFocused)
                    .findAny()
                    .orElse(null);

            if (node != null) {
                node.fireEvent(new KeyEvent(
                        KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                        false, false, false, false));
            }
            else {
                String text = entriesMenu.getItems().get(0).getText();
                useCompletion(text);
            }
            event.consume();
        }
    }
}
