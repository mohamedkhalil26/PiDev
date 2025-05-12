package org.example.services;

import org.example.entities.Voiture;
import org.example.utiles.dataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureService {
    private final Connection connection;

    public VoitureService() {
        connection = dataSource.getConnection();
    }

    public boolean ajouterVoiture(Voiture v) throws SQLException {
        String sql = "INSERT INTO voiture (immatriculation, marque, modele, nombre_places, id_conducteur) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getImmatriculation());
            ps.setString(2, v.getMarque());
            ps.setString(3, v.getModele());
            ps.setInt(4, v.getNombre_places());
            ps.setInt(5, v.getId_conducteur());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateVoiture(Voiture v) throws SQLException {
        String sql = "UPDATE voiture SET immatriculation = ?, marque = ?, modele = ?, nombre_places = ?, id_conducteur = ? WHERE id_voiture = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getImmatriculation());
            ps.setString(2, v.getMarque());
            ps.setString(3, v.getModele());
            ps.setInt(4, v.getNombre_places());
            ps.setInt(5, v.getId_conducteur());
            ps.setInt(6, v.getId_voiture());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteVoiture(int id_voiture) throws SQLException {
        String sql = "DELETE FROM voiture WHERE id_voiture = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id_voiture);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Voiture> getAllVoitures() throws SQLException {
        List<Voiture> list = new ArrayList<>();
        String sql = "SELECT * FROM voiture";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Voiture(
                        rs.getInt("id_voiture"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("nombre_places"),
                        rs.getInt("id_conducteur")
                ));
            }
        }
        return list;
    }
}