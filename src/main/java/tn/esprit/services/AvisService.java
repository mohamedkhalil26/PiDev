package tn.esprit.services;

import tn.esprit.entites.Avis;
import tn.esprit.outils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements IService<Avis> {

    private Connection con = MyDatabase.getInstance().getConnection();
    @Override
    public void ajouter(Avis avis) throws SQLException {

    }

    @Override
    public void ajouterP(Avis u) throws SQLException {

        try {

            String query = "INSERT INTO Avis(id_utilisateur, id_covoiturage, note, commentaire, sentiment) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, u.getId_utilisateur());
            ps.setInt(2, u.getId_covoiturage());
            ps.setInt(3, u.getNote());
            ps.setString(4, u.getCommentaire());
            ps.setString(5, u.getSentiment());

            ps.executeUpdate();

            // Retrieve the auto-generated UserId
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int id_Avis = -1;
            if (generatedKeys.next()) {
                id_Avis = generatedKeys.getInt(1); // Get the auto-generated UserId
                u.setId_avis(id_Avis); // Set the UserId in the User object
            } else {
                throw new SQLException("Creating avis failed, no ID obtained.");
            }

            System.out.println("avis created with ID: " + id_Avis);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Avis> returnList() throws SQLException {
        List<Avis> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM Avis";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                // Create Avis object
                Avis u = new Avis(
                        resultSet.getInt("id_Avis"),
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getInt("id_covoiturage"),
                        resultSet.getInt("note"),
                        resultSet.getString("commentaire"),
                        resultSet.getString("sentiment")
                );
                list.add(u);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public void supprimer(Avis a) {
        String query = "DELETE FROM avis WHERE id_avis=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, a.getId_avis());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("No user found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }


    @Override
    public void modifier(Avis avis) {
        String sql = "UPDATE avis SET id_utilisateur = ?, id_covoiturage = ?, note = ?, commentaire = ?, sentiment = ? WHERE id_avis = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, avis.getId_utilisateur());
            ps.setInt(2, avis.getId_covoiturage());
            ps.setInt(3, avis.getNote());
            ps.setString(4, avis.getCommentaire());
            ps.setString(5, avis.getSentiment()); // Make sure sentiment is not null
            ps.setInt(6, avis.getId_avis());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Feedback successfully updated.");
            } else {
                System.out.println("No feedback found with ID: " + avis.getId_avis());
            }
        } catch (SQLException ex) {
            System.err.println("Error updating feedback: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public List<Integer> getAllUserIds() throws SQLException {
        List<Integer> userIds = new ArrayList<>();
        String query = "SELECT id_utilisateur FROM utilisateur";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                userIds.add(rs.getInt("id_utilisateur"));
            }
        }
        return userIds;
    }

    // New method to get all carpooling IDs
    public List<Integer> getAllCarpoolingIds() throws SQLException {
        List<Integer> carpoolingIds = new ArrayList<>();
        String query = "SELECT id_covoiturage FROM covoiturage";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                carpoolingIds.add(rs.getInt("id_covoiturage"));
            }
        }
        return carpoolingIds;
    }


}
