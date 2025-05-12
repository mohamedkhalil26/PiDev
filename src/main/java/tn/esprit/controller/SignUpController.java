package tn.esprit.controller;

import com.google.api.services.oauth2.model.Userinfo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entites.Role;
import tn.esprit.entites.Utilisateur;
import tn.esprit.services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private Button AddAdmin;

    @FXML
    private TextField Email;

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

    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private void initialize() {
        roleChoiceBox.setItems(FXCollections.observableArrayList("chauffeur", "passager"));
        roleChoiceBox.setValue("passager");
    }
    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Simple phone number validation regex (adjust based on your requirements)
        String phoneRegex = "^[0-9]{8}$"; // Assumes a 10-digit phone number
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isStrongPassword(String password) {
        // Password strength validation: at least 8 characters, including letters, numbers, and special characters
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Submit(javafx.event.ActionEvent actionEvent) throws SQLException {
        // Collect data from input fields
        String username = UserName.getText();
        String Nom = nom.getText();
        String Prenom = prenom.getText();
        String ageValue = age.getText();
        int age_int = Integer.parseInt(age.getText());
        String email=Email.getText();
        String numero_tel= num_tel.getText();
        String Mot_de_passe = mot_de_passe.getText();
        String Con_mot_de_passe = confirmer_mot_de_passe.getText();
        String selectedRole = roleChoiceBox.getValue();




        // Validate input fields
        if (username.isEmpty() || Nom.isEmpty() || Prenom.isEmpty() || email.isEmpty() || numero_tel.isEmpty() || Mot_de_passe.isEmpty()|| Con_mot_de_passe.isEmpty() ||ageValue.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR,"Error", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR,"Error", "Invalid email format.");
            return;
        }

        if (!isValidPhoneNumber(numero_tel)) {
            showAlert(Alert.AlertType.ERROR,"Error", "Invalid phone number format.");
            return;
        }

        if (!Mot_de_passe.equals(Con_mot_de_passe)) {
            showAlert(Alert.AlertType.ERROR,"Error", "Passwords do not match.");
            return;
        }

        if (!isStrongPassword(Mot_de_passe)) {
            showAlert(Alert.AlertType.ERROR,"Error", "Password must be at least 8 characters long and include a mix of letters, numbers, and special characters.");
            return;
        }

        // Create a new User object
        Utilisateur newUser = new Utilisateur(Nom, Prenom, age_int, Role.valueOf(selectedRole), email, numero_tel, username,Mot_de_passe);

        // Use UserService to create the user

        UtilisateurService utilisateurService = new UtilisateurService();
        utilisateurService.ajouterP(newUser);

        // Show success message
        showAlert("Success", "user created successfully.");
        NavigateToAddAdmin();
    }
    void NavigateToAddAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddAdmin.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the home screen.");
        }
    }

    @FXML
    private Button googleButton;
    @FXML
    void Google(ActionEvent event) {
        try {
            Userinfo userInfo = tn.esprit.api.GoogleOAuthAPI.loginWithGoogle();

            if (userInfo != null) {
                // Assuming userInfo.getName() = "First Last"
                String[] names = userInfo.getName().split(" ", 2);
                if (names.length >= 2) {
                    prenom.setText(names[0]);
                    nom.setText(names[1]);
                } else {
                    prenom.setText(names[0]);
                }

                Email.setText(userInfo.getEmail());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed", "An error occurred.");
        }
    }

}
