package service;

import model.EmpreinteCarbone;
import model.Trajet;
import model.Voiture;
import dao.VoitureDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour calculer l'empreinte carbone des trajets
 */
public class EmpreinteCarboneService {

    // Émissions moyennes en g CO2/km par type de carburant
    private static final double EMISSIONS_ESSENCE = 210.0;
    private static final double EMISSIONS_DIESEL = 180.0;
    private static final double EMISSIONS_HYBRIDE = 100.0;
    private static final double EMISSIONS_ELECTRIQUE = 30.0;

    // Émissions moyennes par défaut si le type de carburant est inconnu
    private static final double EMISSIONS_PAR_DEFAUT = 200.0;

    private VoitureDAO voitureDAO;

    public EmpreinteCarboneService() {
        this.voitureDAO = new VoitureDAO();
    }

    /**
     * Calcule l'empreinte carbone d'un trajet
     *
     * @param trajet Le trajet pour lequel calculer l'empreinte carbone
     * @param distance La distance du trajet en kilomètres
     * @return L'empreinte carbone du trajet
     */
    public EmpreinteCarbone calculerEmpreinteCarbone(Trajet trajet, double distance) {
        try {
            // Récupérer la voiture utilisée pour le trajet
            Voiture voiture = voitureDAO.getVoitureById(trajet.getVoitureId());

            if (voiture == null) {
                // Si la voiture n'est pas trouvée, utiliser des valeurs par défaut
                return new EmpreinteCarbone(EMISSIONS_PAR_DEFAUT, distance, trajet.getNbPlacesDisponibles() + 1);
            }

            // Déterminer les émissions en fonction du type de carburant
            double emissionsGCO2ParKm = getEmissionsParTypeCarburant(voiture.getTypeCarburant());

            // Calculer l'empreinte carbone (conducteur + passagers)
            int nbPersonnes = trajet.getNbPlacesDisponibles() + 1; // +1 pour le conducteur
            return new EmpreinteCarbone(emissionsGCO2ParKm, distance, nbPersonnes);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la voiture: " + e.getMessage());
            e.printStackTrace();

            // En cas d'erreur, retourner une empreinte carbone avec des valeurs par défaut
            return new EmpreinteCarbone(EMISSIONS_PAR_DEFAUT, distance, trajet.getNbPlacesDisponibles() + 1);
        }
    }

    /**
     * Calcule la distance entre deux villes (version simplifiée)
     * Dans une application réelle, cette méthode utiliserait une API de cartographie
     *
     * @param villeDepart Ville de départ
     * @param villeArrivee Ville d'arrivée
     * @return Distance estimée en kilomètres
     */
    public double calculerDistance(String villeDepart, String villeArrivee) {
        // Distances fictives entre quelques villes françaises (en km)
        if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Lyon")) {
            return 465.0;
        } else if (villeDepart.equalsIgnoreCase("Lyon") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 465.0;
        } else if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Marseille")) {
            return 775.0;
        } else if (villeDepart.equalsIgnoreCase("Marseille") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 775.0;
        } else if (villeDepart.equalsIgnoreCase("Lyon") && villeArrivee.equalsIgnoreCase("Marseille")) {
            return 315.0;
        } else if (villeDepart.equalsIgnoreCase("Marseille") && villeArrivee.equalsIgnoreCase("Lyon")) {
            return 315.0;
        } else if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Lille")) {
            return 220.0;
        } else if (villeDepart.equalsIgnoreCase("Lille") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 220.0;
        } else if (villeDepart.equalsIgnoreCase("Toulouse") && villeArrivee.equalsIgnoreCase("Bordeaux")) {
            return 245.0;
        } else if (villeDepart.equalsIgnoreCase("Bordeaux") && villeArrivee.equalsIgnoreCase("Toulouse")) {
            return 245.0;
        }

        // Pour les autres combinaisons, calculer une distance approximative
        // (dans une application réelle, on utiliserait une API comme Google Maps)
        return Math.random() * 500 + 100; // Entre 100 et 600 km
    }

    /**
     * Détermine les émissions de CO2 par km en fonction du type de carburant
     *
     * @param typeCarburant Le type de carburant de la voiture
     * @return Les émissions en g CO2/km
     */
    private double getEmissionsParTypeCarburant(String typeCarburant) {
        if (typeCarburant == null) {
            return EMISSIONS_PAR_DEFAUT;
        }

        switch (typeCarburant.toLowerCase()) {
            case "essence":
                return EMISSIONS_ESSENCE;
            case "diesel":
                return EMISSIONS_DIESEL;
            case "hybride":
                return EMISSIONS_HYBRIDE;
            case "electrique":
                return EMISSIONS_ELECTRIQUE;
            default:
                return EMISSIONS_PAR_DEFAUT;
        }
    }

    /**
     * Récupère le type de carburant d'une voiture par son ID
     *
     * @param voitureId L'ID de la voiture
     * @return Le type de carburant, ou "essence" par défaut si la voiture n'est pas trouvée
     */
    public String getTypeCarburant(int voitureId) {
        try {
            Voiture voiture = voitureDAO.getVoitureById(voitureId);
            if (voiture != null) {
                return voiture.getTypeCarburant();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du type de carburant: " + e.getMessage());
            e.printStackTrace();
        }
        return "essence"; // Valeur par défaut
    }

    /**
     * Récupère la liste de tous les types de carburant disponibles
     *
     * @return Liste des types de carburant
     */
    public List<String> getTypesCarburant() {
        List<String> types = new ArrayList<>();
        types.add("essence");
        types.add("diesel");
        types.add("hybride");
        types.add("electrique");
        return types;
    }
}
