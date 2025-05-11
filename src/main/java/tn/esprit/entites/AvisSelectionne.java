package tn.esprit.entites;

public class AvisSelectionne {
    private static AvisSelectionne instance;

    // Private variable to store the logged-in user
    private Avis avisSelectionne;

    // Private constructor to prevent instantiation from outside
    private AvisSelectionne() {}

    // Public method to provide access to the instance
    public static AvisSelectionne getInstance() {
        if (instance == null) {
            instance = new AvisSelectionne();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setSelectedUser(Avis avis) {
        this.avisSelectionne = avis;
    }

    // Method to get the logged-in user
    public Avis getAvisSelectionne() {
        return avisSelectionne;
    }

    // Method to clear the logged-in user (e.g., on logout)
    public void clearAvisSelectionne() {
        this.avisSelectionne = null;
    }
}
