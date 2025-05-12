package tn.esprit.entites;
import tn.esprit.entites.*;
public class UtilisateurConnecte {
    // Private static instance of the class
    private static UtilisateurConnecte instance;

    // Private variable to store the logged-in user
    private Utilisateur UtilisateurConnecte;

    // Private constructor to prevent instantiation from outside
    private UtilisateurConnecte() {}

    // Public method to provide access to the instance
    public static UtilisateurConnecte getInstance() {
        if (instance == null) {
            instance = new UtilisateurConnecte();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setUtilisateurConnecte(Utilisateur user) {
        this.UtilisateurConnecte = user;
    }

    // Method to get the logged-in user
    public Utilisateur getUtilisateurConnecter() {
        return UtilisateurConnecte;
    }

    // Method to clear the logged-in user (e.g., on logout)
    public void clearUtilisateurConnecte() {
        this.UtilisateurConnecte = null;
    }
}