package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entites.Utilisateur;
import tn.esprit.entites.UtilisateurConnecte;
import tn.esprit.entites.UtilisateurSelectionne;
import tn.esprit.services.UtilisateurService;

import java.io.IOException;

public class Modifier_profile_controller {

    @FXML
    private Button Annuler;

    @FXML
    private TextField Email;

    @FXML
    private Button Enregistrer;

    @FXML
    private TextField UserName;

    @FXML
    private TextField age;

    @FXML
    private PasswordField confirmer_mot_de_passe;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_tel;

    @FXML
    private TextField prenom;
    public Utilisateur u = UtilisateurConnecte.getInstance().getUtilisateurConnecter();
    private Stage stage;
    private UtilisateurService userService = new UtilisateurService();
    @FXML
    public void initialize() {
        UserName.setText(u.getUsername());
        nom.setText(u.getNom());
        Email.setText(u.getEmail());
        prenom.setText(u.getPrenom());
        mot_de_passe.setText(u.getMot_de_passe());
        num_tel.setText(u.getNumero_tel());
        age.setText(String.valueOf(u.getAge()));
    }
    @FXML
    void Submit(ActionEvent event) {
        String username = UserName.getText();
        String Mot_de_passe = mot_de_passe.getText();
        String con_Mot_de_passe = confirmer_mot_de_passe.getText();
        String email = Email.getText();
        String Nom = nom.getText();
        String numero_tel = num_tel.getText();
        String PreNom = prenom.getText();
        int agee = Integer.parseInt(age.getText());


        // Validate input fields
        if (username.isEmpty() || Mot_de_passe.isEmpty() || email.isEmpty() || Nom.isEmpty() || numero_tel.isEmpty()|| PreNom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"ERREUR", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR,"ERREUR", "Invalid email format.");
            return;
        }

        if (!isValidPhoneNumber(numero_tel)) {
            showAlert(Alert.AlertType.ERROR,"ERREUR", "Invalid phone number format.");
            return;
        }

        if (!isStrongPassword(Mot_de_passe)) {
            showAlert(Alert.AlertType.ERROR,"ERREUR", "Password must be at least 8 characters long and include a mix of letters, numbers, and special characters.");
            return;
        }
        if (!con_Mot_de_passe.equals(Mot_de_passe)) {
            showAlert(Alert.AlertType.ERROR,"ERREUR", "Password not comfirmed correctly.");
            return;
        }

        // Update the selected user
        Utilisateur utilisateurconnecte = UtilisateurConnecte.getInstance().getUtilisateurConnecter();
        utilisateurconnecte.setUsername(username);
        utilisateurconnecte.setMot_de_passe(Mot_de_passe);
        utilisateurconnecte.setEmail(email);
        utilisateurconnecte.setNom(Nom);
        utilisateurconnecte.setPrenom(PreNom);
        utilisateurconnecte.setNumero_tel(numero_tel);
        utilisateurconnecte.setAge(agee);

        userService.modifier(utilisateurconnecte);
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProfile.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) Enregistrer.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{8}$"; // Assumes an 8-digit phone number
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isStrongPassword(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }



}
