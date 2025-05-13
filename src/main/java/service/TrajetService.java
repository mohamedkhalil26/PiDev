package service;

import dao.TrajetDAO;
import model.EmpreinteCarbone;
import model.Trajet;
import utils.EmailServiceSimulator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les opérations liées aux trajets
 */
public class TrajetService {
    private final TrajetDAO trajetDAO;
    private final EmpreinteCarboneService empreinteCarboneService;

    public TrajetService() {
        this.trajetDAO = new TrajetDAO();
        this.empreinteCarboneService = new EmpreinteCarboneService();
    }

    /**
     * Récupère tous les trajets
     * @return Liste de tous les trajets
     */
    public List<Trajet> getAllTrajets() {
        try {
            return trajetDAO.getTousLesTrajets();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Récupère un trajet par son ID
     * @param id L'ID du trajet à récupérer
     * @return Le trajet correspondant à l'ID, ou null si non trouvé
     */
    public Trajet getTrajetById(int id) {
        try {
            return trajetDAO.getTrajetParId(id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du trajet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calcule l'empreinte carbone d'un trajet
     *
     * @param trajetId L'ID du trajet
     * @return L'empreinte carbone du trajet, ou null en cas d'erreur
     */
    public EmpreinteCarbone calculerEmpreinteCarbone(int trajetId) {
        Trajet trajet = getTrajetById(trajetId);
        if (trajet == null) {
            return null;
        }

        // Calculer la distance entre les villes
        double distance = empreinteCarboneService.calculerDistance(trajet.getVilleDepart(), trajet.getVilleArrivee());

        // Calculer l'empreinte carbone
        return empreinteCarboneService.calculerEmpreinteCarbone(trajet, distance);
    }

    /**
     * Ajoute un nouveau trajet
     * @param trajet Le trajet à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouterTrajet(Trajet trajet) {
        try {
            trajetDAO.ajouterTrajet(trajet);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un trajet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifie un trajet existant
     * @param trajet Le trajet à modifier
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierTrajet(Trajet trajet) {
        try {
            trajetDAO.modifierTrajet(trajet);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification d'un trajet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un trajet par son ID
     * @param id L'ID du trajet à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerTrajet(int id) {
        try {
            trajetDAO.supprimerTrajet(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'un trajet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche des trajets selon différents critères
     * @param villeDepart La ville de départ (peut être null)
     * @param villeArrivee La ville d'arrivée (peut être null)
     * @param dateMin La date minimum de départ (peut être null)
     * @param dateMax La date maximum de départ (peut être null)
     * @return Liste des trajets correspondant aux critères
     */
    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee, LocalDateTime dateMin, LocalDateTime dateMax) {
        try {
            return trajetDAO.rechercherTrajets(villeDepart, villeArrivee, dateMin, dateMax);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de trajets: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Récupère tous les trajets d'un conducteur
     * @param conducteurId L'ID du conducteur
     * @return Liste des trajets du conducteur
     */
    public List<Trajet> getTrajetsByConducteur(int conducteurId) {
        try {
            return trajetDAO.getTrajetsByConducteur(conducteurId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets du conducteur: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Récupère tous les trajets utilisant une voiture spécifique
     * @param voitureId L'ID de la voiture
     * @return Liste des trajets utilisant cette voiture
     */
    public List<Trajet> getTrajetsByVoiture(int voitureId) {
        try {
            return trajetDAO.getTrajetsByVoiture(voitureId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets de la voiture: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Réserve des places pour un passager sur un trajet
     *
     * @param trajetId L'ID du trajet
     * @param emailPassager L'email du passager
     * @param nombrePlaces Le nombre de places à réserver
     * @return true si la réservation a réussi, false sinon
     */
    public boolean reserverPlaces(int trajetId, String emailPassager, int nombrePlaces) {
        try {
            // Récupérer le trajet
            Trajet trajet = trajetDAO.getTrajetParId(trajetId);
            if (trajet == null) {
                System.err.println("Trajet non trouvé: ID " + trajetId);
                return false;
            }

            // Vérifier s'il y a assez de places disponibles
            if (trajet.getNbPlacesDisponibles() < nombrePlaces) {
                System.err.println("Pas assez de places disponibles: " + trajet.getNbPlacesDisponibles() + " < " + nombrePlaces);
                return false;
            }

            // Mettre à jour le nombre de places disponibles
            int nouvellePlacesDisponibles = trajet.getNbPlacesDisponibles() - nombrePlaces;
            trajet.setNbPlacesDisponibles(nouvellePlacesDisponibles);

            // Mettre à jour le trajet dans la base de données
            trajetDAO.modifierTrajet(trajet);

            // Calculer l'empreinte carbone pour l'inclure dans l'email
            EmpreinteCarbone empreinteCarbone = calculerEmpreinteCarbone(trajetId);

            // Envoyer un email de confirmation au passager si un email est fourni
            if (emailPassager != null && !emailPassager.isEmpty()) {
                // Utiliser le service d'email pour envoyer une confirmation
                EmailServiceSimulator.envoyerNotificationReservation(trajet, emailPassager, nombrePlaces);
                System.out.println("Email de confirmation envoyé à " + emailPassager + " pour " + nombrePlaces + " place(s)");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la réservation de places: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envoie des rappels pour les trajets prévus le lendemain
     *
     * @return Le nombre de rappels envoyés
     */
    public int envoyerRappelsTrajetsLendemain() {
        int nombreRappelsEnvoyes = 0;

        try {
            // Récupérer tous les trajets
            List<Trajet> trajets = getAllTrajets();

            // Date et heure actuelles
            LocalDateTime maintenant = LocalDateTime.now();

            // Définir la plage horaire pour le lendemain (entre 24h et 48h à partir de maintenant)
            LocalDateTime debutLendemain = maintenant.plusHours(24);
            LocalDateTime finLendemain = maintenant.plusHours(48);

            System.out.println("Recherche des trajets prévus entre " + debutLendemain + " et " + finLendemain);

            // Parcourir tous les trajets
            for (Trajet trajet : trajets) {
                LocalDateTime dateHeureDepart = trajet.getDateHeureDepart();

                // Vérifier si le trajet est prévu pour le lendemain
                if (dateHeureDepart.isAfter(debutLendemain) && dateHeureDepart.isBefore(finLendemain)) {
                    System.out.println("Trajet " + trajet.getId() + " prévu pour le lendemain: " + dateHeureDepart);

                    // Vérifier si le trajet est planifié
                    if ("planifié".equals(trajet.getStatut())) {
                        // Calculer l'empreinte carbone pour l'inclure dans l'email
                        EmpreinteCarbone empreinteCarbone = calculerEmpreinteCarbone(trajet.getId());

                        // Envoyer un rappel au conducteur
                        // Dans une application réelle, on récupérerait l'email du conducteur depuis la base de données
                        String emailConducteur = "takwabouabid149@gmail.com"; // Email par défaut pour les tests
                        boolean rappelConducteur = EmailServiceSimulator.envoyerRappelAvantDepart(trajet, emailConducteur, true);

                        if (rappelConducteur) {
                            nombreRappelsEnvoyes++;
                            System.out.println("Rappel envoyé au conducteur pour le trajet " + trajet.getId());
                        }

                        // Envoyer des rappels aux passagers
                        // Dans une application réelle, on récupérerait la liste des passagers depuis la base de données
                        // Pour cette simulation, on envoie un seul rappel à un passager fictif
                        String emailPassager = "takwabouabid149@gmail.com"; // Email par défaut pour les tests
                        boolean rappelPassager = EmailServiceSimulator.envoyerRappelAvantDepart(trajet, emailPassager, false);

                        if (rappelPassager) {
                            nombreRappelsEnvoyes++;
                            System.out.println("Rappel envoyé au passager pour le trajet " + trajet.getId());
                        }
                    } else {
                        System.out.println("Le trajet " + trajet.getId() + " n'est pas planifié, statut: " + trajet.getStatut());
                    }
                }
            }

            System.out.println("Nombre total de rappels envoyés: " + nombreRappelsEnvoyes);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des rappels: " + e.getMessage());
            e.printStackTrace();
        }

        return nombreRappelsEnvoyes;
    }
}
