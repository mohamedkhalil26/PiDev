package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SidebarbackController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML private Button Avis;
    @FXML private Button Covoiturages;
    @FXML private Button LogOutBack;
    @FXML private TextField NomUtilisateur;
    @FXML private Button Notifications;
    @FXML private Button Paiements;
    @FXML private Button Reservations;
    @FXML private Button Utilisateurs;
    @FXML private Button Voitures;


    @FXML
    void Naviguer_a_Avis(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Avis.fxml");
        }
    }

    @FXML
    void Naviguer_a_Covoiturages(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Covoiturages.fxml");
        }
    }

    @FXML
    void Naviguer_a_Notifications(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Notifications.fxml");
        }
    }

    @FXML
    void Naviguer_a_Paiements(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Paiements.fxml");
        }
    }

    @FXML
    void Naviguer_a_Reservations(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Reservation.fxml");
        }
    }

    @FXML
    void Naviguer_a_Utilisateurs(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Utilisateurs.fxml");
        }
    }

    @FXML
    void Naviguer_a_Voitures(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Voitures.fxml");
        }
    }

    @FXML
    void Naviguer_a_login(ActionEvent event) {
        if (mainController != null) {
            mainController.setCenterView("/Login.fxml");
        }
    }



    // Style methods
    @FXML void EventsChangeStyleEntered(MouseEvent event) { applyHoverStyle(event); }
    @FXML void EventsChangeStyleExited(MouseEvent event) { applyNormalStyle(event); }
    @FXML void LogOutChangeStyleEntered(MouseEvent event) { applyHoverStyle(event); }
    @FXML void LogOutChangeStyleExited(MouseEvent event) { applyNormalStyle(event); }
    @FXML void UsersChangeStyleEntered(MouseEvent event) { applyHoverStyle(event); }
    @FXML void UsersChangeStyleExited(MouseEvent event) { applyNormalStyle(event); }

    private void applyHoverStyle(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #F3CE5D; -fx-text-fill: #2A2A72; -fx-font-size: 16; " +
                "-fx-pref-width: 210; -fx-pref-height: 55; -fx-background-radius: 15; -fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");
    }

    private void applyNormalStyle(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #B0A295; -fx-text-fill: #F2F2F5; -fx-font-size: 16; " +
                "-fx-pref-width: 210; -fx-pref-height: 55; -fx-background-radius: 15; -fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");
    }
}
