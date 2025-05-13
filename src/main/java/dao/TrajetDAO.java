package dao;

import model.Trajet;
import utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {

    public void ajouterTrajet(Trajet trajet) throws SQLException {
        String sql = "INSERT INTO trajet(ville_depart, ville_arrivee, date_heure_depart, nb_places_disponibles, " +
                "prix_par_place, conducteur_id, voiture_id, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, trajet.getVilleDepart());
            stmt.setString(2, trajet.getVilleArrivee());
            stmt.setTimestamp(3, Timestamp.valueOf(trajet.getDateHeureDepart()));
            stmt.setInt(4, trajet.getNbPlacesDisponibles());
            stmt.setDouble(5, trajet.getPrixParPlace());
            stmt.setInt(6, trajet.getConducteurId());
            stmt.setInt(7, trajet.getVoitureId());
            stmt.setString(8, trajet.getStatut());
            
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trajet.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Trajet> getTousLesTrajets() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                trajets.add(extraireTrajetDuResultSet(rs));
            }
        }
        
        return trajets;
    }

    public Trajet getTrajetParId(int id) throws SQLException {
        String sql = "SELECT * FROM trajet WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraireTrajetDuResultSet(rs);
                }
            }
        }
        
        return null;
    }

    public void modifierTrajet(Trajet trajet) throws SQLException {
        String sql = "UPDATE trajet SET ville_depart = ?, ville_arrivee = ?, date_heure_depart = ?, " +
                "nb_places_disponibles = ?, prix_par_place = ?, conducteur_id = ?, voiture_id = ?, statut = ? " +
                "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, trajet.getVilleDepart());
            stmt.setString(2, trajet.getVilleArrivee());
            stmt.setTimestamp(3, Timestamp.valueOf(trajet.getDateHeureDepart()));
            stmt.setInt(4, trajet.getNbPlacesDisponibles());
            stmt.setDouble(5, trajet.getPrixParPlace());
            stmt.setInt(6, trajet.getConducteurId());
            stmt.setInt(7, trajet.getVoitureId());
            stmt.setString(8, trajet.getStatut());
            stmt.setInt(9, trajet.getId());
            
            stmt.executeUpdate();
        }
    }

    public void supprimerTrajet(int id) throws SQLException {
        String sql = "DELETE FROM trajet WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM trajet WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (villeDepart != null && !villeDepart.trim().isEmpty()) {
            sqlBuilder.append(" AND ville_depart LIKE ?");
            params.add("%" + villeDepart + "%");
        }
        
        if (villeArrivee != null && !villeArrivee.trim().isEmpty()) {
            sqlBuilder.append(" AND ville_arrivee LIKE ?");
            params.add("%" + villeArrivee + "%");
        }
        
        if (dateMin != null) {
            sqlBuilder.append(" AND date_heure_depart >= ?");
            params.add(Timestamp.valueOf(dateMin));
        }
        
        if (dateMax != null) {
            sqlBuilder.append(" AND date_heure_depart <= ?");
            params.add(Timestamp.valueOf(dateMax));
        }
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(extraireTrajetDuResultSet(rs));
                }
            }
        }
        
        return trajets;
    }

    public List<Trajet> getTrajetsByConducteur(int conducteurId) throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE conducteur_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, conducteurId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(extraireTrajetDuResultSet(rs));
                }
            }
        }
        
        return trajets;
    }

    public List<Trajet> getTrajetsByVoiture(int voitureId) throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE voiture_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, voitureId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(extraireTrajetDuResultSet(rs));
                }
            }
        }
        
        return trajets;
    }

    private Trajet extraireTrajetDuResultSet(ResultSet rs) throws SQLException {
        return new Trajet(
                rs.getInt("id"),
                rs.getString("ville_depart"),
                rs.getString("ville_arrivee"),
                rs.getTimestamp("date_heure_depart").toLocalDateTime(),
                rs.getInt("nb_places_disponibles"),
                rs.getDouble("prix_par_place"),
                rs.getInt("conducteur_id"),
                rs.getInt("voiture_id")
        );
    }
}
