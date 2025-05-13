package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import model.Trajet;
import model.Voiture;
import service.TrajetService;
import service.VoitureService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ListeTrajetController implements Initializable {

    @FXML
    private TableView<Trajet> tableTrajet;

    @FXML
    private TableColumn<Trajet, Integer> colId;

    @FXML
    private TableColumn<Trajet, String> colVilleDepart;

    @FXML
    private TableColumn<Trajet, String> colVilleArrivee;

    @FXML
    private TableColumn<Trajet, LocalDateTime> colDateHeure;

    @FXML
    private TableColumn<Trajet, Integer> colPlaces;

    @FXML
    private TableColumn<Trajet, Double> colPrix;

    @FXML
    private TableColumn<Trajet, Integer> colConducteur;

    @FXML
    private TableColumn<Trajet, Integer> colVoiture;

    @FXML
    private TableColumn<Trajet, String> colStatut;

    @FXML
    private TableColumn<Trajet, Void> colActions;

    @FXML
    private TextField txtFiltreDepart;

    @FXML
    private TextField txtFiltreArrivee;

    @FXML
    private DatePicker dateFiltre;

    @FXML
    private Slider sliderPrixMax;

    @FXML
    private Label lblPrixMax;

    @FXML
    private ComboBox<Integer> cmbPlacesMin;

    @FXML
    private ComboBox<String> cmbStatutFiltre;

    @FXML
    private TextField txtRecherche;

    @FXML
    private ComboBox<String> cmbTri;

    @FXML
    private Pagination pagination;

    @FXML
    private ComboBox<Integer> cmbElementsParPage;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTotal;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private TrajetService trajetService;
    private VoitureService voitureService;
    private ObservableList<Trajet> trajetsList = FXCollections.observableArrayList();
    private FilteredList<Trajet> filteredList;
    private SortedList<Trajet> sortedList;

    // Pagination
    private int elementsParPage = 10;
    private int pageActuelle = 0;
    private int totalPages = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trajetService = new TrajetService();
        voitureService = new VoitureService();

        // Configuration du DatePicker
        dateFiltre.setConverter(new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty()
                        ? LocalDate.parse(string, dateFormatter)
                        : null;
            }
        });

        // Initialisation des colonnes
        initializeColumns();

        // Chargement des trajets
        chargerTrajets();

        // Configuration des boutons d'action
        configureActionButtons();

        // Initialisation des contrôles de filtrage et pagination
        initializeFilterControls();

        // Configuration de la pagination
        configurePagination();
    }

    /**
     * Initialise les contrôles de filtrage et de tri
     */
    private void initializeFilterControls() {
        // Initialiser le slider de prix max
        sliderPrixMax.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblPrixMax.setText(String.format("%.0f €", newVal.doubleValue()));
        });

        // Initialiser les ComboBox
        if (cmbPlacesMin.getItems().isEmpty()) {
            cmbPlacesMin.getItems().addAll(1, 2, 3, 4, 5);
            cmbPlacesMin.getSelectionModel().selectFirst();
        }

        if (cmbStatutFiltre.getItems().isEmpty()) {
            cmbStatutFiltre.getItems().addAll("Tous", "planifié", "en cours", "terminé", "annulé");
            cmbStatutFiltre.getSelectionModel().select("Tous");
        }

        // Initialiser le ComboBox de tri
        if (cmbTri.getItems().isEmpty()) {
            cmbTri.getItems().addAll(
                "Date (croissant)",
                "Date (décroissant)",
                "Prix (croissant)",
                "Prix (décroissant)",
                "Places disponibles"
            );
            cmbTri.getSelectionModel().select("Date (croissant)");
        }

        // Initialiser le ComboBox d'éléments par page
        if (cmbElementsParPage.getItems().isEmpty()) {
            cmbElementsParPage.getItems().addAll(5, 10, 20, 50, 100);
            cmbElementsParPage.getSelectionModel().select(Integer.valueOf(elementsParPage));
        }

        cmbElementsParPage.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                elementsParPage = newVal;
                updatePagination();
            }
        });

        // Configurer la recherche rapide
        txtRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            handleRechercheRapide();
        });
    }

    private void initializeColumns() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colVilleDepart.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVilleDepart()));
        colVilleArrivee.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVilleArrivee()));
        colDateHeure.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateHeureDepart()));
        colPlaces.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNbPlacesDisponibles()).asObject());
        colPrix.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrixParPlace()).asObject());
        colConducteur.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getConducteurId()).asObject());
        colVoiture.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVoitureId()).asObject());
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut()));

        // Formater la colonne de date
        colDateHeure.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateTimeFormatter.format(item));
                }
            }
        });

        // Configurer la colonne d'actions
        configureActionsColumn();
    }

    private void configureActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAfficher = new Button("Afficher");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(5, btnAfficher, btnModifier, btnSupprimer);

            {
                btnAfficher.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnAfficher.setOnAction(event -> {
                    Trajet trajet = getTableView().getItems().get(getIndex());
                    afficherTrajet(trajet);
                });

                btnModifier.setOnAction(event -> {
                    Trajet trajet = getTableView().getItems().get(getIndex());
                    modifierTrajet(trajet);
                });

                btnSupprimer.setOnAction(event -> {
                    Trajet trajet = getTableView().getItems().get(getIndex());
                    supprimerTrajet(trajet);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void configureActionButtons() {
        // Boutons d'action déjà configurés dans configureActionsColumn()
    }

    private void chargerTrajets() {
        try {
            List<Trajet> trajets = trajetService.getAllTrajets();
            trajetsList.clear();
            trajetsList.addAll(trajets);

            // Créer une liste filtrée
            filteredList = new FilteredList<>(trajetsList, p -> true);

            // Créer une liste triée
            sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableTrajet.comparatorProperty());

            // Appliquer le filtre initial
            appliquerFiltres();

            // Mettre à jour la pagination
            updatePagination();

            lblStatus.setText("Trajets chargés avec succès");
            lblTotal.setText("Total: " + trajetsList.size() + " trajets");
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la récupération des trajets: " + e.getMessage());
        }
    }

    /**
     * Configure la pagination
     */
    private void configurePagination() {
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                pageActuelle = newVal.intValue();
                updateTableView();
            }
        });
    }

    /**
     * Met à jour la pagination en fonction du nombre d'éléments
     */
    private void updatePagination() {
        if (filteredList != null) {
            int totalItems = filteredList.size();
            totalPages = (totalItems + elementsParPage - 1) / elementsParPage; // Arrondi supérieur

            pagination.setPageCount(Math.max(1, totalPages));
            pagination.setCurrentPageIndex(Math.min(pageActuelle, Math.max(0, totalPages - 1)));

            updateTableView();
        }
    }

    /**
     * Met à jour la TableView en fonction de la page actuelle
     */
    private void updateTableView() {
        if (filteredList == null) return;

        int startIndex = pageActuelle * elementsParPage;
        int endIndex = Math.min(startIndex + elementsParPage, filteredList.size());

        // Créer une liste temporaire pour la page actuelle
        ObservableList<Trajet> pageItems = FXCollections.observableArrayList();

        if (startIndex < filteredList.size()) {
            for (int i = startIndex; i < endIndex; i++) {
                pageItems.add(filteredList.get(i));
            }
        }

        tableTrajet.setItems(pageItems);
        lblTotal.setText(String.format("Total: %d trajets (affichage %d-%d)",
                filteredList.size(),
                filteredList.isEmpty() ? 0 : startIndex + 1,
                endIndex));
    }

    /**
     * Applique les filtres à la liste
     */
    private void appliquerFiltres() {
        if (filteredList == null) return;

        Predicate<Trajet> predicate = trajet -> true; // Accepte tout par défaut

        // Filtre par ville de départ
        if (txtFiltreDepart.getText() != null && !txtFiltreDepart.getText().isEmpty()) {
            String villeDepart = txtFiltreDepart.getText().toLowerCase();
            predicate = predicate.and(t -> t.getVilleDepart().toLowerCase().contains(villeDepart));
        }

        // Filtre par ville d'arrivée
        if (txtFiltreArrivee.getText() != null && !txtFiltreArrivee.getText().isEmpty()) {
            String villeArrivee = txtFiltreArrivee.getText().toLowerCase();
            predicate = predicate.and(t -> t.getVilleArrivee().toLowerCase().contains(villeArrivee));
        }

        // Filtre par date
        if (dateFiltre.getValue() != null) {
            LocalDate date = dateFiltre.getValue();
            LocalDateTime dateDebut = date.atStartOfDay();
            LocalDateTime dateFin = date.atTime(LocalTime.MAX);
            predicate = predicate.and(t ->
                !t.getDateHeureDepart().isBefore(dateDebut) &&
                !t.getDateHeureDepart().isAfter(dateFin));
        }

        // Filtre par prix maximum
        if (sliderPrixMax.getValue() < sliderPrixMax.getMax()) {
            double prixMax = sliderPrixMax.getValue();
            predicate = predicate.and(t -> t.getPrixParPlace() <= prixMax);
        }

        // Filtre par places minimum
        if (cmbPlacesMin.getValue() != null && cmbPlacesMin.getValue() > 1) {
            int placesMin = cmbPlacesMin.getValue();
            predicate = predicate.and(t -> t.getNbPlacesDisponibles() >= placesMin);
        }

        // Filtre par statut
        if (cmbStatutFiltre.getValue() != null && !cmbStatutFiltre.getValue().equals("Tous")) {
            String statut = cmbStatutFiltre.getValue();
            predicate = predicate.and(t -> t.getStatut().equals(statut));
        }

        // Filtre par recherche rapide (recherche dans toutes les colonnes textuelles)
        if (txtRecherche.getText() != null && !txtRecherche.getText().isEmpty()) {
            String recherche = txtRecherche.getText().toLowerCase();
            predicate = predicate.and(t ->
                t.getVilleDepart().toLowerCase().contains(recherche) ||
                t.getVilleArrivee().toLowerCase().contains(recherche) ||
                t.getStatut().toLowerCase().contains(recherche) ||
                String.valueOf(t.getPrixParPlace()).contains(recherche) ||
                String.valueOf(t.getNbPlacesDisponibles()).contains(recherche)
            );
        }

        // Appliquer le prédicat
        filteredList.setPredicate(predicate);

        // Appliquer le tri
        appliquerTri();

        // Mettre à jour la pagination
        updatePagination();
    }

    /**
     * Applique le tri sélectionné
     */
    private void appliquerTri() {
        if (sortedList == null || cmbTri.getValue() == null) return;

        String tri = cmbTri.getValue();

        switch (tri) {
            case "Date (croissant)":
                tableTrajet.getSortOrder().clear();
                tableTrajet.getSortOrder().add(colDateHeure);
                colDateHeure.setSortType(TableColumn.SortType.ASCENDING);
                break;

            case "Date (décroissant)":
                tableTrajet.getSortOrder().clear();
                tableTrajet.getSortOrder().add(colDateHeure);
                colDateHeure.setSortType(TableColumn.SortType.DESCENDING);
                break;

            case "Prix (croissant)":
                tableTrajet.getSortOrder().clear();
                tableTrajet.getSortOrder().add(colPrix);
                colPrix.setSortType(TableColumn.SortType.ASCENDING);
                break;

            case "Prix (décroissant)":
                tableTrajet.getSortOrder().clear();
                tableTrajet.getSortOrder().add(colPrix);
                colPrix.setSortType(TableColumn.SortType.DESCENDING);
                break;

            case "Places disponibles":
                tableTrajet.getSortOrder().clear();
                tableTrajet.getSortOrder().add(colPlaces);
                colPlaces.setSortType(TableColumn.SortType.DESCENDING);
                break;
        }

        tableTrajet.sort();
    }

    @FXML
    private void handleRechercher() {
        appliquerFiltres();
    }

    @FXML
    private void handleRechercheRapide() {
        appliquerFiltres();
    }

    @FXML
    private void handleAppliquerTri() {
        appliquerTri();
    }

    @FXML
    private void handleReinitialiser() {
        txtFiltreDepart.clear();
        txtFiltreArrivee.clear();
        dateFiltre.setValue(null);
        txtRecherche.clear();
        sliderPrixMax.setValue(sliderPrixMax.getMax());
        cmbPlacesMin.getSelectionModel().selectFirst();
        cmbStatutFiltre.getSelectionModel().select("Tous");
        cmbTri.getSelectionModel().select("Date (croissant)");

        appliquerFiltres();
    }

    @FXML
    private void handleRafraichir() {
        chargerTrajets();
        lblStatus.setText("Liste des trajets rafraîchie");
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTrajet.fxml"));
            Parent root = loader.load();

            AjouterTrajetController controller = loader.getController();
            controller.setListeTrajetController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture du formulaire d'ajout");
        }
    }

    protected void afficherTrajet(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTrajet.fxml"));
            Parent root = loader.load();

            AfficherTrajetController controller = loader.getController();
            controller.setTrajet(trajet);
            controller.setListeTrajetController(this);

            Stage stage = new Stage();
            stage.setTitle("Détails du trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'affichage des détails");
        }
    }

    private void modifierTrajet(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml"));
            Parent root = loader.load();

            ModifierTrajetController controller = loader.getController();
            controller.setTrajet(trajet);
            controller.setListeTrajetController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier un trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ouverture du formulaire de modification");
        }
    }

    private void supprimerTrajet(Trajet trajet) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le trajet");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                boolean success = trajetService.supprimerTrajet(trajet.getId());
                if (success) {
                    chargerTrajets();
                    lblStatus.setText("Trajet supprimé avec succès");
                } else {
                    afficherErreur("Erreur lors de la suppression du trajet");
                }
            } catch (Exception e) {
                e.printStackTrace();
                afficherErreur("Erreur lors de la suppression du trajet: " + e.getMessage());
            }
        }
    }

    public void refreshTrajets() {
        chargerTrajets();
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleFermer() {
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
    }

    public void afficherTrajet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTrajet.fxml"));
            Parent root = loader.load();

            AfficherTrajetController controller = loader.getController();
            Trajet trajet = tableTrajet.getSelectionModel().getSelectedItem();

            if (trajet == null) {
                afficherErreur("Veuillez sélectionner un trajet à afficher");
                return;
            }

            controller.setTrajet(trajet);
            controller.setListeTrajetController(this);

            Stage stage = new Stage();
            stage.setTitle("Détails du trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'affichage des détails du trajet");
        }
    }
}
