package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.example.entities.Conducteur;
import org.example.services.ConducteurService;

import java.sql.SQLException;
import java.util.List;

public class ConducteurController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;
    @FXML private TextField searchNomField;
    @FXML private TextField searchPrenomField;
    @FXML private TextField searchEmailField;
    @FXML private TextField searchTelephoneField; // champ de recherche ajouté
    @FXML private TableView<Conducteur> conducteurTable;
    @FXML private Label errorLabel;

    private final ObservableList<Conducteur> conducteurData = FXCollections.observableArrayList();
    private int conducteurIdToUpdate = -1;
    private final ConducteurService conducteurService;

    public ConducteurController() {
        conducteurService = new ConducteurService();
    }

    @FXML
    public void initialize() {
        setupConducteurTable();
        showConducteurs();
    }

    private void setupConducteurTable() {
        TableColumn<Conducteur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id_conducteur"));

        TableColumn<Conducteur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Conducteur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Conducteur, String> telephoneCol = new TableColumn<>("Téléphone");
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        TableColumn<Conducteur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        conducteurTable.getColumns().setAll(idCol, nomCol, prenomCol, telephoneCol, emailCol);
        conducteurTable.setItems(conducteurData);
    }

    @FXML
    private void addConducteur() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            errorLabel.setText("Champs Vides: Tous les champs doivent être remplis.");
            return;
        }

        Conducteur conducteur = new Conducteur(
                conducteurIdToUpdate == -1 ? 0 : conducteurIdToUpdate,
                nom, prenom, telephone, email
        );

        try {
            if (conducteurIdToUpdate == -1) {
                boolean added = conducteurService.ajouterConducteur(conducteur);
                if (added) {
                    errorLabel.setText("Succès: Conducteur ajouté avec succès.");
                } else {
                    errorLabel.setText("Erreur: Un conducteur avec cet email existe déjà.");
                }
            } else {
                boolean updated = conducteurService.updateConducteur(conducteur);
                if (updated) {
                    errorLabel.setText("Succès: Conducteur mis à jour avec succès.");
                    conducteurIdToUpdate = -1;
                } else {
                    errorLabel.setText("Erreur: Échec de la mise à jour du conducteur.");
                }
            }
            showConducteurs();
            clearConducteurFields();
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void showConducteurs() {
        conducteurData.clear();
        try {
            conducteurData.addAll(conducteurService.getAllConducteurs());
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la récupération des conducteurs - " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerConducteur() {
        Conducteur selected = conducteurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à supprimer.");
            return;
        }
        try {
            if (conducteurService.deleteConducteur(selected.getId_conducteur())) {
                errorLabel.setText("Succès: Conducteur supprimé avec succès.");
                showConducteurs();
            } else {
                errorLabel.setText("Erreur: Échec de la suppression du conducteur.");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void modifierConducteur() {
        Conducteur selected = conducteurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à modifier.");
            return;
        }
        nomField.setText(selected.getNom());
        prenomField.setText(selected.getPrenom());
        telephoneField.setText(selected.getTelephone());
        emailField.setText(selected.getEmail());
        conducteurIdToUpdate = selected.getId_conducteur();
    }

    @FXML
    private void searchConducteurs() {
        conducteurData.clear();

        String nom = searchNomField.getText().trim();
        String prenom = searchPrenomField.getText().trim();
        String email = searchEmailField.getText().trim();
        String telephone = searchTelephoneField.getText().trim();

        try {
            List<Conducteur> allConducteurs = conducteurService.getAllConducteurs();

            allConducteurs.stream()
                    .filter(c -> (nom.isEmpty() || c.getNom().toLowerCase().contains(nom.toLowerCase())) &&
                            (prenom.isEmpty() || c.getPrenom().toLowerCase().contains(prenom.toLowerCase())) &&
                            (email.isEmpty() || c.getEmail().toLowerCase().contains(email.toLowerCase())) &&
                            (telephone.isEmpty() || c.getTelephone().toLowerCase().contains(telephone.toLowerCase())))
                    .sorted((c1, c2) -> c1.getNom().compareToIgnoreCase(c2.getNom()))
                    .forEach(conducteurData::add);

            if (conducteurData.isEmpty()) {
                errorLabel.setText("Aucun Résultat: Aucun conducteur trouvé.");
            } else {
                errorLabel.setText("");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la recherche des conducteurs - " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    private void clearConducteurFields() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        emailField.clear();
        conducteurIdToUpdate = -1;
        errorLabel.setText("");
    }
}


