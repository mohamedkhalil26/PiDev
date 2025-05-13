package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/covoiturage?createDatabaseIfNotExist=true";
    private static final String USER = "root"; // remplace si ton identifiant est différent
    private static final String PASSWORD = ""; // remplace si tu as un mot de passe MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
