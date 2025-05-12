package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.example.entities.Voiture;
import org.example.services.VoitureService;
import org.example.services.ConducteurService;
import org.example.entities.Conducteur;

import java.sql.SQLException;
import java.util.List;

public class VoitureController {
    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField immatriculationField;
    @FXML private TextField nombre_placesField;
    @FXML private TextField idConducteurVoitureField;
    @FXML private TextField searchMarqueField;
    @FXML private TextField searchModeleField;
    @FXML private TextField searchImmatriculationField;
    @FXML private TableView<Voiture> voitureTable;
    @FXML private Label errorLabel;

    private final ObservableList<Voiture> voitureData = FXCollections.observableArrayList();
    private int voitureIdToUpdate = -1;
    private final VoitureService voitureService;
    private final ConducteurService conducteurService;

    public VoitureController() {
        voitureService = new VoitureService();
        conducteurService = new ConducteurService();
    }

    @FXML
    public void initialize() {
        setupVoitureTable();
        showVoitures();
    }

    private void setupVoitureTable() {
        TableColumn<Voiture, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id_voiture"));

        TableColumn<Voiture, String> marqueCol = new TableColumn<>("Marque");
        marqueCol.setCellValueFactory(new PropertyValueFactory<>("marque"));

        TableColumn<Voiture, String> modeleCol = new TableColumn<>("Modèle");
        modeleCol.setCellValueFactory(new PropertyValueFactory<>("modele"));

        TableColumn<Voiture, String> immatriculationCol = new TableColumn<>("Immatriculation");
        immatriculationCol.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));

        TableColumn<Voiture, Integer> placesCol = new TableColumn<>("Places");
        placesCol.setCellValueFactory(new PropertyValueFactory<>("nombre_places"));

        TableColumn<Voiture, Integer> idConducteurCol = new TableColumn<>("Conducteur ID");
        idConducteurCol.setCellValueFactory(new PropertyValueFactory<>("id_conducteur"));

        voitureTable.getColumns().setAll(idCol, marqueCol, modeleCol, immatriculationCol, placesCol, idConducteurCol);
        voitureTable.setItems(voitureData);
    }

    @FXML
    private void addVoiture() {
        if (!isValidNumeric(nombre_placesField.getText()) || !isValidNumeric(idConducteurVoitureField.getText())) {
            errorLabel.setText("Entrée Invalide: Les champs Places et ID Conducteur doivent être numériques.");
            return;
        }

        String marque = marqueField.getText().trim();
        String modele = modeleField.getText().trim();
        String immatriculation = immatriculationField.getText().trim();

        if (marque.isEmpty() || modele.isEmpty() || immatriculation.isEmpty()) {
            errorLabel.setText("Champs Vides: Tous les champs doivent être remplis.");
            return;
        }

        int nombrePlaces = Integer.parseInt(nombre_placesField.getText());
        int idConducteur = Integer.parseInt(idConducteurVoitureField.getText());

        try {
            // Vérification de l'existence du conducteur
            List<Conducteur> conducteurs;
            try {
                conducteurs = conducteurService.getAllConducteurs();
            } catch (SQLException e) {
                errorLabel.setText("Erreur SQL: Impossible de vérifier le conducteur - " + e.getMessage());
                return;
            } catch (Exception e) {
                errorLabel.setText("Erreur Service: Impossible de récupérer la liste des conducteurs - " + e.getMessage());
                return;
            }
            boolean conducteurExists = conducteurs.stream()
                    .anyMatch(c -> c.getId_conducteur() == idConducteur);
            if (!conducteurExists) {
                errorLabel.setText("Conducteur Inexistant: ID Conducteur " + idConducteur + " n'existe pas.");
                return;
            }

            Voiture voiture = new Voiture(
                    voitureIdToUpdate == -1 ? 0 : voitureIdToUpdate,
                    immatriculation, marque, modele, nombrePlaces, idConducteur
            );

            try {
                if (voitureIdToUpdate == -1) {
                    if (voitureService.ajouterVoiture(voiture)) {
                        errorLabel.setText("Succès: Voiture ajoutée avec succès.");
                    } else {
                        errorLabel.setText("Erreur: Échec de l'ajout de la voiture.");
                    }
                } else {
                    if (voitureService.updateVoiture(voiture)) {
                        errorLabel.setText("Succès: Voiture mise à jour avec succès.");
                        voitureIdToUpdate = -1;
                    } else {
                        errorLabel.setText("Erreur: Échec de la mise à jour de la voiture.");
                    }
                }
                showVoitures();
                clearVoitureFields();
            } catch (SQLException e) {
                errorLabel.setText("Erreur SQL: " + e.getMessage());
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void showVoitures() {
        voitureData.clear();
        try {
            voitureData.addAll(voitureService.getAllVoitures());
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la récupération des voitures - " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerVoiture() {
        Voiture selected = voitureTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à supprimer.");
            return;
        }
        try {
            if (voitureService.deleteVoiture(selected.getId_voiture())) {
                errorLabel.setText("Succès: Voiture supprimée avec succès.");
                showVoitures();
            } else {
                errorLabel.setText("Erreur: Échec de la suppression de la voiture.");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void modifierVoiture() {
        Voiture selected = voitureTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à modifier.");
            return;
        }
        marqueField.setText(selected.getMarque());
        modeleField.setText(selected.getModele());
        immatriculationField.setText(selected.getImmatriculation());
        nombre_placesField.setText(String.valueOf(selected.getNombre_places()));
        idConducteurVoitureField.setText(String.valueOf(selected.getId_conducteur()));
        voitureIdToUpdate = selected.getId_voiture();
    }

    @FXML
    private void searchVoitures() {
        voitureData.clear();
        String marque = searchMarqueField.getText().trim();
        String modele = searchModeleField.getText().trim();
        String immatriculation = searchImmatriculationField.getText().trim();

        try {
            List<Voiture> allVoitures = voitureService.getAllVoitures();
            allVoitures.stream()
                    .filter(v -> (marque.isEmpty() || v.getMarque().toLowerCase().contains(marque.toLowerCase())) &&
                            (modele.isEmpty() || v.getModele().toLowerCase().contains(modele.toLowerCase())) &&
                            (immatriculation.isEmpty() || v.getImmatriculation().toLowerCase().contains(immatriculation.toLowerCase())))
                    .forEach(voitureData::add);

            if (voitureData.isEmpty()) {
                errorLabel.setText("Aucun Résultat: Aucune voiture trouvée.");
            } else {
                errorLabel.setText("");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la recherche des voitures - " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    private boolean isValidNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearVoitureFields() {
        marqueField.clear();
        modeleField.clear();
        immatriculationField.clear();
        nombre_placesField.clear();
        idConducteurVoitureField.clear();
        voitureIdToUpdate = -1;
        errorLabel.setText("");
    }
}