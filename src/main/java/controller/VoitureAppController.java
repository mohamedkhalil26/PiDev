package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Voiture;
import service.UtilisateurService;
import service.VoitureService;
import utils.EmailServiceSimulator;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VoitureAppController implements Initializable {

    // Composants communs
    @FXML
    private Label lblTitre;
    
    @FXML
    private Button btnRetourListe;
    
    @FXML
    private Button btnAjouter;

    // Composants de la vue Liste
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

    // Composants de la vue Ajouter
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

    // Composants de la vue Modifier
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

    // Composants de la vue Afficher
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

    // Services et données
    private VoitureService voitureService;
    private UtilisateurService utilisateurService;
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();
    private ObservableList<Integer> utilisateurIds = FXCollections.observableArrayList();
    private ObservableList<String> emailsEnvoyes = FXCollections.observableArrayList();
    private Voiture voitureSelectionnee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les services
        voitureService = new VoitureService();
        utilisateurService = new UtilisateurService();

        // Initialiser la vue Liste
        initialiserTableVoitures();
        chargerVoitures();

        // Initialiser les utilisateurs pour la vue Ajouter
        chargerUtilisateurs();
        
        // Initialiser la liste des emails
        listViewEmails.setItems(emailsEnvoyes);
        
        // Sélectionner le premier type de notification par défaut
        if (cmbTypeNotification != null && cmbTypeNotification.getItems().size() > 0) {
            cmbTypeNotification.getSelectionModel().selectFirst();
        }
        
        // Initialiser l'email par défaut
        if (txtEmail != null) {
            txtEmail.setText("takwabouabid149@gmail.com");
        }
    }

    // Méthodes d'initialisation
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
        Callback<TableColumn<Voiture, Void>, TableCell<Voiture, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Voiture, Void> call(final TableColumn<Voiture, Void> param) {
                return new TableCell<>() {
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
        
        if (!utilisateurIds.isEmpty()) {
            cmbUtilisateur.getSelectionModel().selectFirst();
        }
    }

    // Méthodes de navigation
    private void afficherPanel(VBox panel) {
        panelListe.setVisible(false);
        panelAjouter.setVisible(false);
        panelModifier.setVisible(false);
        panelAfficher.setVisible(false);
        
        panel.setVisible(true);
        
        // Mettre à jour les boutons de navigation
        btnRetourListe.setVisible(!panel.equals(panelListe));
        btnAjouter.setVisible(panel.equals(panelListe));
    }

    @FXML
    private void handleRetourListe() {
        afficherPanel(panelListe);
        lblTitre.setText("Gestion des Voitures");
        chargerVoitures();
    }

    // Méthodes de la vue Liste
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
        
        // Réinitialiser les champs
        txtNbPlaces.clear();
        txtCouleur.clear();
        if (cmbUtilisateur.getItems().size() > 0) {
            cmbUtilisateur.getSelectionModel().selectFirst();
        }
        if (cmbTypeVoiture.getItems().size() > 0) {
            cmbTypeVoiture.getSelectionModel().selectFirst();
        }
        if (cmbCategorie.getItems().size() > 0) {
            cmbCategorie.getSelectionModel().selectFirst();
        }
        if (cmbTypeCarburant.getItems().size() > 0) {
            cmbTypeCarburant.getSelectionModel().selectFirst();
        }
    }

    private void afficherVoiture(Voiture voiture) {
        this.voitureSelectionnee = voiture;
        afficherPanel(panelAfficher);
        lblTitre.setText("Détails de la Voiture");
        
        // Remplir les champs
        lblIdAfficher.setText(String.valueOf(voiture.getId()));
        lblUtilisateurIdAfficher.setText(String.valueOf(voiture.getUtilisateurId()));
        lblNbPlacesAfficher.setText(String.valueOf(voiture.getNbPlaces()));
        lblCouleurAfficher.setText(voiture.getCouleur());
        lblTypeVoitureAfficher.setText(voiture.getTypeVoiture());
        lblCategorieAfficher.setText(voiture.getCategorie());
        lblTypeCarburantAfficher.setText(voiture.getTypeCarburant());
    }

    private void modifierVoiture(Voiture voiture) {
        this.voitureSelectionnee = voiture;
        afficherPanel(panelModifier);
        lblTitre.setText("Modifier une Voiture");
        
        // Remplir les champs
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

    // Méthodes de la vue Ajouter
    @FXML
    private void handleAnnulerAjout() {
        handleRetourListe();
    }

    @FXML
    private void handleEnregistrerAjout() {
        if (validerFormulaireAjout()) {
            try {
                Integer utilisateurId = cmbUtilisateur.getValue();
                if (utilisateurId == null) {
                    afficherErreur("Veuillez sélectionner un utilisateur");
                    return;
                }
                
                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                String couleur = txtCouleur.getText().trim();
                String typeVoiture = cmbTypeVoiture.getValue();
                String categorie = cmbCategorie.getValue();
                String typeCarburant = cmbTypeCarburant.getValue();

                // Créer une nouvelle voiture (id = 0 car il sera généré par la base de données)
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

    // Méthodes de la vue Modifier
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
                int nbPlaces = Integer.parseInt(txtNbPlacesModif.getText().trim());
                String couleur = txtCouleurModif.getText().trim();
                String typeVoiture = cmbTypeVoitureModif.getValue();
                String categorie = cmbCategorieModif.getValue();
                String typeCarburant = cmbTypeCarburantModif.getValue();

                // Mettre à jour la voiture
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

    // Méthodes de la vue Afficher
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
    
    @FXML
    private void handleEnvoyerEmail() {
        String email = txtEmail.getText().trim();
        String typeNotification = cmbTypeNotification.getValue();
        
        if (email.isEmpty()) {
            updateStatusEmail("Veuillez entrer une adresse email", Color.RED);
            return;
        }
        
        if (typeNotification == null) {
            updateStatusEmail("Veuillez sélectionner un type de notification", Color.RED);
            return;
        }
        
        // Désactiver le bouton pendant l'envoi
        btnEnvoyerEmail.setDisable(true);
        updateStatusEmail("Envoi en cours...", Color.ORANGE);
        
        // Préparer le contenu de l'email en fonction du type de notification
        String sujet = "";
        String contenu = "";
        
        switch (typeNotification) {
            case "Confirmation de réservation":
                sujet = "Confirmation de réservation de voiture";
                contenu = genererContenuReservation();
                break;
            case "Rappel avant départ":
                sujet = "Rappel : Votre départ approche";
                contenu = genererContenuRappel();
                break;
            case "Notification d'entretien":
                sujet = "Rappel d'entretien pour votre véhicule";
                contenu = genererContenuEntretien();
                break;
            default:
                sujet = "Notification concernant votre véhicule";
                contenu = genererContenuGeneral();
        }
        
        // Simuler l'envoi d'email (affichage dans la console)
        boolean success = EmailServiceSimulator.sendEmail(email, sujet, contenu);
        
        if (success) {
            updateStatusEmail("Email envoyé avec succès !", Color.GREEN);
            // Ajouter l'email à l'historique
            ajouterEmailHistorique(typeNotification, email);
        } else {
            updateStatusEmail("Échec de l'envoi de l'email.", Color.RED);
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
        
        emailsEnvoyes.add(0, entry); // Ajouter au début de la liste
    }
    
    private String genererContenuReservation() {
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
    
    private String genererContenuRappel() {
        return "<h2>Rappel de départ</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Nous vous rappelons que votre départ est prévu pour demain.</p>" +
               "<p>Détails du véhicule :</p>" +
               "<ul>" +
               "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
               "<li><b>Catégorie :</b> " + voitureSelectionnee.getCategorie() + "</li>" +
               "<li><b>Couleur :</b> " + voitureSelectionnee.getCouleur() + "</li>" +
               "</ul>" +
               "<p>Nous vous souhaitons un excellent voyage.</p>";
    }
    
    private String genererContenuEntretien() {
        return "<h2>Rappel d'entretien</h2>" +
               "<p>Bonjour,</p>" +
               "<p>Nous vous rappelons qu'il est temps de faire l'entretien de votre véhicule :</p>" +
               "<ul>" +
               "<li><b>Type de véhicule :</b> " + voitureSelectionnee.getTypeVoiture() + "</li>" +
               "<li><b>Type de carburant :</b> " + voitureSelectionnee.getTypeCarburant() + "</li>" +
               "</ul>" +
               "<p>Un entretien régulier permet de maintenir votre véhicule en bon état et d'optimiser sa consommation de carburant.</p>";
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

    // Méthodes utilitaires
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
