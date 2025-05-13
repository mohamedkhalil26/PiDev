package service;

import dao.VoitureDAO;
import model.Voiture;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les opérations liées aux voitures
 */
public class VoitureService {
    private final VoitureDAO voitureDAO;

    public VoitureService() {
        this.voitureDAO = new VoitureDAO();
    }

    /**
     * Récupère toutes les voitures
     * @return Liste de toutes les voitures
     */
    public List<Voiture> getAllVoitures() {
        try {
            return voitureDAO.getToutesLesVoitures();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des voitures: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Ajoute une nouvelle voiture
     * @param voiture La voiture à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouterVoiture(Voiture voiture) {
        try {
            voitureDAO.ajouterVoiture(voiture);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifie une voiture existante
     * @param voiture La voiture à modifier
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierVoiture(Voiture voiture) {
        try {
            voitureDAO.modifierVoiture(voiture);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification d'une voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime une voiture par son ID
     * @param id L'ID de la voiture à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerVoiture(int id) {
        try {
            voitureDAO.supprimerVoiture(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche des voitures par critère
     * @param critere Le critère de recherche
     * @return Liste des voitures correspondant au critère
     */
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
                voiture.getCategorie().toLowerCase().contains(critereLower)) {
                resultatRecherche.add(voiture);
            }
        }
        
        return resultatRecherche;
    }
}
