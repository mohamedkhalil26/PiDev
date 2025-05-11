package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabsViewController {

    @FXML private TabPane tabPane;
    @FXML private Tab reservationsTab;
    @FXML private Tab reclamationsTab;

    public void selectReservationsTab() {
        tabPane.getSelectionModel().select(reservationsTab);
    }

    public void selectReclamationsTab() {
        tabPane.getSelectionModel().select(reclamationsTab);
    }
}


