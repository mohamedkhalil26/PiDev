package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Voiture;
import service.VoitureService;
import utils.EmailServiceSimulator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AfficherVoitureController implements Initializable {

    @FXML
    private Label lblId;

    @FXML
    private Label lblUtilisateurId;

    @FXML
    private Label lblNbPlaces;

    @FXML
    private Label lblCouleur;

    @FXML
    private Label lblTypeVoiture;

    @FXML
    private Label lblCategorie;

    @FXML
    private Label lblTypeCarburant;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> cmbTypeNotification;

    @FXML
    private Button btnEnvoyerEmail;

    @FXML
    private Label lblStatusEmail;

    @FXML
    private ListView<String> listViewEmails;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnRetour;

    private ObservableList<String> emailsEnvoyes = FXCollections.observableArrayList();

    private Voiture voiture;
    private VoitureService voitureService;
    private ListeVoitureController listeVoitureController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        voitureService = new VoitureService();

        // Initialiser la liste des emails
        listViewEmails.setItems(emailsEnvoyes);

        // Sélectionner le premier type de notification par défaut
        if (cmbTypeNotification.getItems().size() > 0) {
            cmbTypeNotification.getSelectionModel().selectFirst();
        }

        // Initialiser l'email par défaut
        txtEmail.setText("takwabouabid149@gmail.com");
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
        afficherDetails();
    }

    private void afficherDetails() {
        if (voiture != null) {
            lblId.setText(String.valueOf(voiture.getId()));
            lblUtilisateurId.setText(String.valueOf(voiture.getUtilisateurId()));
            lblNbPlaces.setText(String.valueOf(voiture.getNbPlaces()));
            lblCouleur.setText(voiture.getCouleur());
            lblTypeVoiture.setText(voiture.getTypeVoiture());
            lblCategorie.setText(voiture.getCategorie());
            lblTypeCarburant.setText(voiture.getTypeCarburant());
        }
    }

    @FXML
    private void handleModifier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierVoiture.fxml"));
            Parent root = loader.load();

            ModifierVoitureController controller = loader.getController();
            controller.setVoiture(voiture);
            controller.setListeVoitureController(listeVoitureController);

            Stage currentStage = (Stage) btnModifier.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            stage.setTitle("Modifier une voiture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture du formulaire de modification");
        }
    }

    @FXML
    private void handleSupprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = voitureService.supprimerVoiture(voiture.getId());
                if (success) {
                    if (listeVoitureController != null) {
                        listeVoitureController.rafraichirTableau();
                    }
                    fermerFenetre();
                    afficherConfirmation("Voiture supprimée avec succès");
                } else {
                    afficherErreur("Erreur lors de la suppression de la voiture");
                }
            }
        });
    }

    @FXML
    private void handleRetour() {
        fermerFenetre();
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleEnvoyerEmail() {
        String email = txtEmail.getText().trim();
        String typeNotification = cmbTypeNotification.getValue();

        if (email.isEmpty()) {
            updateStatusEmail("Veuillez entrer une adresse email", Color.RED);
            return;
        }

        if (typeNotification == null) {
            updateStatusEmail("Veuillez sélectionner un type de notification", Color.RED);
            return;
        }

        // Désactiver le bouton pendant l'envoi
        btnEnvoyerEmail.setDisable(true);
        updateStatusEmail("Envoi en cours...", Color.ORANGE);

        // Préparer le contenu de l'email en fonction du type de notification
        String sujet = "";
        String contenu = "";

        switch (typeNotification) {
            case "Confirmation de réservation":
                sujet = "Confirmation de réservation de voiture";
                contenu = genererContenuReservation();
                break;
            case "Rappel avant départ":
                sujet = "Rappel : Votre départ approche";
                contenu = genererContenuRappel();
                break;
            case "Notification d'entretien":
                sujet = "Rappel d'entretien pour votre véhicule";
                contenu = genererContenuEntretien();
                break;
            default:
                sujet = "Notification concernant votre véhicule";
                contenu = genererContenuGeneral();
        }

        // Simuler l'envoi d'email (affichage dans la console)
        boolean success = EmailServiceSimulator.sendEmail(email, sujet, contenu);

        if (success) {
            updateStatusEmail("Email envoyé avec succès !", Color.GREEN);
            // Ajouter l'email à l'historique
            ajouterEmailHistorique(typeNotification, email);
        } else {
            updateStatusEmail("Échec de l'envoi de l'email.", Color.RED);
        }

        btnEnvoyerEmail.setDisable(false);
    }

    private void updateStatusEmail(String message, Color color) {
        lblStatusEmail.setText(message);
        lblStatusEmail.setTextFill(color);
    }

    private void ajouterEmailHistorique(String type, String destinataire) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = timestamp + " - " + type + " envoyé à " + destinataire;

        emailsEnvoyes.add(0, entry); // Ajouter au début de la liste
    }

    private String genererContenuReservation() {
        return "<h2>Confirmation de réservation</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Nous confirmons la réservation de votre véhicule :</p>" +
               "<ul>" +
               "<li><b>Type de véhicule :</b> " + voiture.getTypeVoiture() + "</li>" +
               "<li><b>Catégorie :</b> " + voiture.getCategorie() + "</li>" +
               "<li><b>Couleur :</b> " + voiture.getCouleur() + "</li>" +
               "<li><b>Nombre de places :</b> " + voiture.getNbPlaces() + "</li>" +
               "<li><b>Type de carburant :</b> " + voiture.getTypeCarburant() + "</li>" +
               "</ul>" +
               "<p>Merci de votre confiance.</p>";
    }

    private String genererContenuRappel() {
        return "<h2>Rappel de départ</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Nous vous rappelons que votre départ est prévu pour demain.</p>" +
               "<p>Détails du véhicule :</p>" +
               "<ul>" +
               "<li><b>Type de véhicule :</b> " + voiture.getTypeVoiture() + "</li>" +
               "<li><b>Catégorie :</b> " + voiture.getCategorie() + "</li>" +
               "<li><b>Couleur :</b> " + voiture.getCouleur() + "</li>" +
               "</ul>" +
               "<p>Nous vous souhaitons un excellent voyage.</p>";
    }

    private String genererContenuEntretien() {
        return "<h2>Rappel d'entretien</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Nous vous rappelons qu'il est temps de faire l'entretien de votre véhicule :</p>" +
               "<ul>" +
               "<li><b>Type de véhicule :</b> " + voiture.getTypeVoiture() + "</li>" +
               "<li><b>Type de carburant :</b> " + voiture.getTypeCarburant() + "</li>" +
               "</ul>" +
               "<p>Un entretien régulier permet de maintenir votre véhicule en bon état et d'optimiser sa consommation de carburant.</p>";
    }

    private String genererContenuGeneral() {
        return "<h2>Information sur votre véhicule</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Voici les informations concernant votre véhicule :</p>" +
               "<ul>" +
               "<li><b>ID :</b> " + voiture.getId() + "</li>" +
               "<li><b>Type de véhicule :</b> " + voiture.getTypeVoiture() + "</li>" +
               "<li><b>Catégorie :</b> " + voiture.getCategorie() + "</li>" +
               "<li><b>Couleur :</b> " + voiture.getCouleur() + "</li>" +
               "<li><b>Nombre de places :</b> " + voiture.getNbPlaces() + "</li>" +
               "<li><b>Type de carburant :</b> " + voiture.getTypeCarburant() + "</li>" +
               "</ul>" +
               "<p>N'hésitez pas à nous contacter pour toute information complémentaire.</p>";
    }

    private void afficherConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    public void setListeVoitureController(ListeVoitureController controller) {
        this.listeVoitureController = controller;
    }
}
