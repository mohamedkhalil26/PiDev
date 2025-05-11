package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.api.mailer;
import tn.esprit.entites.Utilisateur;
import tn.esprit.services.UtilisateurService;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Mot_de_passe_oubliee_controller {
    private final UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    private TextField Email;;

    @FXML
    private Button sendButton;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la navigation : " + e.getMessage());
        }
    }

    @FXML
    private void submit(ActionEvent event) {
        String emailAddress = Email.getText().trim();

        if (emailAddress.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer une adresse e-mail.");
            return;
        }

        Utilisateur user = utilisateurService.getUtilisateurByEmail(emailAddress);

        if (user != null) {
            try {
                mailer.sendEmail(user);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "E-mail envoyé à : " + emailAddress);
                navigateToLogin();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'envoi de l'e-mail : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse e-mail non trouvée.");
        }
    }
}
