package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entites.UtilisateurConnecte;

import java.io.IOException;

public class SidebarController {
    private Stage chatStage;
    @FXML
    private Button chatButton;

    @FXML
    private Button Avis;

    @FXML
    private Button Covoiturages;

    @FXML
    private Button EditProfile;

    @FXML
    private Button LogOut;

    @FXML
    private Button Notification;

    @FXML
    private Button Paiements;

    @FXML
    private Button Reservation;

    @FXML
    private Button Utilisateur;

    @FXML
    private Button Voitures;

    @FXML
    void naviguer_a_avis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Afficher avis");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }

    @FXML
    void naviguer_a_covoiturages(ActionEvent event) {

    }

    @FXML
    void naviguer_a_editprofile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierprofile.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Modifier profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }

    }

    @FXML
    void naviguer_a_login(ActionEvent event) {
        UtilisateurConnecte.getInstance().setUtilisateurConnecte(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("LOGIN");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }

    @FXML
    void naviguer_a_notifiaction(ActionEvent event) {

    }

    @FXML
    void naviguer_a_paiements(ActionEvent event) {

    }

    @FXML
    void naviguer_a_reservation(ActionEvent event) {

    }

    @FXML
    void naviguer_a_utilisateur(ActionEvent event) {

    }

    @FXML
    void naviguer_a_voitures(ActionEvent event) {

    }
    @FXML
    private void openChatWindow(ActionEvent event) throws IOException {
        if (chatStage != null && chatStage.isShowing()) {
            chatStage.toFront(); // Bring the existing chat window to the front
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chat.fxml"));
        Parent root = loader.load();

        chatStage = new Stage();
        chatStage.initModality(Modality.NONE);
        chatStage.setTitle("Chat Bot");
        chatStage.setScene(new Scene(root));

        Stage mainStage = (Stage) chatButton.getScene().getWindow();
        chatStage.setX(mainStage.getX() + mainStage.getWidth() - 350);
        chatStage.setY(mainStage.getY() + mainStage.getHeight() - 550);

        chatStage.show();
    }


}
