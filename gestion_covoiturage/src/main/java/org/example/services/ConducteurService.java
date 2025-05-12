package org.example.services;

import org.example.entities.Conducteur;
import org.example.utiles.dataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConducteurService {
    private final Connection connection;

    public ConducteurService() {
        connection = dataSource.getConnection();
    }

    public boolean ajouterConducteur(Conducteur c) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM conducteur WHERE email = ?";
        try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setString(1, c.getEmail());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Conducteur avec email " + c.getEmail() + " existe déjà.");
                return false;
            }
        }
        String sql = "INSERT INTO conducteur (nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getTelephone());
            ps.setString(4, c.getEmail());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateConducteur(Conducteur c) throws SQLException {
        String sql = "UPDATE conducteur SET nom = ?, prenom = ?, telephone = ?, email = ? WHERE id_conducteur = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getTelephone());
            ps.setString(4, c.getEmail());
            ps.setInt(5, c.getId_conducteur());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteConducteur(int id_conducteur) throws SQLException {
        String sql = "DELETE FROM conducteur WHERE id_conducteur = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id_conducteur);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Conducteur> getAllConducteurs() throws SQLException {
        List<Conducteur> list = new ArrayList<>();
        String sql = "SELECT * FROM conducteur";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Conducteur(
                        rs.getInt("id_conducteur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email")
                ));
            }
        }
        return list;
    }
}