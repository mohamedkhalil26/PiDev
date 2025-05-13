package service;

import dao.VoitureDAO;
import model.EmpreinteCarbone;
import model.Trajet;
import model.Voiture;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for calculating carbon footprints of trips.
 * Emission values are based on approximate gCO2/km for different fuel types.
 */
public class CarbonFootprintService {
    // Emission factors in gCO2/km (approximate, source: simplified industry averages)
    private static final double EMISSIONS_ESSENCE = 210.0;
    private static final double EMISSIONS_DIESEL = 180.0;
    private static final double EMISSIONS_HYBRIDE = 100.0;
    private static final double EMISSIONS_ELECTRIQUE = 30.0;
    private static final double EMISSIONS_PAR_DEFAUT = 200.0;

    private final VoitureDAO voitureDAO;

    public CarbonFootprintService() {
        this.voitureDAO = new VoitureDAO();
    }

    public EmpreinteCarbone calculerEmpreinteCarbone(Trajet trajet, double distance) {
        try {
            Voiture voiture = voitureDAO.getVoitureById(trajet.getVoitureId());
            double emissionsGCO2ParKm = voiture == null ? EMISSIONS_PAR_DEFAUT : getEmissionsParTypeCarburant(voiture.getTypeCarburant());
            // Assume total passengers = total seats - available seats + driver
            int nbPersonnes = voiture != null ? (voiture.getNbPlaces() - trajet.getNbPlacesDisponibles() + 1) : 1;
            return new EmpreinteCarbone(emissionsGCO2ParKm, distance, nbPersonnes);
        } catch (SQLException e) {
            System.err.println("Error retrieving vehicle: " + e.getMessage());
            e.printStackTrace();
            return new EmpreinteCarbone(EMISSIONS_PAR_DEFAUT, distance, 1);
        }
    }

    public double calculerDistance(String villeDepart, String villeArrivee) {
        if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Lyon") ||
                villeDepart.equalsIgnoreCase("Lyon") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 465.0;
        } else if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Marseille") ||
                villeDepart.equalsIgnoreCase("Marseille") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 775.0;
        } else if (villeDepart.equalsIgnoreCase("Lyon") && villeArrivee.equalsIgnoreCase("Marseille") ||
                villeDepart.equalsIgnoreCase("Marseille") && villeArrivee.equalsIgnoreCase("Lyon")) {
            return 315.0;
        } else if (villeDepart.equalsIgnoreCase("Paris") && villeArrivee.equalsIgnoreCase("Lille") ||
                villeDepart.equalsIgnoreCase("Lille") && villeArrivee.equalsIgnoreCase("Paris")) {
            return 220.0;
        } else if (villeDepart.equalsIgnoreCase("Toulouse") && villeArrivee.equalsIgnoreCase("Bordeaux") ||
                villeDepart.equalsIgnoreCase("Bordeaux") && villeArrivee.equalsIgnoreCase("Toulouse")) {
            return 245.0;
        }
        // TODO: Replace with real distance API or database lookup
        return 300.0; // Default distance for unknown routes
    }

    private double getEmissionsParTypeCarburant(String typeCarburant) {
        if (typeCarburant == null) return EMISSIONS_PAR_DEFAUT;
        switch (typeCarburant.toLowerCase()) {
            case "essence": return EMISSIONS_ESSENCE;
            case "diesel": return EMISSIONS_DIESEL;
            case "hybride": return EMISSIONS_HYBRIDE;
            case "electrique": return EMISSIONS_ELECTRIQUE;
            default: return EMISSIONS_PAR_DEFAUT;
        }
    }

    public String getTypeCarburant(int voitureId) {
        try {
            Voiture voiture = voitureDAO.getVoitureById(voitureId);
            return voiture != null ? voiture.getTypeCarburant() : "essence";
        } catch (SQLException e) {
            System.err.println("Error retrieving fuel type: " + e.getMessage());
            e.printStackTrace();
            return "essence";
        }
    }

    public List<String> getTypesCarburant() {
        List<String> types = new ArrayList<>();
        types.add("Essence");
        types.add("Diesel");
        types.add("Hybride");
        types.add("Electrique");
        return types;
    }
}