package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.entites.Reclamation;
import tn.esprit.services.ReclamationService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReclamationController {

    @FXML
    private TextField sujetField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField reservationIdField;
    @FXML
    private TextField statutField;

    // Search fields from FXML
    @FXML
    private TextField searchSujetField;
    @FXML
    private TextField searchDescriptionField;
    @FXML
    private TextField searchReservationIdField;
    @FXML
    private ComboBox<String> searchStatutCombo;
    @FXML
    private Button searchButton;
    @FXML
    private Button resetButton;

    @FXML
    private TableView<Reclamation> tableViewReclamations;
    @FXML
    private TableColumn<Reclamation, Integer> idCol;
    @FXML
    private TableColumn<Reclamation, String> sujetCol;
    @FXML
    private TableColumn<Reclamation, String> descriptionCol;
    @FXML
    private TableColumn<Reclamation, Integer> reservationIdCol;
    @FXML
    private TableColumn<Reclamation, String> statutCol;

    @FXML
    private Button modifierBtn, supprimerBtn;

    private final ReclamationService service = new ReclamationService();
    private Reclamation selectedReclamation = null;
    private static final String UTILISATEUR = "admin"; // Simulé
    private List<Reclamation> allReclamations = new ArrayList<>(); // Stockage complet

    @FXML
    public void initialize() {
        // Set up TableView columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sujetCol.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        reservationIdCol.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Populate the ComboBox with status values
        searchStatutCombo.setItems(FXCollections.observableArrayList("EN_ATTENTE", "TRAITEE", "REJETEE"));

        // Load all reclamations
        afficherReclamations();

        // Disable buttons by default
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);

        // Handle table selection
        tableViewReclamations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            modifierBtn.setDisable(!isSelected);
            supprimerBtn.setDisable(!isSelected);

            if (isSelected) {
                selectedReclamation = newSelection;
                sujetField.setText(selectedReclamation.getSujet());
                descriptionArea.setText(selectedReclamation.getDescription());
                reservationIdField.setText(String.valueOf(selectedReclamation.getReservationId()));
                statutField.setText(selectedReclamation.getStatut());
            } else {
                clearForm();
            }
        });
    }

    private void afficherReclamations() {
        try {
            allReclamations = service.afficher();
            tableViewReclamations.setItems(FXCollections.observableArrayList(allReclamations));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement : " + e.getMessage());
        }
    }

    private void clearForm() {
        sujetField.clear();
        descriptionArea.clear();
        reservationIdField.clear();
        statutField.clear();
        selectedReclamation = null;
        tableViewReclamations.getSelectionModel().clearSelection();
    }

    private boolean validateInputs() {
        String sujet = sujetField.getText().trim();
        String description = descriptionArea.getText().trim();
        String reservationId = reservationIdField.getText().trim();
        String statut = statutField.getText().trim();

        if (sujet.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ requis", "Le sujet ne peut pas être vide.");
            return false;
        }
        if (description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ requis", "La description ne peut pas être vide.");
            return false;
        }
        if (reservationId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ requis", "L'ID de réservation est requis.");
            return false;
        }
        if (statut.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ requis", "Le statut est requis.");
            return false;
        }
        try {
            Integer.parseInt(reservationId);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'ID de réservation doit être un entier.");
            return false;
        }
        try {
            Reclamation.Statut.valueOf(statut);
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le statut doit être EN_ATTENTE, TRAITEE ou REJETEE.");
            return false;
        }
        return true;
    }

    @FXML
    private void ajouterReclamation() {
        if (!validateInputs()) return;

        Reclamation reclamation = new Reclamation(
                sujetField.getText().trim(),
                descriptionArea.getText().trim(),
                Integer.parseInt(reservationIdField.getText().trim())
        );
        reclamation.setStatut(statutField.getText().trim());

        try {
            service.ajouter(reclamation, UTILISATEUR);
            afficherReclamations();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'ajout", e.getMessage());
        }
    }

    @FXML
    private void modifierReclamation() {
        if (selectedReclamation == null || !validateInputs()) return;

        selectedReclamation.setSujet(sujetField.getText().trim());
        selectedReclamation.setDescription(descriptionArea.getText().trim());
        selectedReclamation.setReservationId(Integer.parseInt(reservationIdField.getText().trim()));
        selectedReclamation.setStatut(statutField.getText().trim());

        try {
            service.modifier(selectedReclamation, UTILISATEUR);
            afficherReclamations();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation modifiée avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de modification", e.getMessage());
        }
    }

    @FXML
    private void supprimerReclamation() {
        if (selectedReclamation == null) return;

        try {
            service.supprimer(selectedReclamation.getId(), UTILISATEUR);
            afficherReclamations();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation supprimée avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de suppression", e.getMessage());
        }
    }

    @FXML
    private void rechercherReclamations() {
        String sujet = searchSujetField.getText().toLowerCase().trim();
        String description = searchDescriptionField.getText().toLowerCase().trim();
        String reservationId = searchReservationIdField.getText().toLowerCase().trim();
        String statut = searchStatutCombo.getValue() != null ? searchStatutCombo.getValue().toLowerCase() : "";

        List<Reclamation> filtered = allReclamations.stream()
                .filter(r -> sujet.isEmpty() || r.getSujet().toLowerCase().contains(sujet))
                .filter(r -> description.isEmpty() || r.getDescription().toLowerCase().contains(description))
                .filter(r -> reservationId.isEmpty() || String.valueOf(r.getReservationId()).contains(reservationId))
                .filter(r -> statut.isEmpty() || r.getStatut().toLowerCase().contains(statut))
                .collect(Collectors.toList());

        tableViewReclamations.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void resetRecherche() {
        searchSujetField.clear();
        searchDescriptionField.clear();
        searchReservationIdField.clear();
        searchStatutCombo.setValue(null);
        afficherReclamations(); // Reload all reclamations
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}