package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour initialiser la base de données
 */
public class DatabaseInitializer {

    /**
     * Initialise la base de données en créant les tables nécessaires si elles n'existent pas
     */
    public static void initializeDatabase() {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("Initialisation de la base de données...");

            // Désactiver temporairement les contraintes de clé étrangère
            try {
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
                System.out.println("✅ Contraintes de clé étrangère temporairement désactivées.");
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la désactivation des contraintes de clé étrangère: " + e.getMessage());
            }

            // Création de la table utilisateur si elle n'existe pas
            try {
                String createUtilisateurTable = "CREATE TABLE IF NOT EXISTS utilisateur (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "nom VARCHAR(100) NOT NULL, " +
                        "prenom VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL UNIQUE, " +
                        "roles VARCHAR(100) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "telephone INT NOT NULL, " +
                        "adresse VARCHAR(255) NOT NULL, " +
                        "date_naissance DATE NOT NULL, " +
                        "sexe VARCHAR(10) NOT NULL, " +
                        "taille DOUBLE NULL, " +
                        "poids INT NULL, " +
                        "image VARCHAR(255) NULL, " +
                        "status INT NULL, " +
                        "diplome VARCHAR(100) NULL, " +
                        "specialite VARCHAR(100) NULL, " +
                        "face_encoding TEXT NULL" +
                        ")";

                stmt.executeUpdate(createUtilisateurTable);
                System.out.println("✅ Table 'utilisateur' vérifiée/créée avec succès.");

                // Vérifier si la table utilisateur est vide
                String checkEmptyUtilisateur = "SELECT COUNT(*) FROM utilisateur";
                var rsUtilisateur = stmt.executeQuery(checkEmptyUtilisateur);
                rsUtilisateur.next();
                int countUtilisateur = rsUtilisateur.getInt(1);

                if (countUtilisateur == 0) {
                    // Ajouter des utilisateurs d'exemple
                    String insertUtilisateurData = "INSERT INTO utilisateur (nom, prenom, email, roles, password, telephone, adresse, date_naissance, sexe) VALUES " +
                            "('Dupont', 'Jean', 'jean.dupont@example.com', 'ROLE_USER', 'password123', 123456789, '123 Rue de Paris, Paris', '1990-01-15', 'Homme'), " +
                            "('Martin', 'Sophie', 'sophie.martin@example.com', 'ROLE_USER', 'password123', 987654321, '456 Avenue des Champs, Lyon', '1985-05-20', 'Femme'), " +
                            "('Dubois', 'Pierre', 'pierre.dubois@example.com', 'ROLE_ADMIN', 'admin123', 555123456, '789 Boulevard Saint-Michel, Marseille', '1982-11-10', 'Homme')";

                    stmt.executeUpdate(insertUtilisateurData);
                    System.out.println("✅ Données d'exemple ajoutées à la table 'utilisateur'.");
                }
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la création de la table utilisateur: " + e.getMessage());
                e.printStackTrace();
            }

            // Création de la table voiture si elle n'existe pas
            try {
                String createVoitureTable = "CREATE TABLE IF NOT EXISTS voiture (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "utilisateur_id INT NOT NULL, " +
                        "nb_places INT NOT NULL, " +
                        "couleur VARCHAR(50) NOT NULL, " +
                        "type_voiture VARCHAR(50) NOT NULL, " +
                        "categorie VARCHAR(50) NOT NULL, " +
                        "type_carburant VARCHAR(20) DEFAULT 'essence'" +
                        ")";

                stmt.executeUpdate(createVoitureTable);
                System.out.println("✅ Table 'voiture' vérifiée/créée avec succès.");

                // Vérifier si la table voiture est vide
                String checkEmptyVoiture = "SELECT COUNT(*) FROM voiture";
                var rsVoiture = stmt.executeQuery(checkEmptyVoiture);
                rsVoiture.next();
                int countVoiture = rsVoiture.getInt(1);

                if (countVoiture == 0) {
                    // Ajouter des données d'exemple pour les voitures
                    String insertVoitureData = "INSERT INTO voiture (utilisateur_id, nb_places, couleur, type_voiture, categorie, type_carburant) VALUES " +
                            "(1, 5, 'Noir', 'Berline', 'Confort', 'essence'), " +
                            "(1, 4, 'Blanc', 'SUV', 'Économique', 'diesel'), " +
                            "(2, 2, 'Rouge', 'Coupé', 'Sport', 'hybride'), " +
                            "(2, 7, 'Gris', 'Monospace', 'Familiale', 'electrique')";

                    stmt.executeUpdate(insertVoitureData);
                    System.out.println("✅ Données d'exemple ajoutées à la table 'voiture'.");
                }
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la création de la table voiture: " + e.getMessage());
                e.printStackTrace();
            }

            // Création de la table trajet si elle n'existe pas
            try {
                String createTrajetTable = "CREATE TABLE IF NOT EXISTS trajet (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "ville_depart VARCHAR(100) NOT NULL, " +
                        "ville_arrivee VARCHAR(100) NOT NULL, " +
                        "date_heure_depart DATETIME NOT NULL, " +
                        "nb_places_disponibles INT NOT NULL, " +
                        "prix_par_place DECIMAL(10, 2) NOT NULL, " +
                        "conducteur_id INT NOT NULL, " +
                        "voiture_id INT NOT NULL, " +
                        "statut VARCHAR(50) NOT NULL" +
                        ")";

                stmt.executeUpdate(createTrajetTable);
                System.out.println("✅ Table 'trajet' vérifiée/créée avec succès.");

                // Vérifier si la table trajet est vide
                String checkEmptyTrajet = "SELECT COUNT(*) FROM trajet";
                var rsTrajet = stmt.executeQuery(checkEmptyTrajet);
                rsTrajet.next();
                int countTrajet = rsTrajet.getInt(1);

                if (countTrajet == 0) {
                    // Formater la date actuelle
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Ajouter des données d'exemple pour les trajets
                    String insertTrajetData = "INSERT INTO trajet (ville_depart, ville_arrivee, date_heure_depart, nb_places_disponibles, prix_par_place, conducteur_id, voiture_id, statut) VALUES " +
                            "('Paris', 'Lyon', '" + now.plusDays(1).format(formatter) + "', 3, 25.50, 1, 1, 'planifié'), " +
                            "('Lyon', 'Marseille', '" + now.plusDays(2).format(formatter) + "', 2, 15.75, 2, 2, 'planifié'), " +
                            "('Marseille', 'Nice', '" + now.plusDays(3).format(formatter) + "', 4, 10.00, 1, 1, 'planifié')";

                    stmt.executeUpdate(insertTrajetData);
                    System.out.println("✅ Données d'exemple ajoutées à la table 'trajet'.");
                }
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la création de la table trajet: " + e.getMessage());
                e.printStackTrace();
            }

            // Réactiver les contraintes de clé étrangère
            try {
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
                System.out.println("✅ Contraintes de clé étrangère réactivées.");
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la réactivation des contraintes de clé étrangère: " + e.getMessage());
            }

            System.out.println("Base de données initialisée avec succès.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
