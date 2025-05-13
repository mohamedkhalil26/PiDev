package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.EmailServiceSimulator;

/**
 * Contrôleur pour tester l'envoi d'emails avec le simulateur
 */
public class TestEmailSimulatorController {

    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextField txtSujet;
    
    @FXML
    private TextArea txtMessage;
    
    @FXML
    private Button btnEnvoyer;
    
    @FXML
    private Button btnAnnuler;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private void handleEnvoyer() {
        // Récupérer les valeurs des champs
        String email = txtEmail.getText().trim();
        String sujet = txtSujet.getText().trim();
        String message = txtMessage.getText();
        
        // Vérifier que les champs ne sont pas vides
        if (email.isEmpty() || sujet.isEmpty() || message.isEmpty()) {
            updateStatus("Veuillez remplir tous les champs", Color.RED);
            return;
        }
        
        // Désactiver le bouton pendant l'envoi
        btnEnvoyer.setDisable(true);
        updateStatus("Envoi en cours...", Color.ORANGE);
        
        // Simuler l'envoi d'email (affichage dans la console)
        boolean success = EmailServiceSimulator.sendEmail(email, sujet, message);
        
        if (success) {
            updateStatus("Email simulé avec succès ! Vérifiez la console.", Color.GREEN);
        } else {
            updateStatus("Échec de la simulation d'email.", Color.RED);
        }
        btnEnvoyer.setDisable(false);
    }
    
    @FXML
    private void handleAnnuler() {
        // Fermer la fenêtre
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
    
    private void updateStatus(String message, Color color) {
        lblStatus.setText(message);
        lblStatus.setTextFill(color);
    }
    
    public void initialize() {
        // Initialisation du contrôleur
        updateStatus("Prêt à simuler l'envoi d'email", Color.BLACK);
        
        // Pré-remplir l'adresse email de test
        txtEmail.setText("takwabouabid149@gmail.com");
        txtSujet.setText("Test de simulation d'email");
        txtMessage.setText("<h2>Test de simulation d'email</h2>\n<p>Ceci est un message de test pour vérifier que la simulation d'email fonctionne correctement.</p>\n<p>Ce message sera affiché dans la console au lieu d'être envoyé par email.</p>");
    }
}
