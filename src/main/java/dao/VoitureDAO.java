package dao;

import model.Voiture;
import utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureDAO {



    public Voiture getVoitureById(int id) throws SQLException {
        String sql = "SELECT * FROM voiture WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String typeCarburant = null;
                try {
                    typeCarburant = rs.getString("type_carburant");
                } catch (SQLException e) {
                    // La colonne n'existe peut-être pas encore
                    typeCarburant = "essence"; // Valeur par défaut
                }

                return new Voiture(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("nb_places"),
                        rs.getString("couleur"),
                        rs.getString("type_voiture"),
                        rs.getString("categorie"),
                        typeCarburant
                );
            }
        }
        return null;
    }

    public void ajouterVoiture(Voiture v) throws SQLException {
        // Vérifier si la colonne type_carburant existe
        boolean colonneTypeCarburantExiste = verifierColonneTypeCarburant();

        String sql;
        if (colonneTypeCarburantExiste) {
            sql = "INSERT INTO voiture(utilisateur_id, nb_places, couleur, type_voiture, categorie, type_carburant) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO voiture(utilisateur_id, nb_places, couleur, type_voiture, categorie) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getUtilisateurId());
            stmt.setInt(2, v.getNbPlaces());
            stmt.setString(3, v.getCouleur());
            stmt.setString(4, v.getTypeVoiture());
            stmt.setString(5, v.getCategorie());

            if (colonneTypeCarburantExiste) {
                stmt.setString(6, v.getTypeCarburant());
            }

            stmt.executeUpdate();
        }
    }

    public List<Voiture> getToutesLesVoitures() throws SQLException {
        List<Voiture> voitures = new ArrayList<>();
        String sql = "SELECT * FROM voiture";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            boolean colonneTypeCarburantExiste = verifierColonneTypeCarburant();

            while (rs.next()) {
                String typeCarburant = "essence"; // Valeur par défaut

                if (colonneTypeCarburantExiste) {
                    try {
                        typeCarburant = rs.getString("type_carburant");
                        if (typeCarburant == null) {
                            typeCarburant = "essence";
                        }
                    } catch (SQLException e) {
                        // Ignorer l'erreur et utiliser la valeur par défaut
                    }
                }

                voitures.add(new Voiture(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("nb_places"),
                        rs.getString("couleur"),
                        rs.getString("type_voiture"),
                        rs.getString("categorie"),
                        typeCarburant
                ));
            }
        }
        return voitures;
    }

    public void modifierVoiture(Voiture v) throws SQLException {
        boolean colonneTypeCarburantExiste = verifierColonneTypeCarburant();

        String sql;
        if (colonneTypeCarburantExiste) {
            sql = "UPDATE voiture SET nb_places=?, couleur=?, type_voiture=?, categorie=?, type_carburant=? WHERE id=?";
        } else {
            sql = "UPDATE voiture SET nb_places=?, couleur=?, type_voiture=?, categorie=? WHERE id=?";
        }

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getNbPlaces());
            stmt.setString(2, v.getCouleur());
            stmt.setString(3, v.getTypeVoiture());
            stmt.setString(4, v.getCategorie());

            if (colonneTypeCarburantExiste) {
                stmt.setString(5, v.getTypeCarburant());
                stmt.setInt(6, v.getId());
            } else {
                stmt.setInt(5, v.getId());
            }

            stmt.executeUpdate();
        }
    }

    public void supprimerVoiture(int id) throws SQLException {
        String sql = "DELETE FROM voiture WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Vérifie si la colonne type_carburant existe dans la table voiture
     *
     * @return true si la colonne existe, false sinon
     */
    private boolean verifierColonneTypeCarburant() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "voiture", "type_carburant");
            return columns.next(); // Si next() retourne true, la colonne existe
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la colonne type_carburant: " + e.getMessage());
            return false; // En cas d'erreur, supposer que la colonne n'existe pas
        }
    }

    /**
     * Ajoute la colonne type_carburant à la table voiture si elle n'existe pas déjà
     *
     * @return true si la colonne a été ajoutée ou existe déjà, false en cas d'erreur
     */
    public boolean ajouterColonneTypeCarburant() {
        if (verifierColonneTypeCarburant()) {
            return true; // La colonne existe déjà
        }

        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "ALTER TABLE voiture ADD COLUMN type_carburant VARCHAR(20) DEFAULT 'essence'";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la colonne type_carburant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour le type de carburant d'une voiture
     *
     * @param voitureId L'ID de la voiture
     * @param typeCarburant Le nouveau type de carburant
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean mettreAJourTypeCarburant(int voitureId, String typeCarburant) {
        // S'assurer que la colonne existe
        if (!verifierColonneTypeCarburant()) {
            if (!ajouterColonneTypeCarburant()) {
                return false;
            }
        }

        String sql = "UPDATE voiture SET type_carburant = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeCarburant);
            stmt.setInt(2, voitureId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du type de carburant: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


