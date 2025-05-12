package org.example.services;

import org.example.entities.Covoiturage;
import org.example.utiles.dataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CovoiturageService {
    private final Connection connection;

    public CovoiturageService() {
        connection = dataSource.getConnection();
    }

    public boolean ajouterCovoiturage(Covoiturage c) throws SQLException {
        String sql = "INSERT INTO covoiturage (lieu_depart, lieu_arrivee, date_heure, id_conducteur, id_voiture, places_restantes, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getLieu_depart());
            ps.setString(2, c.getLieu_arrivee());
            ps.setString(3, c.getDate_heure());
            ps.setInt(4, c.getId_conducteur());
            ps.setInt(5, c.getId_voiture());
            ps.setInt(6, c.getPlaces_restantes());
            ps.setString(7, c.getStatut());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateCovoiturage(Covoiturage c) throws SQLException {
        String sql = "UPDATE covoiturage SET lieu_depart = ?, lieu_arrivee = ?, date_heure = ?, id_conducteur = ?, id_voiture = ?, places_restantes = ?, statut = ? WHERE id_covoiturage = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getLieu_depart());
            ps.setString(2, c.getLieu_arrivee());
            ps.setString(3, c.getDate_heure());
            ps.setInt(4, c.getId_conducteur());
            ps.setInt(5, c.getId_voiture());
            ps.setInt(6, c.getPlaces_restantes());
            ps.setString(7, c.getStatut());
            ps.setInt(8, c.getId_covoiturage());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCovoiturage(int id_covoiturage) throws SQLException {
        String sql = "DELETE FROM covoiturage WHERE id_covoiturage = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id_covoiturage);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Covoiturage> getAllCovoiturages() throws SQLException {
        List<Covoiturage> list = new ArrayList<>();
        String sql = "SELECT * FROM covoiturage";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Covoiturage(
                        rs.getInt("id_covoiturage"),
                        rs.getString("lieu_depart"),
                        rs.getString("lieu_arrivee"),
                        rs.getString("date_heure"),
                        rs.getInt("id_conducteur"),
                        rs.getInt("id_voiture"),
                        rs.getInt("places_restantes"),
                        rs.getString("statut")
                ));
            }
        }
        return list;
    }
}