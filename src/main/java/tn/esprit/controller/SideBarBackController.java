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
        Utilisateur U = UtilisateurConnecte.getInstance().getUtilisateurConnecter();
        NomUtilisateur.setText(U.getNom());
    }

    @FXML
    void EventsChangeStyleEntered(MouseEvent event) {
        // À compléter selon le style désiré
    }

    @FXML
    void EventsChangeStyleExited(MouseEvent event) {
        // À compléter selon le style désiré
    }

    @FXML
    void LogOutChangeStyleEntered(MouseEvent event) {
        // À compléter
    }

    @FXML
    void LogOutChangeStyleExited(MouseEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_Avis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis_back.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Afficher avis Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Naviguer_a_Covoiturages(ActionEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_Notifications(ActionEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_Paiements(ActionEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_Reservations(ActionEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_Utilisateurs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUtilisateurBack.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Modifier profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Naviguer_a_Voitures(ActionEvent event) {
        // À compléter
    }

    @FXML
    void Naviguer_a_login(ActionEvent event) {
        UtilisateurConnecte.getInstance().setUtilisateurConnecte(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("LOGIN");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void UsersChangeStyleEntered(MouseEvent event) {
        // À compléter
    }

    @FXML
    void UsersChangeStyleExited(MouseEvent event) {
        // À compléter
    }
}
