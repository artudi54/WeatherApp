package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import main.java.gui.WeatherApplication;

public class FXMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            AppUtils.setWorkingDirectory();
            primaryStage = new WeatherApplication();
            primaryStage.show();
        }
        catch (Exception exc) {
            exc.printStackTrace();
            Platform.exit();
            System.exit(1);
        }

    }
}

