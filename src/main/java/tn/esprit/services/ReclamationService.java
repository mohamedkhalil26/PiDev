package tn.esprit.services;

import tn.esprit.entites.Reclamation;
import tn.esprit.outils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    public void ajouter(Reclamation reclamation, String utilisateur) throws SQLException {
        String sql = "INSERT INTO reclamation (sujet, description, reservation_id, statut) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getDescription());
            ps.setInt(3, reclamation.getReservationId());
            ps.setString(4, reclamation.getStatut());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                reclamation.setId(rs.getInt(1));
            }
        }
    }

    public List<Reclamation> afficher() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getInt("reservation_id")
                );
                r.setId(rs.getInt("id"));
                String statut = rs.getString("statut");
                if (statut == null || statut.trim().isEmpty()) {
                    r.setStatut("EN_ATTENTE");
                } else {
                    try {
                        r.setStatut(statut.trim());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid statut found for reclamation ID " + r.getId() + ": " + statut);
                        r.setStatut("EN_ATTENTE");
                    }
                }
                reclamations.add(r);
            }
        }
        return reclamations;
    }

    public void modifier(Reclamation reclamation, String utilisateur) throws SQLException {
        String sql = "UPDATE reclamation SET sujet = ?, description = ?, reservation_id = ?, statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getDescription());
            ps.setInt(3, reclamation.getReservationId());
            ps.setString(4, reclamation.getStatut());
            ps.setInt(5, reclamation.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id, String utilisateur) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

