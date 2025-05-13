package utils;

import model.Trajet;
import service.TrajetService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Service pour l'envoi automatique d'emails
 */
public class AutoEmailService {
    
    private static Timer timer;
    private static final long CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(1); // Vérification toutes les minutes en mode test
    private static final String DEFAULT_EMAIL = "takwabouabid149@gmail.com"; // Email par défaut pour les tests
    
    /**
     * Démarre le service d'envoi automatique d'emails
     */
    public static void start() {
        if (timer != null) {
            stop(); // Arrêter le timer existant s'il y en a un
        }
        
        timer = new Timer(true); // true = daemon thread
        
        // Planifier les tâches
        timer.scheduleAtFixedRate(new ReminderTask(), 0, CHECK_INTERVAL);
        
        System.out.println("Service d'envoi automatique d'emails démarré");
    }
    
    /**
     * Arrête le service d'envoi automatique d'emails
     */
    public static void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Service d'envoi automatique d'emails arrêté");
        }
    }
    
    /**
     * Tâche qui vérifie et envoie les emails automatiquement
     */
    private static class ReminderTask extends TimerTask {
        private final TrajetService trajetService = new TrajetService();
        
        @Override
        public void run() {
            try {
                System.out.println("[" + LocalDateTime.now() + "] Vérification des trajets pour envoi d'emails...");
                
                // Récupérer tous les trajets
                List<Trajet> trajets = trajetService.getAllTrajets();
                
                for (Trajet trajet : trajets) {
                    // Vérifier si le trajet est prévu pour bientôt (dans les 24 heures)
                    LocalDateTime maintenant = LocalDateTime.now();
                    LocalDateTime dateTrajet = trajet.getDateHeureDepart();
                    
                    long heuresAvantDepart = ChronoUnit.HOURS.between(maintenant, dateTrajet);
                    
                    // Envoyer un rappel 24h avant le départ
                    if (heuresAvantDepart > 23 && heuresAvantDepart < 25) {
                        System.out.println("Envoi d'un rappel pour le trajet " + trajet.getId() + " prévu dans ~24h");
                        
                        // Rappel au conducteur
                        EmailServiceSimulator.envoyerRappelAvantDepart(trajet, DEFAULT_EMAIL, true);
                        
                        // Rappel aux passagers (simulation avec un seul passager)
                        EmailServiceSimulator.envoyerRappelAvantDepart(trajet, DEFAULT_EMAIL, false);
                    }
                    
                    // Envoyer un rappel 1h avant le départ
                    if (heuresAvantDepart > 0 && heuresAvantDepart < 2) {
                        System.out.println("Envoi d'un rappel urgent pour le trajet " + trajet.getId() + " prévu dans ~1h");
                        
                        // Rappel urgent au conducteur
                        String sujet = "RAPPEL URGENT: Votre trajet dans 1 heure";
                        String message = "<h2>Rappel urgent pour votre trajet</h2>" +
                                "<p>Votre trajet de " + trajet.getVilleDepart() + " à " + trajet.getVilleArrivee() + 
                                " est prévu dans environ 1 heure.</p>" +
                                "<p>Assurez-vous d'être prêt à partir.</p>";
                        
                        EmailServiceSimulator.sendEmail(DEFAULT_EMAIL, sujet, message);
                    }
                    
                    // Envoyer une notification après le trajet (pour évaluation)
                    if (dateTrajet.isBefore(maintenant) && ChronoUnit.HOURS.between(dateTrajet, maintenant) < 2) {
                        System.out.println("Envoi d'une demande d'évaluation pour le trajet " + trajet.getId() + " terminé récemment");
                        
                        String sujet = "Comment s'est passé votre trajet ?";
                        String message = "<h2>Votre trajet est terminé</h2>" +
                                "<p>Nous espérons que votre trajet de " + trajet.getVilleDepart() + " à " + trajet.getVilleArrivee() + 
                                " s'est bien passé.</p>" +
                                "<p>N'hésitez pas à évaluer votre expérience !</p>";
                        
                        EmailServiceSimulator.sendEmail(DEFAULT_EMAIL, sujet, message);
                    }
                }
                
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi automatique d'emails : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Envoie immédiatement un email de confirmation pour un nouveau trajet
     * 
     * @param trajet Le trajet créé
     */
    public static void envoyerConfirmationNouveauTrajet(Trajet trajet) {
        try {
            System.out.println("Envoi automatique d'une confirmation pour le nouveau trajet " + trajet.getId());
            EmailServiceSimulator.envoyerConfirmationCreationTrajet(trajet, DEFAULT_EMAIL);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la confirmation : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envoie immédiatement un email de confirmation pour une réservation
     * 
     * @param trajet Le trajet réservé
     * @param nombrePlaces Le nombre de places réservées
     */
    public static void envoyerConfirmationReservation(Trajet trajet, int nombrePlaces) {
        try {
            System.out.println("Envoi automatique d'une confirmation pour la réservation de " + nombrePlaces + 
                    " place(s) sur le trajet " + trajet.getId());
            EmailServiceSimulator.envoyerNotificationReservation(trajet, DEFAULT_EMAIL, nombrePlaces);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la confirmation : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
