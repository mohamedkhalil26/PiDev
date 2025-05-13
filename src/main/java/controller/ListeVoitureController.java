package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Voiture;
import service.VoitureService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeVoitureController implements Initializable {

    @FXML
    private TableView<Voiture> tableVoitures;

    @FXML
    private TableColumn<Voiture, Integer> colId;

    @FXML
    private TableColumn<Voiture, Integer> colUtilisateurId;

    @FXML
    private TableColumn<Voiture, Integer> colNbPlaces;

    @FXML
    private TableColumn<Voiture, String> colCouleur;

    @FXML
    private TableColumn<Voiture, String> colTypeVoiture;

    @FXML
    private TableColumn<Voiture, String> colCategorie;

    @FXML
    private TableColumn<Voiture, String> colTypeCarburant;

    @FXML
    private TableColumn<Voiture, Void> colActions;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnAjouter;

    private VoitureService voitureService;
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        voitureService = new VoitureService();

        // Initialiser les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUtilisateurId.setCellValueFactory(new PropertyValueFactory<>("utilisateurId"));
        colNbPlaces.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        colCouleur.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        colTypeVoiture.setCellValueFactory(new PropertyValueFactory<>("typeVoiture"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colTypeCarburant.setCellValueFactory(new PropertyValueFactory<>("typeCarburant"));

        // Configurer la colonne d'actions
        configureActionsColumn();

        // Charger les données
        chargerVoitures();
    }

    private void configureActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(5, btnVoir, btnModifier, btnSupprimer);

            {
                btnVoir.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnModifier.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnVoir.setOnAction(event -> {
                    Voiture voiture = getTableView().getItems().get(getIndex());
                    afficherVoiture(voiture);
                });

                btnModifier.setOnAction(event -> {
                    Voiture voiture = getTableView().getItems().get(getIndex());
                    modifierVoiture(voiture);
                });

                btnSupprimer.setOnAction(event -> {
                    Voiture voiture = getTableView().getItems().get(getIndex());
                    supprimerVoiture(voiture);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void chargerVoitures() {
        List<Voiture> voitures = voitureService.getAllVoitures();
        voituresList.clear();
        voituresList.addAll(voitures);
        tableVoitures.setItems(voituresList);
        lblStatus.setText("Nombre de voitures: " + voitures.size());
    }

    @FXML
    private void handleRecherche() {
        String critere = txtRecherche.getText();
        List<Voiture> resultatRecherche = voitureService.rechercherVoitures(critere);
        voituresList.clear();
        voituresList.addAll(resultatRecherche);
        lblStatus.setText("Résultats trouvés: " + resultatRecherche.size());
    }



    @FXML
    private void handleAjouterVoiture() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterVoiture.fxml"));
            Parent root = loader.load();

            AjouterVoitureController controller = loader.getController();
            controller.setListeVoitureController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter une voiture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture du formulaire d'ajout");
        }
    }

    private void afficherVoiture(Voiture voiture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherVoiture.fxml"));
            Parent root = loader.load();

            AfficherVoitureController controller = loader.getController();
            controller.setVoiture(voiture);
            controller.setListeVoitureController(this);

            Stage stage = new Stage();
            stage.setTitle("Détails de la voiture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'affichage des détails");
        }
    }

    private void modifierVoiture(Voiture voiture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierVoiture.fxml"));
            Parent root = loader.load();

            ModifierVoitureController controller = loader.getController();
            controller.setVoiture(voiture);
            controller.setListeVoitureController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier une voiture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture du formulaire de modification");
        }
    }

    private void supprimerVoiture(Voiture voiture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = voitureService.supprimerVoiture(voiture.getId());
                if (success) {
                    chargerVoitures();
                    lblStatus.setText("Voiture supprimée avec succès");
                } else {
                    afficherErreur("Erreur lors de la suppression de la voiture");
                }
            }
        });
    }

    public void rafraichirTableau() {
        chargerVoitures();
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Rafraîchit la liste des voitures
     */
    public void refreshVoitures() {
        chargerVoitures();
    }

    /**
     * Ajoute un bouton de rafraîchissement à l'interface
     */
    @FXML
    private void handleRafraichir() {
        refreshVoitures();
        System.out.println("Liste des voitures rafraîchie");
    }
}
