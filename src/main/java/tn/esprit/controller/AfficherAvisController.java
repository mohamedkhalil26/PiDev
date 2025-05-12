package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entites.Avis;
import tn.esprit.entites.AvisSelectionne;
import tn.esprit.services.AvisService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherAvisController {

    @FXML
    private Button addButton;

    @FXML
    private TableView<Avis> feedbackTable;

    @FXML
    private TableColumn<Avis, Integer> colId;

    @FXML
    private TableColumn<Avis, Integer> colUtilisateur;

    @FXML
    private TableColumn<Avis, Integer> colCovoiturage;

    @FXML
    private TableColumn<Avis, Integer> colNote;

    @FXML
    private TableColumn<Avis, String> colCommentaire;

    @FXML
    private TableColumn<Avis, String> colSentiment;

    @FXML
    private TableColumn<Avis, Void> colDel;

    private final AvisService avisService = new AvisService();

    @FXML
    public void initialize() throws SQLException {
        // Initialize columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id_avis"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
        colCovoiturage.setCellValueFactory(new PropertyValueFactory<>("id_covoiturage"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        colSentiment.setCellValueFactory(new PropertyValueFactory<>("sentiment"));

        // Set up buttons and load data
        setUpButtons();
        loadAvis();

        // Set up row double-click event
        feedbackTable.setRowFactory(tv -> {
            TableRow<Avis> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Avis selectedAvis = row.getItem();
                    AvisSelectionne.getInstance().setSelectedUser(selectedAvis);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier_avis.fxml"));
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("User Details");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Error", "Unable to load user details: " + e.getMessage());
                    }
                }
            });
            return row;
        });
    }

    public void loadAvis() throws SQLException {
        List<Avis> avisList = avisService.returnList();
        feedbackTable.getItems().setAll(avisList);
    }

    @FXML
    void naviguer_a_ajouter_avis(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter_Avis.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load the Add Avis screen.");
        }
    }

    private void setUpButtons() {
        colDel.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(avis);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    private void handleDeleteButtonClick(Avis avis) {
        try {
            avisService.supprimer(avis);
            loadAvis();
            showAlert("Success", "Avis deleted successfully.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while deleting the avis: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}