package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import tn.esprit.entites.Avis;
import tn.esprit.outils.MyDatabase;
import tn.esprit.services.AvisService;
import tn.esprit.api.SentimentAnalyzer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AjouterAvisBackController {


        @FXML
        private ComboBox<Integer> IDCovoiturageComboBox;

        @FXML
        private ComboBox<Integer> IDUtilisateurComboBox;

        @FXML
        private ComboBox<Integer> NoteComboBox;

        @FXML
        private TextArea commentArea;

        @FXML
        private Button displayAllButton;

        @FXML
        private Label sentimentLabel;

        @FXML
        private HBox starRatingBox;

        @FXML
        private Button submitButton;

        private final AvisService avisService = new AvisService();

        public void initialize() {
            // Populate NoteComboBox with star values (1 to 5)
            for (int i = 1; i <= 5; i++) {
                NoteComboBox.getItems().add(i);
            }

            // Custom cell factory to show stars
            NoteComboBox.setCellFactory(comboBox -> new ListCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty || item == null) ? null : "★".repeat(item));
                }
            });
            NoteComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty || item == null) ? null : "★".repeat(item));
                }
            });
            commentArea.textProperty().addListener((obs, oldVal, newVal) -> {
                String sentiment = SentimentAnalyzer.analyze(newVal);
                updateSentimentDisplay(sentiment);
            });

            // Populate IDUtilisateurComboBox and IDCovoiturageComboBox from DB
            populateCovoiturageComboBox();
            populateUtilisateurComboBox();

        }

        private void populateUtilisateurComboBox() {
            try {
                List<Integer> userIds = avisService.getAllUserIds();
                IDUtilisateurComboBox.getItems().setAll(userIds);
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to load user IDs: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void populateCovoiturageComboBox() {
            try {
                List<Integer> carpoolingIds = avisService.getAllCarpoolingIds();
                IDCovoiturageComboBox.getItems().setAll(carpoolingIds);
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to load carpooling IDs: " + e.getMessage());
                e.printStackTrace();
            }
        }
        private void updateSentimentDisplay(String sentiment) {
            sentimentLabel.setText("Sentiment: " + sentiment.toUpperCase());
            sentimentLabel.getStyleClass().removeAll("positive", "negative", "neutral");
            sentimentLabel.getStyleClass().add(sentiment.toLowerCase());
        }


        @FXML
        void Submit(ActionEvent event) {
            try {
                String comment = commentArea.getText().trim();
                Integer rate = NoteComboBox.getValue();
                Integer idCov = IDCovoiturageComboBox.getValue();
                Integer idUser = IDUtilisateurComboBox.getValue();
                String sentiment =sentimentLabel.getText().trim();


                // Validate input
                if (comment.isEmpty() || rate == null || idCov == null || idUser == null) {
                    showAlert("Error", "All fields must be filled out!");
                    return;
                }

                // Create and save Avis
                Avis avis = new Avis();
                avis.setCommentaire(comment);
                avis.setNote(rate);
                avis.setId_covoiturage(idCov);
                avis.setId_utilisateur(idUser);
                avis.setSentiment(sentiment);

                avisService.ajouterP(avis);

                showAlert("Success", "Avis added successfully!");
                clearForm();

            } catch (SQLException e) {
                showAlert("Database Error", "Failed to save Avis: " + e.getMessage());
            } catch (Exception e) {
                showAlert("Error", "An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
        @FXML
        void naviguerVersAfficherAvis(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) displayAllButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Unable to load the Afficher Avis screen.");
            }
        }


        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }

        private void clearForm() {
            NoteComboBox.setValue(null);
            IDCovoiturageComboBox.setValue(null);
            IDUtilisateurComboBox.setValue(null);
            commentArea.clear();
        }
    }

