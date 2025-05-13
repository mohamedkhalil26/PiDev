package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Voiture;
import service.UtilisateurService;
import service.VoitureService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterVoitureController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbUtilisateur;

    @FXML
    private TextField txtNbPlaces;

    @FXML
    private TextField txtCouleur;

    @FXML
    private ComboBox<String> cmbTypeVoiture;

    @FXML
    private ComboBox<String> cmbCategorie;

    @FXML
    private ComboBox<String> cmbTypeCarburant;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnEnregistrer;

    private VoitureService voitureService;
    private UtilisateurService utilisateurService;
    private ListeVoitureController listeVoitureController;
    private ObservableList<Integer> utilisateurIds = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        voitureService = new VoitureService();
        utilisateurService = new UtilisateurService();

        // Charger les utilisateurs disponibles
        chargerUtilisateurs();

        // Initialiser les ComboBox si nécessaire
        if (cmbTypeVoiture.getItems().isEmpty()) {
            cmbTypeVoiture.getItems().addAll("Berline", "SUV", "Coupé", "Cabriolet", "Monospace", "Citadine");
        }

        if (cmbCategorie.getItems().isEmpty()) {
            cmbCategorie.getItems().addAll("Confort", "Économique", "Luxe", "Familiale", "Sport");
        }

        if (cmbTypeCarburant.getItems().isEmpty()) {
            cmbTypeCarburant.getItems().addAll("essence", "diesel", "hybride", "electrique");
        }
    }

    /**
     * Charge la liste des utilisateurs disponibles
     */
    private void chargerUtilisateurs() {
        List<Integer> ids = utilisateurService.getAllUtilisateurIds();
        utilisateurIds.clear();
        utilisateurIds.addAll(ids);
        cmbUtilisateur.setItems(utilisateurIds);

        if (!utilisateurIds.isEmpty()) {
            cmbUtilisateur.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    @FXML
    private void handleEnregistrer() {
        if (validerFormulaire()) {
            try {
                Integer utilisateurId = cmbUtilisateur.getValue();
                if (utilisateurId == null) {
                    afficherErreur("Veuillez sélectionner un utilisateur");
                    return;
                }

                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                String couleur = txtCouleur.getText().trim();
                String typeVoiture = cmbTypeVoiture.getValue();
                String categorie = cmbCategorie.getValue();
                String typeCarburant = cmbTypeCarburant.getValue();

                // Créer une nouvelle voiture (id = 0 car il sera généré par la base de données)
                Voiture nouvelleVoiture = new Voiture(0, utilisateurId, nbPlaces, couleur, typeVoiture, categorie, typeCarburant);

                boolean success = voitureService.ajouterVoiture(nouvelleVoiture);

                if (success) {
                    afficherConfirmation("Voiture ajoutée avec succès");
                    if (listeVoitureController != null) {
                        listeVoitureController.rafraichirTableau();
                    }
                    fermerFenetre();
                } else {
                    afficherErreur("Erreur lors de l'ajout de la voiture");
                }

            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer des valeurs numériques valides pour le nombre de places");
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (cmbUtilisateur.getValue() == null) {
            erreurs.append("- Veuillez sélectionner un utilisateur\n");
        }

        if (txtNbPlaces.getText().trim().isEmpty()) {
            erreurs.append("- Le nombre de places est requis\n");
        } else {
            try {
                Integer.parseInt(txtNbPlaces.getText().trim());
            } catch (NumberFormatException e) {
                erreurs.append("- Le nombre de places doit être un nombre\n");
            }
        }

        if (txtCouleur.getText().trim().isEmpty()) {
            erreurs.append("- La couleur est requise\n");
        }

        if (cmbTypeVoiture.getValue() == null) {
            erreurs.append("- Le type de voiture est requis\n");
        }

        if (cmbCategorie.getValue() == null) {
            erreurs.append("- La catégorie est requise\n");
        }

        if (cmbTypeCarburant.getValue() == null) {
            erreurs.append("- Le type de carburant est requis\n");
        }

        if (erreurs.length() > 0) {
            afficherErreur("Veuillez corriger les erreurs suivantes :\n" + erreurs.toString());
            return false;
        }

        return true;
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
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    public void setListeVoitureController(ListeVoitureController controller) {
        this.listeVoitureController = controller;
    }
}
