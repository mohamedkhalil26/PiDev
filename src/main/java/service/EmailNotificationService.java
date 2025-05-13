package service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import model.EmpreinteCarbone;
import model.Trajet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Service pour la gestion des notifications par email, y compris les rappels automatiques.
 */
public class EmailNotificationService {
    private static final String SENDGRID_API_KEY = "VOTRE_CLE_API_SENDGRID_ICI";
    private static final String FROM_EMAIL = "covoiturage@example.com";
    private static final String FROM_NAME = "Service de Covoiturage";
    private static final String DEFAULT_EMAIL = "takwabouabid149@gmail.com";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    private static final long CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(1);
    private static Timer timer;
    private final TrajetService trajetService;
    private final EmpreinteCarboneService empreinteCarboneService;

    public EmailNotificationService() {
        this.trajetService = new TrajetService();
        this.empreinteCarboneService = new EmpreinteCarboneService();
    }

    public boolean sendEmail(String to, String subject, String htmlContent, boolean useSendGrid) {
        if (useSendGrid) {
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
                System.out.println("Email sent to " + to + " with status: " + response.getStatusCode());
                return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
            } catch (IOException e) {
                System.err.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("\n========== EMAIL SIMULATION ==========");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Content HTML:");
            System.out.println("----------------------------------------");
            System.out.println(htmlContent);
            System.out.println("----------------------------------------");
            System.out.println("Simulated email sent successfully!");
            System.out.println("=====================================\n");
            return true;
        }
    }

    public boolean envoyerConfirmationCreationTrajet(Trajet trajet, String emailConducteur, EmpreinteCarbone empreinteCarbone, boolean useSendGrid) {
        String subject = "Confirmation de création de trajet";
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Votre trajet a été créé avec succès !</h2>")
                .append("<p>Bonjour,</p>")
                .append("<p>Nous vous confirmons la création de votre trajet :</p>")
                .append("<ul>")
                .append("<li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>")
                .append("<li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>")
                .append("<li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>")
                .append("<li><strong>Places disponibles :</strong> ").append(trajet.getNbPlacesDisponibles()).append("</li>")
                .append("<li><strong>Prix par place :</strong> ").append(trajet.getPrixParPlace()).append(" €</li>")
                .append("</ul>");

        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Impact environnemental</h3>")
                    .append("<ul>")
                    .append("<li><strong>Distance :</strong> ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append("</li>")
                    .append("<li><strong>Émissions CO2 :</strong> ").append(String.format("%.2f kg", empreinteCarbone.getEmissionsTotalesKgCO2())).append("</li>");
            if (empreinteCarbone.getEconomiesPourcentage() > 0) {
                htmlContent.append("<li><strong>Économies grâce au covoiturage :</strong> ").append(String.format("%.1f%%", empreinteCarbone.getEconomiesPourcentage())).append("</li>");
            }
            htmlContent.append("<li><strong>Arbres nécessaires pour compenser :</strong> ").append(String.format("%.2f", empreinteCarbone.getArbresCompensation())).append("</li>")
                    .append("</ul>")
                    .append("<p><em>En covoiturant, vous contribuez à réduire les émissions de CO2 !</em></p>");
        }

        htmlContent.append("<p>Vous recevrez des notifications lorsque des passagers réserveront des places.</p>")
                .append("<p>Merci d'utiliser notre service de covoiturage !</p>");

        return sendEmail(emailConducteur, subject, htmlContent.toString(), useSendGrid);
    }

    public boolean envoyerNotificationReservation(Trajet trajet, String emailPassager, int nombrePlaces, EmpreinteCarbone empreinteCarbone, boolean useSendGrid) {
        String subject = "Confirmation de réservation de trajet";
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Votre réservation a été confirmée !</h2>")
                .append("<p>Bonjour,</p>")
                .append("<p>Nous vous confirmons la réservation de ").append(nombrePlaces).append(" place(s) pour le trajet suivant :</p>")
                .append("<ul>")
                .append("<li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>")
                .append("<li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>")
                .append("<li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>")
                .append("<li><strong>Prix total :</strong> ").append(trajet.getPrixParPlace() * nombrePlaces).append(" €</li>")
                .append("</ul>");

        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Votre impact environnemental</h3>")
                    .append("<ul>")
                    .append("<li><strong>Distance :</strong> ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append("</li>")
                    .append("<li><strong>Émissions CO2 évitées :</strong> ").append(String.format("%.2f kg", empreinteCarbone.getEconomiesKgCO2())).append("</li>")
                    .append("<li><strong>Équivalent en arbres :</strong> ").append(String.format("%.2f arbres pendant un an", empreinteCarbone.getEconomiesKgCO2() / 25.0)).append("</li>")
                    .append("</ul>")
                    .append("<p><em>Merci de contribuer à la réduction des émissions de CO2 en covoiturant !</em></p>");
        }

        htmlContent.append("<p>Vous recevrez un rappel 24 heures avant le départ.</p>")
                .append("<p>Merci d'utiliser notre service de covoiturage !</p>");

        return sendEmail(emailPassager, subject, htmlContent.toString(), useSendGrid);
    }

    public boolean envoyerRappelAvantDepart(Trajet trajet, String email, boolean estConducteur, EmpreinteCarbone empreinteCarbone, boolean useSendGrid) {
        String subject = "Rappel : Votre trajet demain";
        String role = estConducteur ? "conducteur" : "passager";
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h2>Rappel pour votre trajet de demain</h2>")
                .append("<p>Bonjour,</p>")
                .append("<p>Nous vous rappelons que vous êtes ").append(role).append(" pour le trajet suivant prévu demain :</p>")
                .append("<ul>")
                .append("<li><strong>Départ :</strong> ").append(trajet.getVilleDepart()).append("</li>")
                .append("<li><strong>Arrivée :</strong> ").append(trajet.getVilleArrivee()).append("</li>")
                .append("<li><strong>Date et heure :</strong> ").append(trajet.getDateHeureDepart().format(DATE_FORMATTER)).append("</li>")
                .append("</ul>");

        if (estConducteur) {
            htmlContent.append("<p>N'oubliez pas de vérifier votre véhicule avant le départ.</p>");
        } else {
            htmlContent.append("<p>N'oubliez pas d'être à l'heure au point de rendez-vous.</p>");
        }

        if (empreinteCarbone != null) {
            htmlContent.append("<h3>Impact environnemental de ce trajet</h3>")
                    .append("<p>En covoiturant sur ce trajet de ").append(String.format("%.1f km", empreinteCarbone.getDistanceKm())).append(", ");
            if (estConducteur) {
                htmlContent.append("vous permettez d'économiser environ ").append(String.format("%.2f kg de CO2", empreinteCarbone.getEconomiesKgCO2())).append(".</p>");
            } else {
                htmlContent.append("vous économisez environ ").append(String.format("%.2f kg de CO2", empreinteCarbone.getEmissionsTotalesKgCO2())).append(" par rapport à un trajet solo.</p>");
            }
        }

        htmlContent.append("<p>Nous vous souhaitons un excellent trajet !</p>");
        return sendEmail(email, subject, htmlContent.toString(), useSendGrid);
    }

    public void startAutoEmailService() {
        if (timer != null) {
            stopAutoEmailService();
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new ReminderTask(), 0, CHECK_INTERVAL);
        System.out.println("Service d'envoi automatique d'emails démarré");
    }

    public void stopAutoEmailService() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Service d'envoi automatique d'emails arrêté");
        }
    }

    private class ReminderTask extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("[" + LocalDateTime.now() + "] Vérification des trajets pour envoi d'emails...");
                List<Trajet> trajets = trajetService.getAllTrajets();
                LocalDateTime maintenant = LocalDateTime.now();

                for (Trajet trajet : trajets) {
                    LocalDateTime dateTrajet = trajet.getDateHeureDepart();
                    long heuresAvantDepart = ChronoUnit.HOURS.between(maintenant, dateTrajet);

                    if (heuresAvantDepart > 23 && heuresAvantDepart < 25) {
                        System.out.println("Envoi d'un rappel 24h avant départ pour le trajet " + trajet.getId());
                        EmpreinteCarbone empreinte = calculerEmpreinteCarbone(trajet);
                        envoyerRappelAvantDepart(trajet, DEFAULT_EMAIL, true, empreinte, false);
                        envoyerRappelAvantDepart(trajet, DEFAULT_EMAIL, false, empreinte, false);
                    }

                    if (heuresAvantDepart > 0 && heuresAvantDepart < 2) {
                        System.out.println("Envoi d'un rappel urgent 1h avant départ pour le trajet " + trajet.getId());
                        String sujet = "RAPPEL URGENT: Votre trajet dans 1 heure";
                        String message = "<h2>Rappel urgent pour votre trajet</h2>" +
                                "<p>Votre trajet de " + trajet.getVilleDepart() + " à " + trajet.getVilleArrivee() +
                                " est prévu dans environ 1 heure.</p>" +
                                "<p>Assurez-vous d'être prêt à partir.</p>";
                        sendEmail(DEFAULT_EMAIL, sujet, message, false);
                    }

                    if (dateTrajet.isBefore(maintenant) && ChronoUnit.HOURS.between(dateTrajet, maintenant) < 2) {
                        System.out.println("Envoi d'une demande d'évaluation pour le trajet " + trajet.getId());
                        String sujet = "Comment s'est passé votre trajet ?";
                        String message = "<h2>Votre trajet est terminé</h2>" +
                                "<p>Nous espérons que votre trajet de " + trajet.getVilleDepart() + " à " + trajet.getVilleArrivee() +
                                " s'est bien passé.</p>" +
                                "<p>N'hésitez pas à évaluer votre expérience !</p>";
                        sendEmail(DEFAULT_EMAIL, sujet, message, false);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi automatique d'emails : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void envoyerConfirmationNouveauTrajet(Trajet trajet, boolean useSendGrid) {
        try {
            System.out.println("Envoi d'une confirmation pour le nouveau trajet " + trajet.getId());
            EmpreinteCarbone empreinte = calculerEmpreinteCarbone(trajet);
            envoyerConfirmationCreationTrajet(trajet, DEFAULT_EMAIL, empreinte, useSendGrid);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la confirmation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean envoyerConfirmationReservation(Trajet trajet, String email, int nombrePlaces, EmpreinteCarbone empreinte, boolean useSendGrid) {
        try {
            System.out.println("Envoi d'une confirmation pour la réservation de " + nombrePlaces + " place(s) sur le trajet " + trajet.getId());
            empreinte = calculerEmpreinteCarbone(trajet);
            envoyerNotificationReservation(trajet, DEFAULT_EMAIL, nombrePlaces, empreinte, useSendGrid);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la confirmation : " + e.getMessage());
            e.printStackTrace();
        }
        return useSendGrid;
    }

    private EmpreinteCarbone calculerEmpreinteCarbone(Trajet trajet) {
        double distance = empreinteCarboneService.calculerDistance(trajet.getVilleDepart(), trajet.getVilleArrivee());
        return empreinteCarboneService.calculerEmpreinteCarbone(trajet, distance);
    }
}