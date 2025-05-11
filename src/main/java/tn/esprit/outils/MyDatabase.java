package tn.esprit.outils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private Connection connection;
    private static MyDatabase instance;

    // Database connection dsetails
    private final String URL = "jdbc:mysql://localhost:3306/gryffindor";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the database connection.
     */
    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }
    public static MyDatabase getInstance() {
        if (instance == null) {
            synchronized (MyDatabase.class) { // Ensure thread safety in multi-threaded environments
                if (instance == null) {
                    instance = new MyDatabase();
                }
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}