package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.EmpreinteCarbone;
import model.Trajet;
import service.EmpreinteCarboneService;
import service.TrajetService;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la réservation de places sur un trajet
 */
public class ReserverTrajetController implements Initializable {

    @FXML
    private ComboBox<Trajet> cmbTrajet;
    
    @FXML
    private Spinner<Integer> spnNombrePlaces;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private Label lblDepart;
    
    @FXML
    private Label lblArrivee;
    
    @FXML
    private Label lblDateHeure;
    
    @FXML
    private Label lblPlacesDisponibles;
    
    @FXML
    private Label lblPrix;
    
    @FXML
    private Label lblPrixTotal;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private Button btnReserver;
    
    private TrajetService trajetService;
    private EmpreinteCarboneService empreinteCarboneService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trajetService = new TrajetService();
        empreinteCarboneService = new EmpreinteCarboneService();
        
        // Configurer le Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spnNombrePlaces.setValueFactory(valueFactory);
        
        // Ajouter un écouteur pour mettre à jour le prix total
        spnNombrePlaces.valueProperty().addListener((obs, oldVal, newVal) -> {
            updatePrixTotal();
        });
        
        // Ajouter un écouteur pour le changement de trajet sélectionné
        cmbTrajet.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateTrajetDetails(newVal);
            } else {
                clearTrajetDetails();
            }
        });
        
        // Charger les trajets disponibles
        loadTrajets();
    }
    
    /**
     * Charge les trajets disponibles dans le ComboBox
     */
    private void loadTrajets() {
        try {
            List<Trajet> trajets = trajetService.getAllTrajets();
            
            // Filtrer pour ne garder que les trajets avec des places disponibles et statut "planifié"
            trajets.removeIf(t -> t.getNbPlacesDisponibles() <= 0 || !t.getStatut().equals("planifié"));
            
            cmbTrajet.setItems(FXCollections.observableArrayList(trajets));
            
            // Configurer l'affichage des trajets dans le ComboBox
            cmbTrajet.setCellFactory(param -> new ListCell<Trajet>() {
                @Override
                protected void updateItem(Trajet item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getVilleDepart() + " → " + item.getVilleArrivee() + 
                                " (" + item.getDateHeureDepart().format(dateTimeFormatter) + ")");
                    }
                }
            });
            
            // Même chose pour l'affichage du trajet sélectionné
            cmbTrajet.setButtonCell(new ListCell<Trajet>() {
                @Override
                protected void updateItem(Trajet item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getVilleDepart() + " → " + item.getVilleArrivee() + 
                                " (" + item.getDateHeureDepart().format(dateTimeFormatter) + ")");
                    }
                }
            });
            
            if (!trajets.isEmpty()) {
                cmbTrajet.getSelectionModel().selectFirst();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement des trajets: " + e.getMessage());
        }
    }
    
    /**
     * Met à jour les détails affichés pour un trajet
     */
    private void updateTrajetDetails(Trajet trajet) {
        lblDepart.setText(trajet.getVilleDepart());
        lblArrivee.setText(trajet.getVilleArrivee());
        lblDateHeure.setText(trajet.getDateHeureDepart().format(dateTimeFormatter));
        lblPlacesDisponibles.setText(String.valueOf(trajet.getNbPlacesDisponibles()));
        lblPrix.setText(String.format("%.2f €", trajet.getPrixParPlace()));
        
        // Mettre à jour le prix total
        updatePrixTotal();
        
        // Limiter le nombre de places à réserver
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, trajet.getNbPlacesDisponibles(), 1);
        spnNombrePlaces.setValueFactory(valueFactory);
    }
    
    /**
     * Efface les détails du trajet
     */
    private void clearTrajetDetails() {
        lblDepart.setText("--");
        lblArrivee.setText("--");
        lblDateHeure.setText("--");
        lblPlacesDisponibles.setText("--");
        lblPrix.setText("--");
        lblPrixTotal.setText("--");
    }
    
    /**
     * Met à jour le prix total en fonction du nombre de places
     */
    private void updatePrixTotal() {
        Trajet trajet = cmbTrajet.getValue();
        if (trajet != null && spnNombrePlaces.getValue() != null) {
            double prixTotal = trajet.getPrixParPlace() * spnNombrePlaces.getValue();
            lblPrixTotal.setText(String.format("%.2f €", prixTotal));
        } else {
            lblPrixTotal.setText("--");
        }
    }
    
    /**
     * Gère le clic sur le bouton Réserver
     */
    @FXML
    private void handleReserver() {
        if (!validateInputs()) {
            return;
        }
        
        Trajet trajet = cmbTrajet.getValue();
        int nombrePlaces = spnNombrePlaces.getValue();
        String email = txtEmail.getText().trim();
        
        try {
            boolean success = trajetService.reserverPlaces(trajet.getId(), email, nombrePlaces);
            
            if (success) {
                showInfo("Réservation effectuée avec succès!");
                
                // Fermer la fenêtre après un court délai
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> {
                            Stage stage = (Stage) btnReserver.getScene().getWindow();
                            stage.close();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showError("Erreur lors de la réservation. Veuillez réessayer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la réservation: " + e.getMessage());
        }
    }
    
    /**
     * Gère le clic sur le bouton Annuler
     */
    /**
     * Valide les entrées utilisateur
     */
    private boolean validateInputs() {
        StringBuilder erreurs = new StringBuilder();
        
        if (cmbTrajet.getValue() == null) {
            erreurs.append("- Veuillez sélectionner un trajet\n");
        }
        
        if (spnNombrePlaces.getValue() == null || spnNombrePlaces.getValue() < 1) {
            erreurs.append("- Le nombre de places doit être au moins 1\n");
        }
        
        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            erreurs.append("- L'email est requis\n");
        } else if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            erreurs.append("- L'email n'est pas valide\n");
        }
        
        if (erreurs.length() > 0) {
            showError("Veuillez corriger les erreurs suivantes:\n" + erreurs.toString());
            return false;
        }
        
        return true;
    }
    
    /**
     * Affiche un message d'erreur
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche un message d'information
     */
    private void showInfo(String message) {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    }
}
