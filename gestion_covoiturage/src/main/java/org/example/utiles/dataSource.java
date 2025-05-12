package org.example.utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dataSource {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_covoiturage";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    private dataSource() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion établie !");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion !");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }
}