package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getResource("/MainLayout.fxml");
        if (url == null) {
            throw new IllegalStateException("Cannot find MainLayout.fxml");
        }
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Gestion Covoiturage");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}