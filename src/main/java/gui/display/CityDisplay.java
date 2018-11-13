package main.java.gui.display;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.java.AppUtils;
import main.java.weather.types.City;

public class CityDisplay extends VBox {
    @FXML
    private TextField idLabel;
    @FXML
    private TextField nameLabel;
    @FXML
    private TextField countryLabel;
    @FXML
    private TextField coordinatesLabel;

    private City city;

    public CityDisplay() {
        AppUtils.loadFXML(getClass().getResource("/fxml/CityDisplay.fxml"), this);
    }

    public CityDisplay(City city) {
        this();
        setCity(city);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
        if (city == null) {
            clearCity();
            return;
        }
        idLabel.setText(Integer.toString(city.getId()));
        nameLabel.setText(city.getName());
        countryLabel.setText(city.getCountry().toString());
        coordinatesLabel.setText(city.getCoordinates().toString());
    }

    public void clearCity() {
        idLabel.clear();
        nameLabel.clear();
        countryLabel.clear();
        coordinatesLabel.clear();
    }
}
