package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entites.Utilisateur;
import tn.esprit.entites.UtilisateurSelectionne;
import tn.esprit.services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUtilisateursBack {

    private UtilisateurService userService;

    @FXML
    private Button AjouterAdmin;

    @FXML
    private TableView<Utilisateur> UsersTable;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, Integer> colAge;

    @FXML
    private TableColumn<Utilisateur, String> colRole;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, String> colNum_Tel;

    @FXML
    private TableColumn<Utilisateur, String> colUsername;

    @FXML
    private TableColumn<Utilisateur, String> colMotDePasse;

    @FXML
    private TableColumn<Utilisateur, Void> colDelete;

    public AfficherUtilisateursBack() {
        userService = new UtilisateurService();
    }

    @FXML
    public void initialize() throws SQLException {
        // Set up table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id_utilisateur"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNum_Tel.setCellValueFactory(new PropertyValueFactory<>("numero_tel"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colMotDePasse.setCellValueFactory(new PropertyValueFactory<>("mot_de_passe"));

        loadUsers();
        setUpDeleteButton();

        // Row factory with coloring and double-click
        UsersTable.setRowFactory(tv -> {
            TableRow<Utilisateur> row = new TableRow<>() {
                @Override
                protected void updateItem(Utilisateur user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty || user == null) {
                        setStyle("");
                        getStyleClass().clear();
                    } else {
                        getStyleClass().clear();
                        switch (user.getRole()) {
                            case admin:
                                getStyleClass().add("row-admin");
                                break;
                            case chauffeur:
                                getStyleClass().add("row-chauffeur");
                                break;
                            case passager:
                                getStyleClass().add("row-passager");
                                break;
                        }
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    UtilisateurSelectionne.getInstance().setSelectedUser(row.getItem());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDetails.fxml"));
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("User Details");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);

                        ((Stage) UsersTable.getScene().getWindow()).close();
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });
    }

    public void loadUsers() throws SQLException {
        List<Utilisateur> users = userService.returnList();
        UsersTable.getItems().setAll(users);
    }

    private void setUpDeleteButton() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void handleDeleteButtonClick(Utilisateur user) {
        System.out.println("Delete user: " + user);
        try {
            userService.supprimer(user);
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the user: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void naviguer_a_ajouter_admin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AjouterAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the Add Admin screen.");
        }
    }
}
