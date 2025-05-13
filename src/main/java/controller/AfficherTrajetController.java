package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Trajet;
import model.Voiture;
import service.TrajetService;
import service.VoitureService;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AfficherTrajetController implements Initializable {

    @FXML
    private Label lblId;
    
    @FXML
    private Label lblVilleDepart;
    
    @FXML
    private Label lblVilleArrivee;
    
    @FXML
    private Label lblDateHeure;
    
    @FXML
    private Label lblNbPlaces;
    
    @FXML
    private Label lblPrix;
    
    @FXML
    private Label lblConducteur;
    
    @FXML
    private Label lblVoiture;
    
    @FXML
    private Label lblStatut;
    
    @FXML
    private Button btnModifier;
    
    @FXML
    private Button btnSupprimer;
    
    @FXML
    private Button btnRetour;
    
    private Trajet trajet;
    private TrajetService trajetService;
    private VoitureService voitureService;
    private ListeTrajetController listeTrajetController;
    
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trajetService = new TrajetService();
        voitureService = new VoitureService();
    }
    
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        afficherDetails();
    }
    
    private void afficherDetails() {
        if (trajet != null) {
            lblId.setText(String.valueOf(trajet.getId()));
            lblVilleDepart.setText(trajet.getVilleDepart());
            lblVilleArrivee.setText(trajet.getVilleArrivee());
            lblDateHeure.setText(dateTimeFormatter.format(trajet.getDateHeureDepart()));
            lblNbPlaces.setText(String.valueOf(trajet.getNbPlacesDisponibles()));
            lblPrix.setText(String.format("%.2f €", trajet.getPrixParPlace()));
            lblConducteur.setText("ID: " + trajet.getConducteurId());
            
            // Récupérer les détails de la voiture
            try {
                Voiture voiture = voitureService.getAllVoitures().stream()
                        .filter(v -> v.getId() == trajet.getVoitureId())
                        .findFirst()
                        .orElse(null);
                
                if (voiture != null) {
                    lblVoiture.setText(voiture.getTypeVoiture() + " - " + voiture.getCouleur());
                } else {
                    lblVoiture.setText("ID: " + trajet.getVoitureId());
                }
            } catch (Exception e) {
                lblVoiture.setText("ID: " + trajet.getVoitureId());
            }
            
            lblStatut.setText(trajet.getStatut());
        }
    }
    
    @FXML
    private void handleModifier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml"));
            Parent root = loader.load();
            
            ModifierTrajetController controller = loader.getController();
            controller.setTrajet(trajet);
            controller.setListeTrajetController(listeTrajetController);
            
            Stage currentStage = (Stage) btnModifier.getScene().getWindow();
            currentStage.close();
            
            Stage stage = new Stage();
            stage.setTitle("Modifier un trajet");
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
        alert.setHeaderText("Supprimer le trajet");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = trajetService.supprimerTrajet(trajet.getId());
                if (success) {
                    if (listeTrajetController != null) {
                        listeTrajetController.afficherTrajet();
                    }
                    fermerFenetre();
                    afficherConfirmation("Trajet supprimé avec succès");
                } else {
                    afficherErreur("Erreur lors de la suppression du trajet");
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
    
    public void setListeTrajetController(ListeTrajetController controller) {
        this.listeTrajetController = controller;
    }
}
