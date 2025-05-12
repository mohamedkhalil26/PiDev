package tn.esprit.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.entites.Utilisateur;
import tn.esprit.entites.UtilisateurConnecte;
import tn.esprit.services.*;

import java.io.IOException;
import java.net.URL;

public class LoginController {
    private UtilisateurService utilisateurService = new UtilisateurService();


    @FXML
    private Label forgetpassword;

        @FXML
        private Button loginBtn;

        @FXML
        private Label navsignup;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField Emailfield;

    @FXML
    private Button googleLoginButton;

    @FXML
    private TextField usernameField;

    @FXML
    void Submit(ActionEvent event) {
            String Mot_de_passe = passwordField.getText();
            String email = Emailfield.getText();
            Utilisateur U = new Utilisateur();
        if (email.isEmpty() || Mot_de_passe.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, " Error", "tous les champs sont obligatoires !.");
            return;
        }
            U=utilisateurService.getUtilisateurByEmailAndPass(email, Mot_de_passe);
            if(U==null) {
                showAlert(Alert.AlertType.ERROR,"Error","Email ou Mot_de_passe incorrect");
            }
            else {
                showAlert(Alert.AlertType.INFORMATION, "connecte avec succes", "bienvenue, " + U.getNom() +" "+U.getPrenom()+" !");

                UtilisateurConnecte.getInstance().setUtilisateurConnecte(U);
                if(U.getRole().toString().equals("admin")){
                    navigateToBackAdmin();
                }
                else {
                    navigateToHome();
                }

            }


    }
    private void navigateToBackAdmin() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUtilisateurBack.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "impossible de charger dashboared Admin.");
        }
    }
    private void navigateToHome() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierprofile.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, " Error", "impossible de charger l'accueil .");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void forgetpassword(MouseEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/oubliee.fxml");
            System.out.println("FXML URL: " + fxmlUrl);

            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Fichier manquant", "Le fichier FXML 'oubliee.fxml' est introuvable dans les ressources.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mot de passe oubliée");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur FXML", "Le fichier FXML a été trouvé, mais une erreur s'est produite lors du chargement.");
        }
    }



    @FXML
    void navigateToSignUp(MouseEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUpView.fxml"));
                Parent root = loader.load();

                // Get the current stage from the button (if applicable)
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("SignUp");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception if the FXML loading fails
            }
        }


    @FXML
    private void loginWithGoogle(ActionEvent event) {
        System.out.println("Connexion Google lancée...");
        Utilisateur U = new Utilisateur();
        try {
            com.google.api.services.oauth2.model.Userinfo user = tn.esprit.api.GoogleOAuthAPI.loginWithGoogle();
            System.out.println("Nom : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            U=utilisateurService.getUtilisateurByEmail(user.getEmail());
            if(U==null) {
                showAlert(Alert.AlertType.ERROR,"Error","Email ou Mot_de_passe incorrect");
            }
            else {
                showAlert(Alert.AlertType.INFORMATION, "connecte avec succes", "bienvenue, " + U.getNom() +" "+U.getPrenom()+" !");

                UtilisateurConnecte.getInstance().setUtilisateurConnecte(U);
                if(U.getRole().toString().equals("admin")){
                    navigateToBackAdmin();
                }
                else {
                    navigateToHome();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public LoginController() {
        System.out.println("LoginController chargé !");
    }


}


