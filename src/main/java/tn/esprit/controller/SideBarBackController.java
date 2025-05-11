package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.entites.Utilisateur;
import tn.esprit.entites.UtilisateurConnecte;

import java.io.IOException;

public class SideBarBackController {

    @FXML
    private Button Avis;

    @FXML
    private Button Covoiturages;

    @FXML
    private Button LogOutBack;

    @FXML
    private TextField NomUtilisateur;

    @FXML
    private Button Notifications;

    @FXML
    private Button Paiements;

    @FXML
    private Button Reservations;

    @FXML
    private Button Utilisateurs;

    @FXML
    private Button Voitures;

    @FXML
    void initialize() {
        Utilisateur U=UtilisateurConnecte.getInstance().getUtilisateurConnecter();
        NomUtilisateur.setText(U.getNom());

    }


    @FXML
    void EventsChangeStyleEntered(MouseEvent event) {


    }

    @FXML
    void EventsChangeStyleExited(MouseEvent event) {

    }

    @FXML
    void LogOutChangeStyleEntered(MouseEvent event) {

    }

    @FXML
    void LogOutChangeStyleExited(MouseEvent event) {

    }

    @FXML
    void Naviguer_a_Avis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis_back.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Afficher avis Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }

    @FXML
    void Naviguer_a_Covoiturages(ActionEvent event) {

    }

    @FXML
    void Naviguer_a_Notifications(ActionEvent event) {

    }

    @FXML
    void Naviguer_a_Paiements(ActionEvent event) {

    }

    @FXML
    void Naviguer_a_Reservations(ActionEvent event) {

    }

    @FXML
    void Naviguer_a_Utilisateurs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUtilisateurBack.fxml"));
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
    void Naviguer_a_Voitures(ActionEvent event) {

    }

    @FXML
    void Naviguer_a_login(ActionEvent event) {
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
    void UsersChangeStyleEntered(MouseEvent event) {

    }

    @FXML
    void UsersChangeStyleExited(MouseEvent event) {

    }

}
