package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import utils.AutoEmailService;
import utils.DatabaseInitializer;

import java.io.IOException;
import java.net.URL;

/**
 * Classe principale de l'application
 * Point d'entrée pour l'exécution de l'application JavaFX
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialiser la base de données
            System.out.println("Initialisation de la base de données...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("Base de données initialisée avec succès.");
            
            // Démarrer le service d'envoi automatique d'emails
            System.out.println("Démarrage du service d'envoi automatique d'emails...");
            AutoEmailService.start();
            
            // Essayer de charger le fichier FXML de plusieurs façons
            URL fxmlUrl = getClass().getResource("/MainMenu.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("/src/main/resources/MainMenu.fxml");
            }
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("MainMenu.fxml");
            }
            
            if (fxmlUrl == null) {
                throw new IOException("Impossible de trouver le fichier MainMenu.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            primaryStage.setTitle("Application de Covoiturage");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAndExit("Erreur au démarrage", "Une erreur est survenue lors du démarrage de l'application: " + e.getMessage());
        }
    }
    
    @Override
    public void stop() {
        // Arrêter le service d'envoi automatique d'emails
        AutoEmailService.stop();
        System.out.println("Service d'envoi automatique d'emails arrêté.");
        
        System.out.println("Application arrêtée.");
    }
    
    private void showErrorAndExit(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
        });
    }

    /**
     * Point d'entrée principal de l'application
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
