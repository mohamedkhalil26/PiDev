package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentPane;

    private TabsViewController tabsViewController;

    @FXML
    public void initialize() {
        System.out.println("✅ MainController initialisé");
        setCenterView("/TabsView.fxml");
        chargerSidebar();
    }

    private void chargerSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sidebarback.fxml"));
            AnchorPane sidebar = loader.load();

            SidebarbackController controller = loader.getController();
            controller.setMainController(this);

            if (contentPane.getParent() instanceof BorderPane borderPane) {
                borderPane.setLeft(sidebar);
            }
        } catch (IOException e) {
            showError("Erreur de chargement de la barre latérale", e);
        }
    }

    public void setCenterView(String fxmlPath) {
        try {
            System.out.println("🔄 Chargement de : " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            if (fxmlPath.endsWith("TabsView.fxml")) {
                tabsViewController = loader.getController();
            }

            System.out.println("✅ Vue affichée : " + fxmlPath);
        } catch (IOException e) {
            System.err.println("❌ Échec du chargement de : " + fxmlPath);
            e.printStackTrace();
            showError("Erreur de chargement", e);
        }
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
