package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Trajet;
import model.Voiture;
import service.TrajetService;
import service.VoitureService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierTrajetController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtVilleDepart;

    @FXML
    private TextField txtVilleArrivee;

    @FXML
    private DatePicker dateDepart;

    @FXML
    private ComboBox<String> cmbHeure;

    @FXML
    private ComboBox<String> cmbMinute;

    @FXML
    private TextField txtNbPlaces;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtConducteurId;

    @FXML
    private ComboBox<Voiture> cmbVoiture;

    @FXML
    private ComboBox<String> cmbStatut;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnEnregistrer;

    private TrajetService trajetService;
    private VoitureService voitureService;
    private Trajet trajet;
    private ListeTrajetController listeTrajetController;
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trajetService = new TrajetService();
        voitureService = new VoitureService();

        // Configuration du DatePicker
        dateDepart.setConverter(new StringConverter<LocalDate>() {
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

        // Initialiser les ComboBox
        if (cmbHeure.getItems().isEmpty()) {
            for (int i = 0; i < 24; i++) {
                cmbHeure.getItems().add(String.format("%02d", i));
            }
        }

        if (cmbMinute.getItems().isEmpty()) {
            for (int i = 0; i < 60; i += 5) {
                cmbMinute.getItems().add(String.format("%02d", i));
            }
        }

        // Charger les voitures
        chargerVoitures();

        // Configurer le ComboBox des voitures
        cmbVoiture.setConverter(new StringConverter<Voiture>() {
            @Override
            public String toString(Voiture voiture) {
                return voiture != null ? voiture.getTypeVoiture() + " - " + voiture.getCouleur() : "";
            }

            @Override
            public Voiture fromString(String string) {
                return null; // Non utilisé
            }
        });

        // Initialiser le statut
        if (cmbStatut.getItems().isEmpty()) {
            cmbStatut.getItems().addAll("planifié", "en cours", "terminé", "annulé");
        }
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        remplirFormulaire();
    }

    private void remplirFormulaire() {
        if (trajet != null) {
            txtId.setText(String.valueOf(trajet.getId()));
            txtVilleDepart.setText(trajet.getVilleDepart());
            txtVilleArrivee.setText(trajet.getVilleArrivee());

            LocalDateTime dateHeure = trajet.getDateHeureDepart();
            dateDepart.setValue(dateHeure.toLocalDate());
            cmbHeure.setValue(String.format("%02d", dateHeure.getHour()));
            cmbMinute.setValue(String.format("%02d", dateHeure.getMinute() - (dateHeure.getMinute() % 5)));

            txtNbPlaces.setText(String.valueOf(trajet.getNbPlacesDisponibles()));
            txtPrix.setText(String.format("%.2f", trajet.getPrixParPlace()).replace(',', '.'));
            txtConducteurId.setText(String.valueOf(trajet.getConducteurId()));

            // Sélectionner la voiture
            for (Voiture voiture : voituresList) {
                if (voiture.getId() == trajet.getVoitureId()) {
                    cmbVoiture.setValue(voiture);
                    break;
                }
            }

            cmbStatut.setValue(trajet.getStatut());
        }
    }

    private void chargerVoitures() {
        List<Voiture> voitures = voitureService.getAllVoitures();
        voituresList.clear();
        voituresList.addAll(voitures);
        cmbVoiture.setItems(voituresList);
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    @FXML
    private void handleEnregistrer() {
        if (validerFormulaire()) {
            try {
                String villeDepart = txtVilleDepart.getText().trim();
                String villeArrivee = txtVilleArrivee.getText().trim();

                LocalDate date = dateDepart.getValue();
                if (date == null) {
                    afficherErreur("Veuillez sélectionner une date de départ");
                    return;
                }

                int heure = 0;
                int minute = 0;
                try {
                    heure = Integer.parseInt(cmbHeure.getValue());
                    minute = Integer.parseInt(cmbMinute.getValue());
                } catch (NumberFormatException e) {
                    afficherErreur("Format d'heure ou de minute invalide");
                    return;
                }

                LocalDateTime dateHeureDepart = LocalDateTime.of(date, LocalTime.of(heure, minute));

                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                double prix = Double.parseDouble(txtPrix.getText().trim().replace(',', '.'));
                int voitureId = cmbVoiture.getValue().getId();
                String statut = cmbStatut.getValue();

                // Mettre à jour le trajet
                trajet.setVilleDepart(villeDepart);
                trajet.setVilleArrivee(villeArrivee);
                trajet.setDateHeureDepart(dateHeureDepart);
                trajet.setNbPlacesDisponibles(nbPlaces);
                trajet.setPrixParPlace(prix);
                trajet.setVoitureId(voitureId);
                trajet.setStatut(statut);

                boolean success = trajetService.modifierTrajet(trajet);

                if (success) {
                    afficherConfirmation("Trajet modifié avec succès");
                    if (listeTrajetController != null) {
                        listeTrajetController.refreshTrajets();
                    }
                    fermerFenetre();
                } else {
                    afficherErreur("Erreur lors de la modification du trajet");
                }

            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer des valeurs numériques valides");
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (txtVilleDepart.getText().trim().isEmpty()) {
            erreurs.append("- La ville de départ est requise\n");
        }

        if (txtVilleArrivee.getText().trim().isEmpty()) {
            erreurs.append("- La ville d'arrivée est requise\n");
        }

        if (dateDepart.getValue() == null) {
            erreurs.append("- La date de départ est requise\n");
        }

        if (txtNbPlaces.getText().trim().isEmpty()) {
            erreurs.append("- Le nombre de places est requis\n");
        } else {
            try {
                int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
                if (nbPlaces <= 0) {
                    erreurs.append("- Le nombre de places doit être supérieur à 0\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le nombre de places doit être un nombre\n");
            }
        }

        if (txtPrix.getText().trim().isEmpty()) {
            erreurs.append("- Le prix est requis\n");
        } else {
            try {
                double prix = Double.parseDouble(txtPrix.getText().trim().replace(',', '.'));
                if (prix < 0) {
                    erreurs.append("- Le prix ne peut pas être négatif\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le prix doit être un nombre\n");
            }
        }

        if (cmbVoiture.getValue() == null) {
            erreurs.append("- La voiture est requise\n");
        }

        if (cmbStatut.getValue() == null) {
            erreurs.append("- Le statut est requis\n");
        }

        if (erreurs.length() > 0) {
            afficherErreur("Veuillez corriger les erreurs suivantes :\n" + erreurs.toString());
            return false;
        }

        return true;
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    public void setListeTrajetController(ListeTrajetController controller) {
        this.listeTrajetController = controller;
    }
}
