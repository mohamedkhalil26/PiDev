package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import model.Trajet;
import service.TrajetService;
import utils.EmailServiceSimulator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class TestAutoEmailController {

    @FXML
    private TextField txtVilleDepart;
    
    @FXML
    private TextField txtVilleArrivee;
    
    @FXML
    private TextField txtNbPlaces;
    
    @FXML
    private TextField txtPrix;
    
    @FXML
    private ComboBox<Integer> cmbJour;
    
    @FXML
    private ComboBox<String> cmbMois;
    
    @FXML
    private ComboBox<Integer> cmbAnnee;
    
    @FXML
    private ComboBox<Integer> cmbHeure;
    
    @FXML
    private ComboBox<Integer> cmbMinute;
    
    @FXML
    private TextField txtTrajetId;
    
    @FXML
    private TextField txtPlacesReserver;
    
    @FXML
    private TextField txtTrajetRappelId;
    
    @FXML
    private Label lblStatus;
    
    private TrajetService trajetService = new TrajetService();
    
    @FXML
    public void initialize() {
        // Initialiser les ComboBox pour la date
        for (int i = 1; i <= 31; i++) {
            cmbJour.getItems().add(i);
        }
        
        String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", 
                         "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        cmbMois.getItems().addAll(mois);
        
        int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = anneeActuelle; i <= anneeActuelle + 2; i++) {
            cmbAnnee.getItems().add(i);
        }
        
        // Initialiser les ComboBox pour l'heure
        for (int i = 0; i < 24; i++) {
            cmbHeure.getItems().add(i);
        }
        
        for (int i = 0; i < 60; i += 5) {
            cmbMinute.getItems().add(i);
        }
        
        // Valeurs par défaut
        LocalDate aujourdhui = LocalDate.now();
        cmbJour.setValue(aujourdhui.getDayOfMonth());
        cmbMois.setValue(mois[aujourdhui.getMonthValue() - 1]);
        cmbAnnee.setValue(aujourdhui.getYear());
        
        LocalTime maintenant = LocalTime.now();
        cmbHeure.setValue(maintenant.getHour());
        cmbMinute.setValue(maintenant.getMinute() / 5 * 5); // Arrondir à 5 minutes près
        
        txtVilleDepart.setText("Paris");
        txtVilleArrivee.setText("Lyon");
        txtNbPlaces.setText("3");
        txtPrix.setText("25.50");
        
        updateStatus("Prêt à tester l'envoi automatique d'emails", Color.BLACK);
    }
    
    @FXML
    private void handleCreerTrajet() {
        try {
            // Récupérer les valeurs des champs
            String villeDepart = txtVilleDepart.getText().trim();
            String villeArrivee = txtVilleArrivee.getText().trim();
            int nbPlaces = Integer.parseInt(txtNbPlaces.getText().trim());
            double prix = Double.parseDouble(txtPrix.getText().trim().replace(',', '.'));
            
            // Construire la date et l'heure
            int jour = cmbJour.getValue();
            String moisStr = cmbMois.getValue();
            int mois = getMoisIndex(moisStr) + 1; // +1 car les mois commencent à 1 dans LocalDate
            int annee = cmbAnnee.getValue();
            int heure = cmbHeure.getValue();
            int minute = cmbMinute.getValue();
            
            LocalDateTime dateHeure = LocalDateTime.of(annee, mois, jour, heure, minute);
            
            // Créer le trajet
            Trajet trajet = new Trajet(0, villeDepart, villeArrivee, dateHeure, nbPlaces, prix, 1, 1);
            
            // Ajouter le trajet (l'email sera envoyé automatiquement)
            boolean success = trajetService.ajouterTrajet(trajet);
            
            if (success) {
                updateStatus("Trajet créé avec succès et email envoyé automatiquement", Color.GREEN);
                txtTrajetId.setText(String.valueOf(trajet.getId()));
                txtTrajetRappelId.setText(String.valueOf(trajet.getId()));
            } else {
                updateStatus("Erreur lors de la création du trajet", Color.RED);
            }
            
        } catch (NumberFormatException e) {
            updateStatus("Erreur: Veuillez entrer des valeurs numériques valides", Color.RED);
        } catch (Exception e) {
            updateStatus("Erreur: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleReserver() {
        try {
            // Récupérer les valeurs des champs
            int trajetId = Integer.parseInt(txtTrajetId.getText().trim());
            int nbPlaces = Integer.parseInt(txtPlacesReserver.getText().trim());
            
            // Réserver les places (l'email sera envoyé automatiquement)
            boolean success = trajetService.reserverPlaces(trajetId, "takwabouabid149@gmail.com", nbPlaces);
            
            if (success) {
                updateStatus("Réservation effectuée avec succès et email envoyé automatiquement", Color.GREEN);
            } else {
                updateStatus("Erreur lors de la réservation", Color.RED);
            }
            
        } catch (NumberFormatException e) {
            updateStatus("Erreur: Veuillez entrer des valeurs numériques valides", Color.RED);
        } catch (Exception e) {
            updateStatus("Erreur: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRappel() {
        try {
            // Récupérer les valeurs des champs
            int trajetId = Integer.parseInt(txtTrajetRappelId.getText().trim());
            
            // Récupérer le trajet
            Trajet trajet = trajetService.getTrajetById(trajetId);
            
            if (trajet != null) {
                // Simuler un rappel 24h avant départ
                boolean success = EmailServiceSimulator.envoyerRappelAvantDepart(trajet, "takwabouabid149@gmail.com", true);
                
                if (success) {
                    updateStatus("Rappel simulé avec succès", Color.GREEN);
                } else {
                    updateStatus("Erreur lors de la simulation du rappel", Color.RED);
                }
            } else {
                updateStatus("Erreur: Trajet non trouvé", Color.RED);
            }
            
        } catch (NumberFormatException e) {
            updateStatus("Erreur: Veuillez entrer un ID de trajet valide", Color.RED);
        } catch (Exception e) {
            updateStatus("Erreur: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }
    
    private int getMoisIndex(String mois) {
        String[] moiss = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                         "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        
        for (int i = 0; i < moiss.length; i++) {
            if (moiss[i].equals(mois)) {
                return i;
            }
        }
        
        return 0; // Par défaut, janvier
    }
    
    private void updateStatus(String message, Color color) {
        lblStatus.setText(message);
        lblStatus.setTextFill(color);
    }
}
