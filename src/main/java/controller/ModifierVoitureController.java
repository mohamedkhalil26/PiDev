package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Voiture;
import service.VoitureService;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierVoitureController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtUtilisateurId;

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
    private Voiture voiture;
    private ListeVoitureController listeVoitureController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        voitureService = new VoitureService();

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

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
        remplirFormulaire();
    }

    private void remplirFormulaire() {
        if (voiture != null) {
            txtId.setText(String.valueOf(voiture.getId()));
            txtUtilisateurId.setText(String.valueOf(voiture.getUtilisateurId()));
            txtNbPlaces.setText(String.valueOf(voiture.getNbPlaces()));
            txtCouleur.setText(voiture.getCouleur());
            cmbTypeVoiture.setValue(voiture.getTypeVoiture());
            cmbCategorie.setValue(voiture.getCategorie());
            cmbTypeCarburant.setValue(voiture.getTypeCarburant());
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
                int id = Integer.parseInt(txtId.getText().trim());
                int utilisateurId = Integer.parseInt(txtUtilisateurId.getText().trim());
                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                String couleur = txtCouleur.getText().trim();
                String typeVoiture = cmbTypeVoiture.getValue();
                String categorie = cmbCategorie.getValue();
                String typeCarburant = cmbTypeCarburant.getValue();

                // Mettre à jour la voiture
                voiture.setNbPlaces(nbPlaces);
                voiture.setCouleur(couleur);
                voiture.setTypeVoiture(typeVoiture);
                voiture.setCategorie(categorie);
                voiture.setTypeCarburant(typeCarburant);

                boolean success = voitureService.modifierVoiture(voiture);

                if (success) {
                    afficherConfirmation("Voiture modifiée avec succès");
                    if (listeVoitureController != null) {
                        listeVoitureController.rafraichirTableau();
                    }
                    fermerFenetre();
                } else {
                    afficherErreur("Erreur lors de la modification de la voiture");
                }

            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer des valeurs numériques valides pour le nombre de places");
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

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
