package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import tn.esprit.entites.Reservation;
import tn.esprit.services.ReservationService;
import tn.esprit.api.SmsSender;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    @FXML private TableView<Reservation> tableViewReservations;
    @FXML private TableColumn<Reservation, Integer> idcovoiturageCol;
    @FXML private TableColumn<Reservation, Integer> passagerCol;
    @FXML private TableColumn<Reservation, Integer> nbredeplacesCol;
    @FXML private TableColumn<Reservation, Double> prixCol;
    @FXML private TextField txtidcovoiturage, txtidpassager, txtnombredeplaces, txtprix;
    @FXML private ImageView qrCodeImageView;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        System.out.println("🔧 Méthode initialize() appelée");

        idcovoiturageCol.setCellValueFactory(new PropertyValueFactory<>("idCovoiturage"));
        passagerCol.setCellValueFactory(new PropertyValueFactory<>("idPassager"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        nbredeplacesCol.setCellValueFactory(new PropertyValueFactory<>("nbPlacesReservees"));

        afficherReservations();

        tableViewReservations.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                try {
                    afficherQRCodeDansInterface(newSel);
                    remplirChampsDepuisSelection(newSel);
                } catch (Exception e) {
                    showAlert("Erreur QR", "Impossible d'afficher le QR Code : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void ajouterReservation() {
        try {
            int idCovoiturage = Integer.parseInt(txtidcovoiturage.getText());
            int idPassager = Integer.parseInt(txtidpassager.getText());
            double prix = Double.parseDouble(txtprix.getText());
            int nbPlaces = Integer.parseInt(txtnombredeplaces.getText());

            Reservation reservation = new Reservation(idCovoiturage, idPassager, prix, nbPlaces);
            reservationService.insert(reservation);
            afficherReservations();
            viderChamps();
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer des valeurs numériques valides.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void modifierReservation() {
        Reservation selected = tableViewReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une réservation à modifier.");
            return;
        }

        try {
            double prix = Double.parseDouble(txtprix.getText());
            int nbPlaces = Integer.parseInt(txtnombredeplaces.getText());

            selected.setPrix(prix);
            selected.setNbPlacesReservees(nbPlaces);

            reservationService.update(selected);
            afficherReservations();
            viderChamps();

            showAlert("Succès", "Réservation modifiée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer des valeurs numériques valides.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void supprimerReservation() {
        Reservation selected = tableViewReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une réservation à supprimer.");
            return;
        }

        try {
            reservationService.delete(selected.getIdReservation());

            File qrFile = new File("qrcode_reservation_" + selected.getIdReservation() + ".png");
            if (qrFile.exists()) qrFile.delete();

            afficherReservations();
            viderChamps();

            showAlert("Succès", "Réservation supprimée avec succès !");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void confirmerReservation() {
        Reservation selected = tableViewReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Aucune réservation sélectionnée.");
            return;
        }

        try {
            SmsSender.envoyerSms("+21697808162", "Votre réservation ID " + selected.getIdReservation() + " a été confirmée !");
            showAlert("Succès", "Réservation confirmée et SMS envoyé !");
        } catch (com.twilio.exception.ApiException e) {
            showAlert("Erreur", "Erreur lors de l'envoi du SMS : Code " + e.getCode() + " - " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la confirmation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherQRCodeDansInterface(Reservation reservation) throws WriterException, IOException {
        String qrContent = "Réservation ID: " + reservation.getIdReservation() +
                "\nCovoiturage: " + reservation.getIdCovoiturage() +
                "\nPassager: " + reservation.getIdPassager() +
                "\nPrix: " + reservation.getPrix() +
                "\nPlaces: " + reservation.getNbPlacesReservees();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image qrImage = SwingFXUtils.toFXImage(bufferedImage, null);
        qrCodeImageView.setImage(qrImage);
    }

    private void afficherReservations() {
        List<Reservation> reservations = reservationService.getAll();

        if (reservations == null) {
            System.out.println("⚠️ Liste NULL, remplacement par liste vide");
            reservations = new ArrayList<>();
        }

        System.out.println("📋 Réservations à afficher : " + reservations.size());
        ObservableList<Reservation> list = FXCollections.observableArrayList(reservations);
        tableViewReservations.setItems(list);
    }

    private void remplirChampsDepuisSelection(Reservation r) {
        txtidcovoiturage.setText(String.valueOf(r.getIdCovoiturage()));
        txtidpassager.setText(String.valueOf(r.getIdPassager()));
        txtprix.setText(String.valueOf(r.getPrix()));
        txtnombredeplaces.setText(String.valueOf(r.getNbPlacesReservees()));
    }

    private void viderChamps() {
        txtidcovoiturage.clear();
        txtidpassager.clear();
        txtprix.clear();
        txtnombredeplaces.clear();
        qrCodeImageView.setImage(null);
        tableViewReservations.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

