package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.EmpreinteCarbone;
import model.Trajet;
import model.Voiture;
import service.TrajetService;
import service.VoitureService;
import service.EmpreinteCarboneService;
import service.EmailNotificationService;
import service.UtilisateurService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MainAppController implements Initializable {

    // Main UI components
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab carManagementTab;

    @FXML
    private Tab tripManagementTab;

    // Vehicle management components
    @FXML
    private Label lblTitre;

    @FXML
    private Button btnRetourListe;

    @FXML
    private Button btnAjouter;

    @FXML
    private VBox btnAjouterTrajet;

    @FXML
    private VBox panelListe;

    @FXML
    private TextField txtRecherche;

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
    private Label lblStatus;

    @FXML
    private VBox panelAjouter;

    @FXML
    private ComboBox<Integer> cmbUtilisateur;

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
    private VBox panelModifier;

    @FXML
    private TextField txtIdModif;

    @FXML
    private TextField txtUtilisateurIdModif;

    @FXML
    private TextField txtNbPlacesModif;

    @FXML
    private TextField txtCouleurModif;

    @FXML
    private ComboBox<String> cmbTypeVoitureModif;

    @FXML
    private ComboBox<String> cmbCategorieModif;

    @FXML
    private ComboBox<String> cmbTypeCarburantModif;

    @FXML
    private VBox panelAfficher;

    @FXML
    private Label lblIdAfficher;

    @FXML
    private Label lblUtilisateurIdAfficher;

    @FXML
    private Label lblNbPlacesAfficher;

    @FXML
    private Label lblCouleurAfficher;

    @FXML
    private Label lblTypeVoitureAfficher;

    @FXML
    private Label lblCategorieAfficher;

    @FXML
    private Label lblTypeCarburantAfficher;

    // Trip management components
    @FXML
    private VBox panelListeTrajets;

    @FXML
    private TableView<Trajet> tableTrajets;

    @FXML
    private TableColumn<Trajet, Integer> colTrajetId;

    @FXML
    private TableColumn<Trajet, String> colVilleDepart;

    @FXML
    private TableColumn<Trajet, String> colVilleArrivee;

    @FXML
    private TableColumn<Trajet, LocalDateTime> colDateHeureDepart;

    @FXML
    private TableColumn<Trajet, Integer> colNbPlacesDisponibles;

    @FXML
    private TableColumn<Trajet, Double> colPrixParPlace;

    @FXML
    private TableColumn<Trajet, Void> colTrajetActions;

    @FXML
    private Label lblStatusTrajets;

    @FXML
    private VBox panelAjouterTrajet;

    @FXML
    private TextField txtVilleDepart;

    @FXML
    private TextField txtVilleArrivee;

    @FXML
    private TextField txtDateHeureDepart;

    @FXML
    private TextField txtNbPlacesTrajet;

    @FXML
    private TextField txtPrixParPlace;

    @FXML
    private ComboBox<Integer> cmbVoitureTrajet;

    @FXML
    private ComboBox<Integer> cmbConducteurTrajet;

    // Email components
    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<String> cmbTypeNotification;

    @FXML
    private Button btnEnvoyerEmail;

    @FXML
    private Label lblStatusEmail;

    @FXML
    private ListView<String> listViewEmails;

    @FXML
    private Label lblCarbonFootprint;

    // Services and data
    private TrajetService trajetService;
    private VoitureService voitureService;
    private EmpreinteCarboneService empreinteCarboneService;
    private UtilisateurService utilisateurService;
    private EmailNotificationService emailNotificationService;
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();
    private ObservableList<Trajet> trajetsList = FXCollections.observableArrayList();
    private ObservableList<Integer> utilisateurIds = FXCollections.observableArrayList();
    private ObservableList<Integer> voitureIds = FXCollections.observableArrayList();
    private ObservableList<String> emailsEnvoyes = FXCollections.observableArrayList();
    private Voiture voitureSelectionnee;
    private Trajet trajetSelectionne;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize services
        trajetService = new TrajetService();
        voitureService = new VoitureService();
        empreinteCarboneService = new EmpreinteCarboneService();
        utilisateurService = new UtilisateurService();
        emailNotificationService = new EmailNotificationService();

        // Initialize vehicle management view
        initialiserTableVoitures();
        chargerVoitures();
        chargerUtilisateurs();

        // Initialize trip management view
        initialiserTableTrajets();
        chargerTrajets();
        chargerVoituresPourTrajet();

        // Initialize email list
        listViewEmails.setItems(emailsEnvoyes);

        // Initialize ComboBoxes
        cmbTypeVoiture.getItems().addAll("Berline", "SUV", "Citadine", "Utilitaire");
        cmbTypeVoitureModif.getItems().addAll("Berline", "SUV", "Citadine", "Utilitaire");
        cmbCategorie.getItems().addAll("Economique", "Intermédiaire", "Premium");
        cmbCategorieModif.getItems().addAll("Economique", "Intermédiaire", "Premium");
        cmbTypeCarburant.getItems().addAll("Essence", "Diesel", "Electrique", "Hybride");
        cmbTypeCarburantModif.getItems().addAll("Essence", "Diesel", "Electrique", "Hybride");
        cmbTypeNotification.getItems().addAll(
                "Confirmation de réservation",
                "Rappel avant départ",
                "Notification d'entretien",
                "Information générale"
        );

        // Select default values
        if (!cmbTypeNotification.getItems().isEmpty()) {
            cmbTypeNotification.getSelectionModel().selectFirst();
        }
        txtEmail.setText("takwabouabid149@gmail.com");
    }

    // Main Menu Handlers
    @FXML
    private void handleGestionVoitures() {
        mainTabPane.getSelectionModel().select(carManagementTab);
        afficherPanel(panelListe);
        lblTitre.setText("Gestion des Voitures");
        chargerVoitures();
        trajetSelectionne = null;
        lblCarbonFootprint.setText("");
    }

    @FXML
    private void handleGestionTrajets() {
        mainTabPane.getSelectionModel().select(tripManagementTab);
        afficherPanel(panelListeTrajets);
        lblTitre.setText("Gestion des Trajets");
        chargerTrajets();
        voitureSelectionnee = null;
        lblCarbonFootprint.setText("");
    }

    // Vehicle Management Methods
    private void initialiserTableVoitures() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUtilisateurId.setCellValueFactory(new PropertyValueFactory<>("utilisateurId"));
        colNbPlaces.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        colCouleur.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        colTypeVoiture.setCellValueFactory(new PropertyValueFactory<>("typeVoiture"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colTypeCarburant.setCellValueFactory(new PropertyValueFactory<>("typeCarburant"));
        configureActionsColumn();
    }

    private void configureActionsColumn() {
        Callback<TableColumn<Voiture, Void>, TableCell<Voiture, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnAfficher = new Button("Afficher");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(3);

            {
                btnAfficher.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnModifier.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                pane.setSpacing(5);
                pane.getChildren().addAll(btnAfficher, btnModifier, btnSupprimer);

                btnAfficher.setOnAction(event -> {
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
        };

        colActions.setCellFactory(cellFactory);
    }

    private void chargerVoitures() {
        List<Voiture> voitures = voitureService.getAllVoitures();
        voituresList.clear();
        voituresList.addAll(voitures);
        tableVoitures.setItems(voituresList);
        lblStatus.setText("Nombre de voitures: " + voitures.size());
    }

    private void chargerUtilisateurs() {
        List<Integer> ids = utilisateurService.getAllUtilisateurIds();
        utilisateurIds.clear();
        utilisateurIds.addAll(ids);
        cmbUtilisateur.setItems(utilisateurIds);
        cmbConducteurTrajet.setItems(utilisateurIds);
        if (!utilisateurIds.isEmpty()) {
            cmbUtilisateur.getSelectionModel().selectFirst();
            cmbConducteurTrajet.getSelectionModel().selectFirst();
        }
    }

    private void chargerVoituresPourTrajet() {
        List<Voiture> voitures = voitureService.getAllVoitures();
        voitureIds.clear();
        voitureIds.addAll(voitures.stream().map(Voiture::getId).toList());
        cmbVoitureTrajet.setItems(voitureIds);
        if (!voitureIds.isEmpty()) {
            cmbVoitureTrajet.getSelectionModel().selectFirst();
        }
    }

    private void afficherPanel(VBox panel) {
        panelListe.setVisible(false);
        panelAjouter.setVisible(false);
        panelModifier.setVisible(false);
        panelAfficher.setVisible(false);
        panelListeTrajets.setVisible(false);
        panelAjouterTrajet.setVisible(false);

        panel.setVisible(true);
        btnRetourListe.setVisible(!panel.equals(panelListe) && !panel.equals(panelListeTrajets));
        btnAjouter.setVisible(panel.equals(panelListe));
        btnAjouterTrajet.setVisible(panel.equals(panelListeTrajets));
    }

    @FXML
    private void handleRetourListe() {
        if (mainTabPane.getSelectionModel().getSelectedItem().equals(carManagementTab)) {
            afficherPanel(panelListe);
            lblTitre.setText("Gestion des Voitures");
            chargerVoitures();
        } else {
            afficherPanel(panelListeTrajets);
            lblTitre.setText("Gestion des Trajets");
            chargerTrajets();
        }
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
    private void handleReinitialiserRecherche() {
        txtRecherche.clear();
        chargerVoitures();
    }

    @FXML
    private void handleAjouterVoiture() {
        afficherPanel(panelAjouter);
        lblTitre.setText("Ajouter une Voiture");
        txtNbPlaces.clear();
        txtCouleur.clear();
        if (!cmbUtilisateur.getItems().isEmpty()) {
            cmbUtilisateur.getSelectionModel().selectFirst();
        }
        if (!cmbTypeVoiture.getItems().isEmpty()) {
            cmbTypeVoiture.getSelectionModel().selectFirst();
        }
        if (!cmbCategorie.getItems().isEmpty()) {
            cmbCategorie.getSelectionModel().selectFirst();
        }
        if (!cmbTypeCarburant.getItems().isEmpty()) {
            cmbTypeCarburant.getSelectionModel().selectFirst();
        }
    }

    private void afficherVoiture(Voiture voiture) {
        voitureSelectionnee = voiture;
        trajetSelectionne = null;
        afficherPanel(panelAfficher);
        lblTitre.setText("Détails de la Voiture");
        lblIdAfficher.setText(String.valueOf(voiture.getId()));
        lblUtilisateurIdAfficher.setText(String.valueOf(voiture.getUtilisateurId()));
        lblNbPlacesAfficher.setText(String.valueOf(voiture.getNbPlaces()));
        lblCouleurAfficher.setText(voiture.getCouleur());
        lblTypeVoitureAfficher.setText(voiture.getTypeVoiture());
        lblCategorieAfficher.setText(voiture.getCategorie());
        lblTypeCarburantAfficher.setText(voiture.getTypeCarburant());
        lblCarbonFootprint.setText("");
    }

    private void modifierVoiture(Voiture voiture) {
        voitureSelectionnee = voiture;
        afficherPanel(panelModifier);
        lblTitre.setText("Modifier une Voiture");
        txtIdModif.setText(String.valueOf(voiture.getId()));
        txtUtilisateurIdModif.setText(String.valueOf(voiture.getUtilisateurId()));
        txtNbPlacesModif.setText(String.valueOf(voiture.getNbPlaces()));
        txtCouleurModif.setText(voiture.getCouleur());
        cmbTypeVoitureModif.setValue(voiture.getTypeVoiture());
        cmbCategorieModif.setValue(voiture.getCategorie());
        cmbTypeCarburantModif.setValue(voiture.getTypeCarburant());
    }

    private void supprimerVoiture(Voiture voiture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = voitureService.supprimerVoiture(voiture.getId());
            if (success) {
                afficherConfirmation("Voiture supprimée avec succès");
                chargerVoitures();
            } else {
                afficherErreur("Erreur lors de la suppression de la voiture");
            }
        }
    }

    @FXML
    private void handleAnnulerAjout() {
        handleRetourListe();
    }

    @FXML
    private void handleEnregistrerAjout() {
        if (validerFormulaireAjout()) {
            try {
                Integer utilisateurId = cmbUtilisateur.getValue();
                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                String couleur = txtCouleur.getText().trim();
                String typeVoiture = cmbTypeVoiture.getValue();
                String categorie = cmbCategorie.getValue();
                String typeCarburant = cmbTypeCarburant.getValue();

                Voiture nouvelleVoiture = new Voiture(0, utilisateurId, nbPlaces, couleur, typeVoiture, categorie, typeCarburant);
                boolean success = voitureService.ajouterVoiture(nouvelleVoiture);

                if (success) {
                    afficherConfirmation("Voiture ajoutée avec succès");
                    handleRetourListe();
                } else {
                    afficherErreur("Erreur lors de l'ajout de la voiture");
                }
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer des valeurs numériques valides pour le nombre de places");
            }
        }
    }

    private boolean validerFormulaireAjout() {
        StringBuilder erreurs = new StringBuilder();
        if (cmbUtilisateur.getValue() == null) {
            erreurs.append("- Veuillez sélectionner un utilisateur\n");
        }
        if (txtNbPlaces.getText().trim().isEmpty()) {
            erreurs.append("- Le nombre de places est requis\n");
        } else {
            try {
                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                if (nbPlaces <= 0) {
                    erreurs.append("- Le nombre de places doit être positif\n");
                }
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
            afficherErreur("Veuillez corriger les erreurs suivantes:\n" + erreurs);
            return false;
        }
        return true;
    }

    @FXML
    private void handleAnnulerModif() {
        if (voitureSelectionnee != null) {
            afficherVoiture(voitureSelectionnee);
        } else {
            handleRetourListe();
        }
    }

    @FXML
    private void handleEnregistrerModif() {
        if (validerFormulaireModif()) {
            try {
                int id = Integer.parseInt(txtIdModif.getText().trim());
                int utilisateurId = Integer.parseInt(txtUtilisateurIdModif.getText().trim());
                if (!utilisateurService.utilisateurExists(utilisateurId)) {
                    afficherErreur("L'utilisateur avec l'ID " + utilisateurId + " n'existe pas");
                    return;
                }
                int nbPlaces = Integer.parseInt(txtNbPlacesModif.getText().trim());
                String couleur = txtCouleurModif.getText().trim();
                String typeVoiture = cmbTypeVoitureModif.getValue();
                String categorie = cmbCategorieModif.getValue();
                String typeCarburant = cmbTypeCarburantModif.getValue();

                voitureSelectionnee.setUtilisateurId(utilisateurId);
                voitureSelectionnee.setNbPlaces(nbPlaces);
                voitureSelectionnee.setCouleur(couleur);
                voitureSelectionnee.setTypeVoiture(typeVoiture);
                voitureSelectionnee.setCategorie(categorie);
                voitureSelectionnee.setTypeCarburant(typeCarburant);

                boolean success = voitureService.modifierVoiture(voitureSelectionnee);
                if (success) {
                    afficherConfirmation("Voiture modifiée avec succès");
                    afficherVoiture(voitureSelectionnee);
                } else {
                    afficherErreur("Erreur lors de la modification de la voiture");
                }
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer des valeurs numériques valides");
            }
        }
    }

    private boolean validerFormulaireModif() {
        StringBuilder erreurs = new StringBuilder();
        if (txtUtilisateurIdModif.getText().trim().isEmpty()) {
            erreurs.append("- L'ID utilisateur est requis\n");
        } else {
            try {
                Integer.parseInt(txtUtilisateurIdModif.getText().trim());
            } catch (NumberFormatException e) {
                erreurs.append("- L'ID utilisateur doit être un nombre\n");
            }
        }
        if (txtNbPlacesModif.getText().trim().isEmpty()) {
            erreurs.append("- Le nombre de places est requis\n");
        } else {
            try {
                int nbPlaces = Integer.parseInt(txtNbPlacesModif.getText().trim());
                if (nbPlaces <= 0) {
                    erreurs.append("- Le nombre de places doit être positif\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le nombre de places doit être un nombre\n");
            }
        }
        if (txtCouleurModif.getText().trim().isEmpty()) {
            erreurs.append("- La couleur est requise\n");
        }
        if (cmbTypeVoitureModif.getValue() == null) {
            erreurs.append("- Le type de voiture est requis\n");
        }
        if (cmbCategorieModif.getValue() == null) {
            erreurs.append("- La catégorie est requise\n");
        }
        if (cmbTypeCarburantModif.getValue() == null) {
            erreurs.append("- Le type de carburant est requis\n");
        }
        if (erreurs.length() > 0) {
            afficherErreur("Veuillez corriger les erreurs suivantes:\n" + erreurs);
            return false;
        }
        return true;
    }

    @FXML
    private void handleModifierDepuisAfficher() {
        if (voitureSelectionnee != null) {
            modifierVoiture(voitureSelectionnee);
        }
    }

    @FXML
    private void handleSupprimerDepuisAfficher() {
        if (voitureSelectionnee != null) {
            supprimerVoiture(voitureSelectionnee);
            handleRetourListe();
        }
    }

    // Trip Management Methods
    private void initialiserTableTrajets() {
        colTrajetId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVilleDepart.setCellValueFactory(new PropertyValueFactory<>("villeDepart"));
        colVilleArrivee.setCellValueFactory(new PropertyValueFactory<>("villeArrivee"));
        colDateHeureDepart.setCellValueFactory(new PropertyValueFactory<>("dateHeureDepart"));
        colNbPlacesDisponibles.setCellValueFactory(new PropertyValueFactory<>("nbPlacesDisponibles"));
        colPrixParPlace.setCellValueFactory(new PropertyValueFactory<>("prixParPlace"));
        configureTrajetActionsColumn();
    }

    private void configureTrajetActionsColumn() {
        Callback<TableColumn<Trajet, Void>, TableCell<Trajet, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnReserver = new Button("Réserver");
            private final Button btnAfficher = new Button("Afficher");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(3);

            {
                btnReserver.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnAfficher.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                pane.setSpacing(5);
                pane.getChildren().addAll(btnReserver, btnAfficher);

                btnReserver.setOnAction(event -> {
                    Trajet trajet = getTableView().getItems().get(getIndex());
                    reserverTrajet(trajet);
                });

                btnAfficher.setOnAction(event -> {
                    Trajet trajet = getTableView().getItems().get(getIndex());
                    afficherTrajet(trajet);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };

        colTrajetActions.setCellFactory(cellFactory);
    }

    private void chargerTrajets() {
        List<Trajet> trajets = trajetService.getAllTrajets();
        trajetsList.clear();
        trajetsList.addAll(trajets);
        tableTrajets.setItems(trajetsList);
        lblStatusTrajets.setText("Nombre de trajets: " + trajets.size());
    }

    private void reserverTrajet(Trajet trajet) {
        trajetSelectionne = trajet;
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Réservation de trajet");
        dialog.setHeaderText("Réserver des places pour le trajet de " + trajet.getVilleDepart() + " à " + trajet.getVilleArrivee());
        dialog.setContentText("Nombre de places à réserver:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombrePlaces -> {
            try {
                int places = Integer.parseInt(nombrePlaces);
                if (places <= 0) {
                    afficherErreur("Le nombre de places doit être positif");
                    return;
                }
                String email = txtEmail.getText().trim();
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    afficherErreur("Veuillez entrer une adresse email valide");
                    return;
                }
                boolean success = trajetService.reserverPlaces(trajet.getId(), email, places);
                if (success) {
                    double distance = empreinteCarboneService.calculerDistance(trajet.getVilleDepart(), trajet.getVilleArrivee());
                    EmpreinteCarbone empreinte = empreinteCarboneService.calculerEmpreinteCarbone(trajet, distance);
                    emailNotificationService.envoyerConfirmationReservation(trajet, email, places, empreinte, false);
                    afficherConfirmation("Réservation effectuée avec succès");
                    chargerTrajets();
                    afficherTrajet(trajet);
                } else {
                    afficherErreur("Erreur lors de la réservation");
                }
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre valide");
            }
        });
    }

    private void afficherTrajet(Trajet trajet) {
        trajetSelectionne = trajet;
        voitureSelectionnee = null;
        afficherPanel(panelAfficher);
        lblTitre.setText("Détails du Trajet");
        lblIdAfficher.setText(String.valueOf(trajet.getId()));
        lblUtilisateurIdAfficher.setText(String.valueOf(trajet.getConducteurId()));
        lblNbPlacesAfficher.setText(String.valueOf(trajet.getNbPlacesDisponibles()));
        lblCouleurAfficher.setText("-");
        lblTypeVoitureAfficher.setText("-");
        lblCategorieAfficher.setText(trajet.getVilleDepart() + " -> " + trajet.getVilleArrivee());
        lblTypeCarburantAfficher.setText(trajet.getDateHeureDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        double distance = empreinteCarboneService.calculerDistance(trajet.getVilleDepart(), trajet.getVilleArrivee());
        EmpreinteCarbone empreinte = empreinteCarboneService.calculerEmpreinteCarbone(trajet, distance);
        lblCarbonFootprint.setText(String.format("Empreinte carbone: %.2f kg CO2 (%.1f%% d'économies)",
                empreinte.getEmissionsTotalesKgCO2(), empreinte.getEconomiesPourcentage()));
    }

    @FXML
    private void handleAjouterTrajet() {
        afficherPanel(panelAjouterTrajet);
        lblTitre.setText("Ajouter un Trajet");
        txtVilleDepart.clear();
        txtVilleArrivee.clear();
        txtDateHeureDepart.clear();
        txtNbPlacesTrajet.clear();
        txtPrixParPlace.clear();
        if (!cmbVoitureTrajet.getItems().isEmpty()) {
            cmbVoitureTrajet.getSelectionModel().selectFirst();
        }
        if (!cmbConducteurTrajet.getItems().isEmpty()) {
            cmbConducteurTrajet.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleEnregistrerTrajet() {
        if (validerFormulaireTrajet()) {
            try {
                String villeDepart = txtVilleDepart.getText().trim();
                String villeArrivee = txtVilleArrivee.getText().trim();
                LocalDateTime dateHeureDepart = LocalDateTime.parse(txtDateHeureDepart.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                int nbPlaces = Integer.parseInt(txtNbPlacesTrajet.getText().trim());
                double prixParPlace = Double.parseDouble(txtPrixParPlace.getText().trim());
                Integer voitureId = cmbVoitureTrajet.getValue();
                Integer conducteurId = cmbConducteurTrajet.getValue();

                Trajet nouveauTrajet = new Trajet(0, villeDepart, villeArrivee, dateHeureDepart, nbPlaces, prixParPlace, voitureId, conducteurId);
                boolean success = trajetService.ajouterTrajet(nouveauTrajet);

                if (success) {
                    double distance = empreinteCarboneService.calculerDistance(villeDepart, villeArrivee);
                    EmpreinteCarbone empreinte = empreinteCarboneService.calculerEmpreinteCarbone(nouveauTrajet, distance);
                    emailNotificationService.envoyerConfirmationCreationTrajet(nouveauTrajet, txtEmail.getText().trim(), empreinte, false);
                    afficherConfirmation("Trajet ajouté avec succès");
                    handleRetourListe();
                } else {
                    afficherErreur("Erreur lors de l'ajout du trajet");
                }
            } catch (Exception e) {
                afficherErreur("Erreur de format: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAnnulerAjoutTrajet() {
        handleRetourListe();
    }

    private boolean validerFormulaireTrajet() {
        StringBuilder erreurs = new StringBuilder();
        if (txtVilleDepart.getText().trim().isEmpty()) {
            erreurs.append("- La ville de départ est requise\n");
        }
        if (txtVilleArrivee.getText().trim().isEmpty()) {
            erreurs.append("- La ville d'arrivée est requise\n");
        }
        if (txtDateHeureDepart.getText().trim().isEmpty()) {
            erreurs.append("- La date et heure de départ sont requises\n");
        } else {
            try {
                LocalDateTime.parse(txtDateHeureDepart.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (Exception e) {
                erreurs.append("- Format de date invalide (utilisez JJ/MM/AAAA HH:MM)\n");
            }
        }
        if (txtNbPlacesTrajet.getText().trim().isEmpty()) {
            erreurs.append("- Le nombre de places est requis\n");
        } else {
            try {
                int nbPlaces = Integer.parseInt(txtNbPlacesTrajet.getText().trim());
                if (nbPlaces <= 0) {
                    erreurs.append("- Le nombre de places doit être positif\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le nombre de places doit être un nombre\n");
            }
        }
        if (txtPrixParPlace.getText().trim().isEmpty()) {
            erreurs.append("- Le prix par place est requis\n");
        } else {
            try {
                double prix = Double.parseDouble(txtPrixParPlace.getText().trim());
                if (prix < 0) {
                    erreurs.append("- Le prix par place ne peut pas être négatif\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le prix par place doit être un nombre\n");
            }
        }
        if (cmbVoitureTrajet.getValue() == null) {
            erreurs.append("- Veuillez sélectionner une voiture\n");
        }
        if (cmbConducteurTrajet.getValue() == null) {
            erreurs.append("- Veuillez sélectionner un conducteur\n");
        }
        if (erreurs.length() > 0) {
            afficherErreur("Veuillez corriger les erreurs suivantes:\n" + erreurs);
            return false;
        }
        return true;
    }

    // Email Handling
    @FXML
    private void handleEnvoyerEmail() {
        String email = txtEmail.getText().trim();
        String typeNotification = cmbTypeNotification.getValue();

        if (email.isEmpty()) {
            updateStatusEmail("Veuillez entrer une adresse email", Color.RED);
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            updateStatusEmail("Veuillez entrer une adresse email valide", Color.RED);
            return;
        }
        if (typeNotification == null) {
            updateStatusEmail("Veuillez sélectionner un type de notification", Color.RED);
            return;
        }
        if (voitureSelectionnee == null && trajetSelectionne == null) {
            updateStatusEmail("Veuillez sélectionner une voiture ou un trajet", Color.RED);
            return;
        }

        btnEnvoyerEmail.setDisable(true);
        updateStatusEmail("Envoi en cours...", Color.ORANGE);

        boolean success;
        String sujet = "";
        String contenu = "";
        EmpreinteCarbone empreinte = null;

        if (trajetSelectionne != null) {
            double distance = empreinteCarboneService.calculerDistance(trajetSelectionne.getVilleDepart(), trajetSelectionne.getVilleArrivee());
            empreinte = empreinteCarboneService.calculerEmpreinteCarbone(trajetSelectionne, distance);
        }

        switch (typeNotification) {
            case "Confirmation de réservation":
                if (trajetSelectionne != null) {
                    success = emailNotificationService.envoyerConfirmationReservation(trajetSelectionne, email, 1, empreinte, false);
                } else {
                    sujet = "Confirmation de réservation de voiture";
                    contenu = genererContenuReservationVoiture();
                    success = emailNotificationService.sendEmail(email, sujet, contenu, false);
                }
                break;
            case "Rappel avant départ":
                if (trajetSelectionne != null) {
                    success = emailNotificationService.envoyerRappelAvantDepart(trajetSelectionne, email, false, empreinte, false);
                } else {
                    sujet = "Rappel : Entretien du véhicule";
                    contenu = genererContenuRappelVoiture();
                    success = emailNotificationService.sendEmail(email, sujet, contenu, false);
                }
                break;
            case "Notification d'entretien":
                sujet = "Rappel d'entretien pour votre véhicule";
                contenu = genererContenuEntretien();
                success = emailNotificationService.sendEmail(email, sujet, contenu, false);
                break;
            default:
                sujet = "Notification concernant votre véhicule";
                contenu = genererContenuGeneral();
                success = emailNotificationService.sendEmail(email, sujet, contenu, false);
        }

        if (success) {
            updateStatusEmail("Email envoyé avec succès !", Color.GREEN);
            ajouterEmailHistorique(typeNotification, email);
        } else {
            updateStatusEmail("Échec de l'envoi de l'email", Color.RED);
        }
        btnEnvoyerEmail.setDisable(false);
    }

    private void updateStatusEmail(String message, Color color) {
        lblStatusEmail.setText(message);
        lblStatusEmail.setTextFill(color);
    }

    private void ajouterEmailHistorique(String type, String destinataire) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = timestamp + " - " + type + " envoyé à " + destinataire;
        emailsEnvoyes.add(0, entry);
    }

    private String genererContenuReservationVoiture() {
        return "<h2>Confirmation de réservation</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous confirmons la réservation de votre véhicule :</p>" +
                "<ul>" +
                "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
                "<li><b>Catégorie :</b> " + voitureSelectionnee.getCategorie() + "</li>" +
                "<li><b>Couleur :</b> " + voitureSelectionnee.getCouleur() + "</li>" +
                "<li><b>Nombre de places :</b> " + voitureSelectionnee.getNbPlaces() + "</li>" +
                "<li><b>Type de carburant :</b> " + voitureSelectionnee.getTypeCarburant() + "</li>" +
                "</ul>" +
                "<p>Merci de votre confiance.</p>";
    }

    private String genererContenuRappelVoiture() {
        return "<h2>Rappel d'entretien</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous vous rappelons qu'il est temps de vérifier votre véhicule.</p>" +
                "<p>Détails du véhicule :</p>" +
                "<ul>" +
                "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
                "<li><b>Catégorie :</b> " + voitureSelectionnee.getCategorie() + "</li>" +
                "<li><b>Couleur :</b> " + voitureSelectionnee.getCouleur() + "</li>" +
                "</ul>" +
                "<p>Nous vous souhaitons une bonne route.</p>";
    }

    private String genererContenuEntretien() {
        return "<h2>Rappel d'entretien</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous vous rappelons qu'il est temps de faire l'entretien de votre véhicule :</p>" +
                "<ul>" +
                "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
                "<li><b>Type de carburant :</b> " + voitureSelectionnee.getTypeCarburant() + "</li>" +
                "</ul>" +
                "<p>Un entretien régulier permet de maintenir votre véhicule en bon état.</p>";
    }

    private String genererContenuGeneral() {
        return "<h2>Information sur votre véhicule</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Voici les informations concernant votre véhicule :</p>" +
                "<ul>" +
                "<li><b>ID :</b> " + voitureSelectionnee.getId() + "</li>" +
                "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
                "<li><b>Catégorie :</b> " + voitureSelectionnee.getCategorie() + "</li>" +
                "<li><b>Couleur :</b> " + voitureSelectionnee.getCouleur() + "</li>" +
                "<li><b>Nombre de places :</b> " + voitureSelectionnee.getNbPlaces() + "</li>" +
                "<li><b>Type de carburant :</b> " + voitureSelectionnee.getTypeCarburant() + "</li>" +
                "</ul>" +
                "<p>N'hésitez pas à nous contacter pour toute information complémentaire.</p>";
    }

    private void afficherConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}