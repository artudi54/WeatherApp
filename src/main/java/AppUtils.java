package main.java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AppUtils {
    public static void loadFXML(URL fxml, Object rootController) {
        loadFXML(fxml, rootController, rootController);
    }
    public static void loadFXML(URL fxml, Object controller, Object root) {
        try {
            FXMLLoader loader = new FXMLLoader(fxml);
            loader.setController(controller);
            loader.setRoot(root);
            loader.load();
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static void closeExecutorService(ExecutorService executorService) {
        try {
            executorService.shutdownNow();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static void setDialogMinSize(Dialog dialog) {
        DialogPane pane = dialog.getDialogPane();
        Stage stage = (Stage)pane.getScene().getWindow();
        pane.applyCss();
        pane.layout();
        stage.setMinWidth(pane.getWidth());
        stage.setMinHeight(pane.getHeight());
    }

    public static void setGroupMinSize(Group group) {
        group.applyCss();
        group.layout();
        Pane pane = (Pane)group.getChildren().get(0);
        Stage stage = (Stage)group.getScene().getWindow();
        stage.setMinHeight(pane.getHeight());
        stage.setMinWidth(pane.getWidth());
    }

    public static void setWorkingDirectory() {
        try {
            Path path = Paths.get(FXMain.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath());
            if (!Files.isDirectory(path)) {
                path = path.getParent();
            }
            System.setProperty("user.dir", path.toString());
        }
        catch (URISyntaxException exc) {
            throw new RuntimeException(exc);
        }
    }
}
