package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.EmailService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Contrôleur pour tester l'envoi d'emails
 */
public class TestEmailController {

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
    
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    
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
        
        // Envoyer l'email dans un thread séparé pour ne pas bloquer l'interface
        executorService.submit(() -> {
            boolean success = EmailService.sendEmail(email, sujet, message);
            
            // Mettre à jour l'interface dans le thread JavaFX
            Platform.runLater(() -> {
                if (success) {
                    updateStatus("Email envoyé avec succès !", Color.GREEN);
                } else {
                    updateStatus("Échec de l'envoi de l'email. Vérifiez la console pour plus de détails.", Color.RED);
                }
                btnEnvoyer.setDisable(false);
            });
        });
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
        updateStatus("Prêt à envoyer", Color.BLACK);
    }
}
