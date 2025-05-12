package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SideBarBackController {

    @FXML
    private void Naviguer_a_Reservations() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Reservations");
        loadScene("/reservations.fxml");
    }

    @FXML
    private void Naviguer_a_Utilisateurs() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Utilisateurs");
        loadScene("/utilisateurs.fxml");
    }

    @FXML
    private void Naviguer_a_Avis() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Avis");
        loadScene("/avis.fxml");
    }

    @FXML
    private void Naviguer_a_Voitures() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Voitures");
        loadScene("/voitures.fxml");
    }

    @FXML
    private void Naviguer_a_Covoiturages() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Covoiturages");
        loadScene("/covoiturages.fxml");
    }

    @FXML
    private void Naviguer_a_Paiements() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Paiements");
        loadScene("/paiements.fxml");
    }

    @FXML
    private void Naviguer_a_Notifications() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Notifications");
        loadScene("/notifications.fxml");
    }

    @FXML
    private void Naviguer_a_Parametres() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Paramètres");
        loadScene("/parametres.fxml");
    }

    @FXML
    private void Naviguer_a_ModifierProfil() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Modifier Profil");
        loadScene("/modifierprofil.fxml");
    }

    @FXML
    private void Naviguer_a_login() {
        // Placeholder for navigation logic
        System.out.println("Navigating to Login");
        loadScene("/login.fxml");
    }

    @FXML
    private void EventsChangeStyleEntered() {
        // Placeholder for style change on mouse enter
        System.out.println("Mouse entered event style change");
    }

    @FXML
    private void EventsChangeStyleExited() {
        // Placeholder for style change on mouse exit
        System.out.println("Mouse exited event style change");
    }

    @FXML
    private void UsersChangeStyleEntered() {
        // Placeholder for style change on mouse enter for Utilisateurs
        System.out.println("Mouse entered users style change");
    }

    @FXML
    private void UsersChangeStyleExited() {
        // Placeholder for style change on mouse exit for Utilisateurs
        System.out.println("Mouse exited users style change");
    }

    @FXML
    private void LogOutChangeStyleEntered() {
        // Placeholder for style change on mouse enter for LogOut
        System.out.println("Mouse entered logout style change");
    }

    @FXML
    private void LogOutChangeStyleExited() {
        // Placeholder for style change on mouse exit for LogOut
        System.out.println("Mouse exited logout style change");
    }

    private void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            // Optionally close the current stage
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}