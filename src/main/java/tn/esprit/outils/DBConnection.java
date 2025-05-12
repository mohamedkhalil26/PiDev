package tn.esprit.outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/covoiturage_app"; // ⚠️ modifie avec ton schéma
    private static final String USER = "root"; // ⚠️ modifie si nécessaire
    private static final String PASSWORD = ""; // ⚠️ modifie si nécessaire

    private DBConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion initiale établie.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la connexion initiale : " + e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Connexion fermée, tentative de reconnexion...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Reconnexion réussie.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la reconnexion : " + e.getMessage());
        }
        return connection;
    }
}

