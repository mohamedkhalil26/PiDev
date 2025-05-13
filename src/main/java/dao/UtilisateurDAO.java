package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.DatabaseUtil;

public class UtilisateurDAO {

    /**
     * Récupère tous les utilisateurs de la base de données
     * @return Liste des IDs d'utilisateurs
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Integer> getAllUtilisateurIds() throws SQLException {
        List<Integer> utilisateurIds = new ArrayList<>();
        String sql = "SELECT id FROM utilisateur";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurIds.add(rs.getInt("id"));
            }
        }
        
        return utilisateurIds;
    }
    
    /**
     * Vérifie si un utilisateur existe dans la base de données
     * @param id L'ID de l'utilisateur à vérifier
     * @return true si l'utilisateur existe, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean utilisateurExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE id = " + id;
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
}
