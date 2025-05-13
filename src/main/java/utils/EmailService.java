package utils;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import model.Trajet;
import model.EmpreinteCarbone;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Service pour l'envoi d'emails via SendGrid
 */
public class EmailService {

    // Remplacez par votre clé API SendGrid
    private static final String SENDGRID_API_KEY = "VOTRE_CLE_API_SENDGRID_ICI";

    // Adresse email de l'expéditeur
    private static final String FROM_EMAIL = "covoiturage@example.com";
    private static final String FROM_NAME = "Service de Covoiturage";

    // Formateur de date pour affichage dans les emails
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    /**
     * Envoie un email via SendGrid
     *
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param htmlContent Contenu HTML de l'email
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public static boolean sendEmail(String to, String subject, String htmlContent) {
        try {
            Email from = new Email(FROM_EMAIL, FROM_NAME);
            Email toEmail = new Email(to);
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setSubject(subject);

            Personalization personalization = new Personalization();
            personalization.addTo(toEmail);
            mail.addPersonalization(personalization);
            mail.addContent(content);

            SendGrid sg = new SendGrid(SENDGRID_API_KEY);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Email envoyé à " + to + " avec statut: " + response.getStatusCode());
            return response.getStatusCode() >= 200 && response.getStatusCode() < 300;

        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envoie un email de confirmation de création de trajet
     *
     * @param trajet Le trajet créé
     * @param emailConducteur L'email du conducteur
     * @param empreinteCarbone L'empreinte carbone du trajet (peut être null)
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public static boolean envoyerConfirmationCreationTrajet(Trajet trajet, String emailConducteur, EmpreinteCarbone empreinteCarbone) {
        String subject = "Confirmation de création de trajet";

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Votre trajet a été créé avec succès !</h2>");
        htmlContent.append("<p>Bonjour,</p>");
        htmlContent.append("<p>Nous vous confirmons la création de votre trajet :</p>");
        htmlContent.append("<ul>");
        htmlContent.append("  <li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>");
        htmlContent.append("  <li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>");
        htmlContent.append("  <li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>");
        htmlContent.append("  <li><strong>Places disponibles :</strong> ").append(trajet.getNbPlacesDisponibles()).append("</li>");
        htmlContent.append("  <li><strong>Prix par place :</strong> ").append(trajet.getPrixParPlace()).append(" €</li>");
        htmlContent.append("</ul>");

        // Ajouter les informations d'empreinte carbone si disponibles
        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Impact environnemental</h3>");
            htmlContent.append("<ul>");
            htmlContent.append("  <li><strong>Distance :</strong> ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append("</li>");
            htmlContent.append("  <li><strong>Émissions CO2 :</strong> ").append(String.format("%.2f kg", empreinteCarbone.getEmissionsTotalesKgCO2())).append("</li>");

            if (empreinteCarbone.getEconomiesPourcentage() > 0) {
                htmlContent.append("  <li><strong>Économies grâce au covoiturage :</strong> ").append(String.format("%.1f%%", empreinteCarbone.getEconomiesPourcentage())).append("</li>");
            }

            htmlContent.append("  <li><strong>Arbres nécessaires pour compenser :</strong> ").append(String.format("%.2f", empreinteCarbone.getArbresCompensation())).append("</li>");
            htmlContent.append("</ul>");
            htmlContent.append("<p><em>En covoiturant, vous contribuez à réduire les émissions de CO2 !</em></p>");
        }

        htmlContent.append("<p>Vous recevrez des notifications lorsque des passagers réserveront des places.</p>");
        htmlContent.append("<p>Merci d'utiliser notre service de covoiturage !</p>");

        return sendEmail(emailConducteur, subject, htmlContent.toString());
    }

    /**
     * Surcharge de la méthode pour compatibilité avec le code existant
     */
    public static boolean envoyerConfirmationCreationTrajet(Trajet trajet, String emailConducteur) {
        return envoyerConfirmationCreationTrajet(trajet, emailConducteur, null);
    }

    /**
     * Envoie une notification de réservation au passager
     *
     * @param trajet Le trajet réservé
     * @param emailPassager L'email du passager
     * @param nombrePlaces Le nombre de places réservées
     * @param empreinteCarbone L'empreinte carbone du trajet (peut être null)
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public static boolean envoyerNotificationReservation(Trajet trajet, String emailPassager, int nombrePlaces, EmpreinteCarbone empreinteCarbone) {
        String subject = "Confirmation de réservation de trajet";

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Votre réservation a été confirmée !</h2>");
        htmlContent.append("<p>Bonjour,</p>");
        htmlContent.append("<p>Nous vous confirmons la réservation de ").append(nombrePlaces).append(" place(s) pour le trajet suivant :</p>");
        htmlContent.append("<ul>");
        htmlContent.append("  <li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>");
        htmlContent.append("  <li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>");
        htmlContent.append("  <li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>");
        htmlContent.append("  <li><strong>Prix total :</strong> ").append(trajet.getPrixParPlace() * nombrePlaces).append(" €</li>");
        htmlContent.append("</ul>");

        // Ajouter les informations d'empreinte carbone si disponibles
        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Votre impact environnemental</h3>");
            htmlContent.append("<ul>");
            htmlContent.append("  <li><strong>Distance :</strong> ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append("</li>");
            htmlContent.append("  <li><strong>Émissions CO2 évitées :</strong> ").append(String.format("%.2f kg", empreinteCarbone.getEconomiesKgCO2())).append("</li>");
            htmlContent.append("  <li><strong>Équivalent en arbres :</strong> ").append(String.format("%.2f arbres pendant un an", empreinteCarbone.getEconomiesKgCO2() / 25.0)).append("</li>");
            htmlContent.append("</ul>");
            htmlContent.append("<p><em>Merci de contribuer à la réduction des émissions de CO2 en covoiturant !</em></p>");
        }

        htmlContent.append("<p>Vous recevrez un rappel 24 heures avant le départ.</p>");
        htmlContent.append("<p>Merci d'utiliser notre service de covoiturage !</p>");

        return sendEmail(emailPassager, subject, htmlContent.toString());
    }

    /**
     * Surcharge de la méthode pour compatibilité avec le code existant
     */
    public static boolean envoyerNotificationReservation(Trajet trajet, String emailPassager, int nombrePlaces) {
        return envoyerNotificationReservation(trajet, emailPassager, nombrePlaces, null);
    }

    /**
     * Envoie un rappel avant le départ d'un trajet
     *
     * @param trajet Le trajet concerné
     * @param email L'email du destinataire (conducteur ou passager)
     * @param estConducteur true si le destinataire est le conducteur, false sinon
     * @param empreinteCarbone L'empreinte carbone du trajet (peut être null)
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public static boolean envoyerRappelAvantDepart(Trajet trajet, String email, boolean estConducteur, EmpreinteCarbone empreinteCarbone) {
        String subject = "Rappel : Votre trajet demain";

        String role = estConducteur ? "conducteur" : "passager";

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Rappel pour votre trajet de demain</h2>");
        htmlContent.append("<p>Bonjour,</p>");
        htmlContent.append("<p>Nous vous rappelons que vous êtes ").append(role).append(" pour le trajet suivant prévu demain :</p>");
        htmlContent.append("<ul>");
        htmlContent.append("  <li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>");
        htmlContent.append("  <li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>");
        htmlContent.append("  <li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>");
        htmlContent.append("</ul>");

        if (estConducteur) {
            htmlContent.append("<p>N'oubliez pas de vérifier votre véhicule avant le départ.</p>");
        } else {
            htmlContent.append("<p>N'oubliez pas d'être à l'heure au point de rendez-vous.</p>");
        }

        // Ajouter les informations d'empreinte carbone si disponibles
        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Impact environnemental de ce trajet</h3>");
            htmlContent.append("<p>En covoiturant sur ce trajet de ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append(", ");

            if (estConducteur) {
                htmlContent.append("vous permettez d'économiser environ ").append(String.format("%.2f kg de CO2", empreinteCarbone.getEconomiesKgCO2())).append(".</p>");
            } else {
                htmlContent.append("vous économisez environ ").append(String.format("%.2f kg de CO2", empreinteCarbone.getEmissionsTotalesKgCO2())).append(" par rapport à un trajet solo.</p>");
            }
        }

        htmlContent.append("<p>Nous vous souhaitons un excellent trajet !</p>");

        return sendEmail(email, subject, htmlContent.toString());
    }

    /**
     * Surcharge de la méthode pour compatibilité avec le code existant
     */
    public static boolean envoyerRappelAvantDepart(Trajet trajet, String email, boolean estConducteur) {
        return envoyerRappelAvantDepart(trajet, email, estConducteur, null);
    }
}
