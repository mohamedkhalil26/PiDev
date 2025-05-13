package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainMenuController {

    @FXML
    private void handleGestionVoitures() {
        try {
            // Essayer de charger le fichier FXML de plusieurs façons
            URL fxmlUrl = getClass().getResource("/VoitureApp.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("/src/main/resources/VoitureApp.fxml");
            }
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("VoitureApp.fxml");
            }

            if (fxmlUrl == null) {
                throw new IOException("Impossible de trouver le fichier VoitureApp.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestion des Voitures");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture de la gestion des voitures: " + e.getMessage());
        }
    }

    @FXML
    private void handleGestionTrajets() {
        try {
            // Essayer de charger le fichier FXML de plusieurs façons
            URL fxmlUrl = getClass().getResource("/ListeTrajet.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("/src/main/resources/ListeTrajet.fxml");
            }
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("ListeTrajet.fxml");
            }

            if (fxmlUrl == null) {
                throw new IOException("Impossible de trouver le fichier ListeTrajet.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestion des Trajets");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture de la gestion des trajets: " + e.getMessage());
        }
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
