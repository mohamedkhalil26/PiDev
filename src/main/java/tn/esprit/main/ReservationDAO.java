package tn.esprit.main;

import tn.esprit.entites.Reservation;
import tn.esprit.outils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // ➕ Créer une réservation
    public void createReservation(Reservation r) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO reservations (id_covoiturage, id_passager, prix, nb_places_reservees) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, r.getIdCovoiturage());
        ps.setInt(2, r.getIdPassager());
        ps.setDouble(3, r.getPrix());
        ps.setInt(4, r.getNbPlacesReservees());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            r.setIdReservation(rs.getInt(1));
            System.out.println("✅ Nouvelle réservation insérée avec ID = " + r.getIdReservation());
        }
    }

    // 📋 Lire toutes les réservations
    public List<Reservation> getAllReservations() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        System.out.println("📥 Exécution de : " + sql);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            Reservation r = new Reservation(
                    rs.getInt("id_covoiturage"),
                    rs.getInt("id_passager"),
                    rs.getDouble("prix"),
                    rs.getInt("nb_places_reservees")
            );
            r.setIdReservation(id);
            System.out.println("🔄 Réservation récupérée : ID = " + id);
            list.add(r);
        }

        return list;
    }

    // ✏️ Modifier une réservation
    public void updateReservation(Reservation r) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE reservations SET prix=?, nb_places_reservees=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, r.getPrix());
        ps.setInt(2, r.getNbPlacesReservees());
        ps.setInt(3, r.getIdReservation());
        int rows = ps.executeUpdate();

        System.out.println("✏️ Réservation modifiée (ID=" + r.getIdReservation() + "), lignes affectées : " + rows);
    }

    // ❌ Supprimer une réservation
    public void deleteReservation(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide pour suppression : " + id);
        }

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM reservations WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();

        System.out.println("🗑️ Réservation supprimée (ID=" + id + "), lignes affectées : " + rowsAffected);
    }
}

