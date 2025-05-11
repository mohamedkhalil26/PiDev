package tn.esprit.services;

import tn.esprit.entites.Role;
import tn.esprit.entites.Utilisateur;
import tn.esprit.outils.MyDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class UtilisateurService implements IService<Utilisateur> {



    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {

    }

    @Override
    public void ajouterP(Utilisateur u) throws SQLException {
        Connection con = MyDatabase.getInstance().getConnection();
        try {

            String query = "INSERT INTO utilisateur(nom, prenom, age, role, email, numero_tel, username,mot_de_passe) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setInt(3, u.getAge());
            ps.setString(4, u.getRole().toString());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getNumero_tel());
            ps.setString(7, u.getUsername());
            ps.setString(8, u.getMot_de_passe());

            ps.executeUpdate();

            // Retrieve the auto-generated UserId
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int id_utilisateur = -1;
            if (generatedKeys.next()) {
                id_utilisateur = generatedKeys.getInt(1); // Get the auto-generated UserId
                u.setId_utilisateur(id_utilisateur); // Set the UserId in the User object
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            System.out.println("User created with ID: " + id_utilisateur);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Utilisateur> returnList() throws SQLException {
        Connection con = MyDatabase.getInstance().getConnection();
        List<Utilisateur> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM utilisateur";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Convert String to Role enum
                Role role = Role.valueOf(resultSet.getString("role"));

                // Create User object
                Utilisateur u = new Utilisateur(
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getString("Nom"),
                        resultSet.getString("Prenom"),
                        resultSet.getInt("Age"),
                        role,
                        resultSet.getString("Email"),
                        resultSet.getString("numero_tel"),
                        resultSet.getString("Username"),
                        resultSet.getString("mot_de_passe")
                );
                list.add(u);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public void supprimer(Utilisateur u) {
        Connection con = MyDatabase.getInstance().getConnection();
        String query = "DELETE FROM utilisateur WHERE id_utilisateur=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, u.getId_utilisateur());
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
    public void modifier(Utilisateur u) {
        Connection con = MyDatabase.getInstance().getConnection();
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, age = ?, role = ?, email = ?, numero_tel = ?, username = ?,mot_de_passe = ? WHERE id_utilisateur = ?";

        try {
            // Create a PreparedStatement
            PreparedStatement ps = con.prepareStatement(sql);

            // Set the parameters for the PreparedStatement
            ps.setString(1, u.getNom());       // Update username
            ps.setString(2, u.getPrenom());       // Update password
            ps.setInt(3, u.getAge());          // Update email
            ps.setString(4, u.getRole().toString());           // Update name
            ps.setString(5, u.getEmail());    // Update phoneNumber
            ps.setString(6, u.getNumero_tel()); // Update status (convert enum to string)
            ps.setString(7, u.getUsername());   // Update role (convert enum to string)
            ps.setString(8, u.getMot_de_passe());
            ps.setInt(9, u.getId_utilisateur());                   // Use userId to identify the user

            // Execute the update query
            int rowsUpdated = ps.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("******************** MODIFIED **************************************");
            } else {
                System.out.println("No user found with ID: " + u.getId_utilisateur());
            }
        } catch (SQLException ex) {
            System.out.println("Error updating user: " + ex.getMessage());
        }
    }
    public Utilisateur getUtilisateurByEmailAndPass(String email, String password) {
        Utilisateur U = null;
        Connection con = MyDatabase.getInstance().getConnection();

        try {
            String req = "SELECT * FROM Utilisateur WHERE Email = ? AND mot_de_passe = ?";
            PreparedStatement psmt = con.prepareStatement(req);
            psmt.setString(1, email);
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                U = new Utilisateur();
                U.setId_utilisateur(rs.getInt("id_utilisateur"));
                U.setNom(rs.getString("nom"));
                U.setPrenom(rs.getString("prenom"));
                U.setAge(rs.getInt("age"));
                // Convert String to Role enum
                String roleStr = rs.getString("Role");
                Role role = Role.valueOf(roleStr); // Convert String to Role enum
                U.setRole(role);
                U.setEmail(rs.getString("Email"));
                U.setNumero_tel(rs.getString("numero_tel"));
                U.setUsername(rs.getString("username"));
                U.setMot_de_passe(rs.getString("mot_de_passe"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Handle case where the database value does not match any enum constant
            e.printStackTrace();
        }

        return U;
    }
    public Utilisateur getUtilisateurByEmail(String email) {
        Utilisateur U = null;
        Connection con = MyDatabase.getInstance().getConnection();
        try {
            String req = "SELECT * FROM Utilisateur WHERE Email = ?";
            PreparedStatement psmt = con.prepareStatement(req);
            psmt.setString(1, email);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                U = new Utilisateur();
                U.setId_utilisateur(rs.getInt("id_utilisateur"));
                U.setNom(rs.getString("nom"));
                U.setPrenom(rs.getString("prenom"));
                U.setAge(rs.getInt("age"));
                // Convert String to Role enum
                String roleStr = rs.getString("Role");
                Role role = Role.valueOf(roleStr); // Convert String to Role enum
                U.setRole(role);
                U.setEmail(rs.getString("Email"));
                U.setNumero_tel(rs.getString("numero_tel"));
                U.setUsername(rs.getString("username"));
                U.setMot_de_passe(rs.getString("mot_de_passe"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Handle case where the database value does not match any enum constant
            e.printStackTrace();
        }

        return U;
    }

}
