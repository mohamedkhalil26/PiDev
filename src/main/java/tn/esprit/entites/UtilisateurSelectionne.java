package tn.esprit.entites;
import tn.esprit.entites.*;
public class UtilisateurSelectionne {




    // Private static instance of the class
    private static UtilisateurSelectionne instance;

    // Private variable to store the logged-in user
    private Utilisateur SelectedUser;

    // Private constructor to prevent instantiation from outside
    private UtilisateurSelectionne() {}

    // Public method to provide access to the instance
    public static UtilisateurSelectionne getInstance() {
        if (instance == null) {
            instance = new UtilisateurSelectionne();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setSelectedUser(Utilisateur user) {
        this.SelectedUser = user;
    }

    // Method to get the logged-in user
    public Utilisateur getSelectedUserr() {
        return SelectedUser;
    }

    // Method to clear the logged-in user (e.g., on logout)
    public void clearSelectedUser() {
        this.SelectedUser = null;
    }
}