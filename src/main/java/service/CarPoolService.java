package service;

import dao.TrajetDAO;
import dao.UtilisateurDAO;
import dao.VoitureDAO;
import model.Trajet;
import model.Voiture;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for managing core carpooling operations: users, vehicles, and trips.
 */
public class CarPoolService {
    private final UtilisateurDAO utilisateurDAO;
    private final VoitureDAO voitureDAO;
    private final TrajetDAO trajetDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public CarPoolService() {
        this.utilisateurDAO = new UtilisateurDAO();
        this.voitureDAO = new VoitureDAO();
        this.trajetDAO = new TrajetDAO();
    }

    // User-related methods
    public List<Integer> getAllUtilisateurIds() {
        try {
            return utilisateurDAO.getAllUtilisateurIds();
        } catch (SQLException e) {
            System.err.println("Error retrieving user IDs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean utilisateurExists(int id) {
        try {
            return utilisateurDAO.utilisateurExists(id);
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Vehicle-related methods
    public List<Voiture> getAllVoitures() {
        try {
            return voitureDAO.getToutesLesVoitures();
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicles: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean ajouterVoiture(Voiture voiture) {
        try {
            voitureDAO.ajouterVoiture(voiture);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierVoiture(Voiture voiture) {
        try {
            voitureDAO.modifierVoiture(voiture);
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerVoiture(int id) {
        try {
            voitureDAO.supprimerVoiture(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Voiture> rechercherVoitures(String critere) {
        List<Voiture> toutesVoitures = getAllVoitures();
        List<Voiture> resultatRecherche = new ArrayList<>();

        if (critere == null || critere.trim().isEmpty()) {
            return toutesVoitures;
        }

        String critereLower = critere.toLowerCase();

        for (Voiture voiture : toutesVoitures) {
            if (String.valueOf(voiture.getId()).contains(critereLower) ||
                    String.valueOf(voiture.getUtilisateurId()).contains(critereLower) ||
                    String.valueOf(voiture.getNbPlaces()).contains(critereLower) ||
                    voiture.getCouleur().toLowerCase().contains(critereLower) ||
                    voiture.getTypeVoiture().toLowerCase().contains(critereLower) ||
                    voiture.getCategorie().toLowerCase().contains(critereLower) ||
                    voiture.getTypeCarburant().toLowerCase().contains(critereLower)) {
                resultatRecherche.add(voiture);
            }
        }

        return resultatRecherche;
    }

    // Trip-related methods
    public List<Trajet> getAllTrajets() {
        try {
            return trajetDAO.getTousLesTrajets();
        } catch (SQLException e) {
            System.err.println("Error retrieving trips: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Trajet getTrajetById(int id) {
        try {
            return trajetDAO.getTrajetParId(id);
        } catch (SQLException e) {
            System.err.println("Error retrieving trip: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean ajouterTrajet(Trajet trajet) {
        try {
            trajetDAO.ajouterTrajet(trajet);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding trip: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierTrajet(Trajet trajet) {
        try {
            trajetDAO.modifierTrajet(trajet);
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating trip: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerTrajet(int id) {
        try {
            trajetDAO.supprimerTrajet(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting trip: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee, LocalDateTime dateMin, LocalDateTime dateMax) {
        try {
            return trajetDAO.rechercherTrajets(villeDepart, villeArrivee, dateMin, dateMax);
        } catch (SQLException e) {
            System.err.println("Error searching trips: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Trajet> getTrajetsByConducteur(int conducteurId) {
        try {
            return trajetDAO.getTrajetsByConducteur(conducteurId);
        } catch (SQLException e) {
            System.err.println("Error retrieving driver trips: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Trajet> getTrajetsByVoiture(int voitureId) {
        try {
            return trajetDAO.getTrajetsByVoiture(voitureId);
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicle trips: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean reserverPlaces(int trajetId, String emailPassager, int nombrePlaces) {
        try {
            if (!EMAIL_PATTERN.matcher(emailPassager).matches()) {
                System.err.println("Invalid email format: " + emailPassager);
                return false;
            }
            if (nombrePlaces <= 0) {
                System.err.println("Number of seats must be positive: " + nombrePlaces);
                return false;
            }
            Trajet trajet = trajetDAO.getTrajetParId(trajetId);
            if (trajet == null) {
                System.err.println("Trip not found: ID " + trajetId);
                return false;
            }
            if (trajet.getNbPlacesDisponibles() < nombrePlaces) {
                System.err.println("Not enough available seats: " + trajet.getNbPlacesDisponibles() + " < " + nombrePlaces);
                return false;
            }

            int nouvellePlacesDisponibles = trajet.getNbPlacesDisponibles() - nombrePlaces;
            trajet.setNbPlacesDisponibles(nouvellePlacesDisponibles);
            trajetDAO.modifierTrajet(trajet);
            // TODO: Persist reservation details (e.g., in a reservations table)
            return true;
        } catch (SQLException e) {
            System.err.println("Error reserving seats: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}