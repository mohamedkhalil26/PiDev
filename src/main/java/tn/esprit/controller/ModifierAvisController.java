package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entites.Avis;
import tn.esprit.entites.AvisSelectionne;
import tn.esprit.services.AvisService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ModifierAvisController {
    @FXML
    private ComboBox<Integer> IDCovoiturageComboBox;

    @FXML
    private ComboBox<Integer> NoteComboBox;

    @FXML
    private TextArea commentArea;

    private final AvisService avisService = new AvisService();
    private Avis selectedAvis;

    public void initialize() {
        selectedAvis = AvisSelectionne.getInstance().getAvisSelectionne();

        if (selectedAvis == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No feedback selected for editing");
            closeWindow();
            return;
        }

        initializeStarRatingComboBox();
        populateCovoiturageComboBox();

        NoteComboBox.setValue(selectedAvis.getNote());
        commentArea.setText(selectedAvis.getCommentaire());
        IDCovoiturageComboBox.setValue(selectedAvis.getId_covoiturage());
    }

    private void initializeStarRatingComboBox() {
        NoteComboBox.getItems().clear();
        for (int i = 1; i <= 5; i++) {
            NoteComboBox.getItems().add(i);
        }

        NoteComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "★".repeat(item));
            }
        });

        NoteComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "★".repeat(item));
            }
        });
    }

    private void populateCovoiturageComboBox() {
        try {
            List<Integer> carpoolingIds = avisService.getAllCarpoolingIds();
            IDCovoiturageComboBox.getItems().setAll(carpoolingIds);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load carpooling IDs: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateFeedback(ActionEvent event) {
        if (selectedAvis == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No feedback selected for update");
            return;
        }

        Integer newNote = NoteComboBox.getValue();
        String newComment = commentArea.getText().trim();
        Integer newCovoiturageId = IDCovoiturageComboBox.getValue();

        if (newNote == null || newComment.isEmpty() || newCovoiturageId == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        selectedAvis.setNote(newNote);
        selectedAvis.setCommentaire(newComment);
        selectedAvis.setId_covoiturage(newCovoiturageId);

        try {
            avisService.modifier(selectedAvis);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback updated successfully.");
            navigateToShowFeedback();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void navigateToShowFeedback(ActionEvent event) {
        navigateToShowFeedback();
    }

    private void navigateToShowFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_avis.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            closeWindow();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the feedback list view.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) commentArea.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
