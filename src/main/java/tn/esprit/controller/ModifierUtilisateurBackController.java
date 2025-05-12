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
import tn.esprit.entites.*;
import tn.esprit.services.UtilisateurService;

import java.io.IOException;

public class ModifierUtilisateurBackController {

    @FXML
    private TextField Email;

    @FXML
    private TextField Prenom;

    @FXML
    private Button SaveBtn;

    @FXML
    private TextField UserName;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_tel;

    public Utilisateur CurrentUser = UtilisateurSelectionne.getInstance().getSelectedUserr();
    private Stage stage;
    private UtilisateurService userService = new UtilisateurService();
    private AfficherUtilisateursBack afficherUtilisateursBack; // Reference to ShowUsersController

    @FXML
    public void initialize() {
        UserName.setText(CurrentUser.getUsername());
        nom.setText(CurrentUser.getNom());
        Email.setText(CurrentUser.getEmail());
        Prenom.setText(CurrentUser.getPrenom());
        mot_de_passe.setText(CurrentUser.getMot_de_passe());
        num_tel.setText(CurrentUser.getNumero_tel());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setShowUsersController(AfficherUtilisateursBack AafficherUtilisateursBack) {
        this.afficherUtilisateursBack = afficherUtilisateursBack;
    }

    @FXML
    void Save(ActionEvent event) {
        String username = UserName.getText();
        String Mot_de_passe = mot_de_passe.getText();
        String email = Email.getText();
        String Nom = nom.getText();
        String numero_tel = num_tel.getText();
        String PreNom = Prenom.getText();

        // Validate input fields
        if (username.isEmpty() || Mot_de_passe.isEmpty() || email.isEmpty() || Nom.isEmpty() || numero_tel.isEmpty()|| PreNom.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        if (!isValidPhoneNumber(numero_tel)) {
            showAlert("Error", "Invalid phone number format.");
            return;
        }

        if (!isStrongPassword(Mot_de_passe)) {
            showAlert("Error", "Password must be at least 8 characters long and include a mix of letters, numbers, and special characters.");
            return;
        }

        // Update the selected user
        Utilisateur updatedUser = UtilisateurSelectionne.getInstance().getSelectedUserr();
        updatedUser.setUsername(username);
        updatedUser.setMot_de_passe(Mot_de_passe);
        updatedUser.setEmail(email);
        updatedUser.setNom(Nom);
        updatedUser.setPrenom(PreNom);
        updatedUser.setNumero_tel(numero_tel);

        userService.modifier(updatedUser);

        // Update the logged-in user if it's the same user
        /*if (LoggedInUser.getInstance().getLoggedInUser() != null &&
                LoggedInUser.getInstance().getLoggedInUser().getUserId() == updatedUser.getUserId()) {
            LoggedInUser.getInstance().setLoggedInUser(updatedUser);
        }*/
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUtilisateurBack.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) SaveBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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