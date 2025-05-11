package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.outils.MyDatabase;

import java.io.IOException;

public class mainGui extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize database
        MyDatabase db = MyDatabase.getInstance();
        System.out.println(db);

        // Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis_back.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("PIDEV");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
