package utils;

import model.Trajet;

import java.time.format.DateTimeFormatter;

/**
 * Service pour simuler l'envoi d'emails (sans dépendance externe)
 * Utilisez cette classe si vous rencontrez des problèmes avec SendGrid
 */
public class EmailServiceSimulator {
    
    // Formateur de date pour affichage dans les emails
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    
    /**
     * Simule l'envoi d'un email (affiche le contenu dans la console)
     * 
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param htmlContent Contenu HTML de l'email
     * @return true (toujours réussi car c'est une simulation)
     */
    public static boolean sendEmail(String to, String subject, String htmlContent) {
        System.out.println("\n========== SIMULATION D'ENVOI D'EMAIL ==========");
        System.out.println("À: " + to);
        System.out.println("Sujet: " + subject);
        System.out.println("Contenu HTML:");
        System.out.println("----------------------------------------");
        System.out.println(htmlContent);
        System.out.println("----------------------------------------");
        System.out.println("Email simulé envoyé avec succès!");
        System.out.println("==============================================\n");
        
        return true;
    }
    
    /**
     * Simule l'envoi d'un email de confirmation de création de trajet
     * 
     * @param trajet Le trajet créé
     * @param emailConducteur L'email du conducteur
     * @return true (toujours réussi car c'est une simulation)
     */
    public static boolean envoyerConfirmationCreationTrajet(Trajet trajet, String emailConducteur) {
        String subject = "Confirmation de création de trajet";
        
        String htmlContent = 
                "<h2>Votre trajet a été créé avec succès !</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous vous confirmons la création de votre trajet :</p>" +
                "<ul>" +
                "  <li><strong>Départ :</strong> " + trajet.getVilleDepart() + "</li>" +
                "  <li><strong>Arrivée :</strong> " + trajet.getVilleArrivee() + "</li>" +
                "  <li><strong>Date et heure :</strong> " + trajet.getDateHeureDepart().format(DATE_FORMATTER) + "</li>" +
                "  <li><strong>Places disponibles :</strong> " + trajet.getNbPlacesDisponibles() + "</li>" +
                "  <li><strong>Prix par place :</strong> " + trajet.getPrixParPlace() + " €</li>" +
                "</ul>" +
                "<p>Vous recevrez des notifications lorsque des passagers réserveront des places.</p>" +
                "<p>Merci d'utiliser notre service de covoiturage !</p>";
        
        return sendEmail(emailConducteur, subject, htmlContent);
    }
    
    /**
     * Simule l'envoi d'une notification de réservation au passager
     * 
     * @param trajet Le trajet réservé
     * @param emailPassager L'email du passager
     * @param nombrePlaces Le nombre de places réservées
     * @return true (toujours réussi car c'est une simulation)
     */
    public static boolean envoyerNotificationReservation(Trajet trajet, String emailPassager, int nombrePlaces) {
        String subject = "Confirmation de réservation de trajet";
        
        String htmlContent = 
                "<h2>Votre réservation a été confirmée !</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous vous confirmons la réservation de " + nombrePlaces + " place(s) pour le trajet suivant :</p>" +
                "<ul>" +
                "  <li><strong>Départ :</strong> " + trajet.getVilleDepart() + "</li>" +
                "  <li><strong>Arrivée :</strong> " + trajet.getVilleArrivee() + "</li>" +
                "  <li><strong>Date et heure :</strong> " + trajet.getDateHeureDepart().format(DATE_FORMATTER) + "</li>" +
                "  <li><strong>Prix total :</strong> " + (trajet.getPrixParPlace() * nombrePlaces) + " €</li>" +
                "</ul>" +
                "<p>Vous recevrez un rappel 24 heures avant le départ.</p>" +
                "<p>Merci d'utiliser notre service de covoiturage !</p>";
        
        return sendEmail(emailPassager, subject, htmlContent);
    }
    
    /**
     * Simule l'envoi d'un rappel avant le départ d'un trajet
     * 
     * @param trajet Le trajet concerné
     * @param email L'email du destinataire (conducteur ou passager)
     * @param estConducteur true si le destinataire est le conducteur, false sinon
     * @return true (toujours réussi car c'est une simulation)
     */
    public static boolean envoyerRappelAvantDepart(Trajet trajet, String email, boolean estConducteur) {
        String subject = "Rappel : Votre trajet demain";
        
        String role = estConducteur ? "conducteur" : "passager";
        
        String htmlContent = 
                "<h2>Rappel pour votre trajet de demain</h2>" +
                "<p>Bonjour,</p>" +
                "<p>Nous vous rappelons que vous êtes " + role + " pour le trajet suivant prévu demain :</p>" +
                "<ul>" +
                "  <li><strong>Départ :</strong> " + trajet.getVilleDepart() + "</li>" +
                "  <li><strong>Arrivée :</strong> " + trajet.getVilleArrivee() + "</li>" +
                "  <li><strong>Date et heure :</strong> " + trajet.getDateHeureDepart().format(DATE_FORMATTER) + "</li>" +
                "</ul>";
        
        if (estConducteur) {
            htmlContent += "<p>N'oubliez pas de vérifier votre véhicule avant le départ.</p>";
        } else {
            htmlContent += "<p>N'oubliez pas d'être à l'heure au point de rendez-vous.</p>";
        }
        
        htmlContent += "<p>Nous vous souhaitons un excellent trajet !</p>";
        
        return sendEmail(email, subject, htmlContent);
    }
}
