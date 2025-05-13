package service;

import dao.UtilisateurDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {
    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
     * Récupère tous les IDs d'utilisateurs
     * @return Liste des IDs d'utilisateurs
     */
    public List<Integer> getAllUtilisateurIds() {
        try {
            return utilisateurDAO.getAllUtilisateurIds();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des IDs d'utilisateurs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Vérifie si un utilisateur existe
     * @param id L'ID de l'utilisateur à vérifier
     * @return true si l'utilisateur existe, false sinon
     */
    public boolean utilisateurExists(int id) {
        try {
            return utilisateurDAO.utilisateurExists(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
