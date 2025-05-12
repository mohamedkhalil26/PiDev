package org.example;

import org.example.entities.Conducteur;
import org.example.entities.Voiture;
import org.example.entities.Covoiturage;
import org.example.services.ConducteurService;
import org.example.services.VoitureService;
import org.example.services.CovoiturageService;
import org.example.utiles.dataSource;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConducteurService conducteurService = new ConducteurService();
        VoitureService voitureService = new VoitureService();
        CovoiturageService covoiturageService = new CovoiturageService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Menu de Gestion de Covoiturage ===");
            System.out.println("1. Ajouter un Conducteur");
            System.out.println("2. Ajouter une Voiture");
            System.out.println("3. Ajouter un Covoiturage");
            System.out.println("4. Afficher tous les Conducteurs");
            System.out.println("5. Afficher toutes les Voitures");
            System.out.println("6. Afficher tous les Covoiturages");
            System.out.println("7. Quitter");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Nom : ");
                        String nom = scanner.nextLine();
                        System.out.print("Prénom : ");
                        String prenom = scanner.nextLine();
                        System.out.print("Téléphone : ");
                        String telephone = scanner.nextLine();
                        System.out.print("Email : ");
                        String email = scanner.nextLine();
                        Conducteur conducteur = new Conducteur(0, nom, prenom, telephone, email);
                        if (conducteurService.ajouterConducteur(conducteur)) {
                            System.out.println("Conducteur ajouté avec succès.");
                        }
                        break;

                    case 2:
                        System.out.print("Immatriculation : ");
                        String immatriculation = scanner.nextLine();
                        System.out.print("Marque : ");
                        String marque = scanner.nextLine();
                        System.out.print("Modèle : ");
                        String modele = scanner.nextLine();
                        System.out.print("Nombre de places : ");
                        int nombrePlaces = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("ID du Conducteur : ");
                        int idConducteur = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        Voiture voiture = new Voiture(0, immatriculation, marque, modele, nombrePlaces, idConducteur);
                        if (voitureService.ajouterVoiture(voiture)) {
                            System.out.println("Voiture ajoutée avec succès.");
                        }
                        break;

                    case 3:
                        System.out.print("Lieu de départ : ");
                        String lieuDepart = scanner.nextLine();
                        System.out.print("Lieu d'arrivée : ");
                        String lieuArrivee = scanner.nextLine();
                        System.out.print("Date et heure (AAAA-MM-JJ HH:MM:SS) : ");
                        String dateHeure = scanner.nextLine();
                        System.out.print("ID du Conducteur : ");
                        int idConducteurCovoit = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("ID de la Voiture : ");
                        int idVoiture = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Places restantes : ");
                        int placesRestantes = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Statut : ");
                        String statut = scanner.nextLine();
                        Covoiturage covoiturage = new Covoiturage(0, lieuDepart, lieuArrivee, dateHeure, idConducteurCovoit, idVoiture, placesRestantes, statut);
                        if (covoiturageService.ajouterCovoiturage(covoiturage)) {
                            System.out.println("Covoiturage ajouté avec succès.");
                        }
                        break;

                    case 4:
                        System.out.println("Conducteurs : " + conducteurService.getAllConducteurs());
                        break;

                    case 5:
                        System.out.println("Voitures : " + voitureService.getAllVoitures());
                        break;

                    case 6:
                        System.out.println("Covoiturages : " + covoiturageService.getAllCovoiturages());
                        break;

                    case 7:
                        System.out.println("Fermeture de l'application.");
                        dataSource.closeConnection();
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Choix invalide. Essayez encore.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}