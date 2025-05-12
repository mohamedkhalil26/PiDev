package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabsViewController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabReclamations;

    @FXML
    private Tab tabReservations;

    @FXML
    public void initialize() {
        // Ce code est exécuté automatiquement après le chargement du FXML
        System.out.println("TabsViewController initialisé !");
        // Tu peux ajouter ici une logique de sélection initiale ou autre
        tabPane.getSelectionModel().select(tabReclamations);
    }

    public void allerAReclamations() {
        tabPane.getSelectionModel().select(tabReclamations);
    }

    public void allerAReservations() {
        tabPane.getSelectionModel().select(tabReservations);
    }
}
