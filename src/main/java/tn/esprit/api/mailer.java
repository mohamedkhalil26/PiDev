package tn.esprit.api;

import tn.esprit.entites.Utilisateur;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class mailer {
    public static void sendEmail(Utilisateur u) {
        final String username = "gryffindor.3b11@gmail.com";
        final String password = "vaak qsru nybg eagr"; // Use App Password for Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            String signature = "\n\n-- \nGryffindor \nTéléphone : +216 11 111 111 \nEmail : gryffindor.3b11@gmail.com \nSite : www.Gryffindor.com";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Gryffindor Application"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getEmail()));
            message.setSubject("Réinitialisation du mot de passe");
            message.setText("Bonjour " + u.getPrenom() + " " + u.getNom() + ","
                    + "\n\nVotre mot de passe est : " + u.getMot_de_passe() + ". Veuillez le changer dès que possible."
                    + "\n\nCordialement," + signature);

            Transport.send(message);
            System.out.println("Email envoyé avec succès");

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail : " + e.getMessage(), e);
        }
    }
}
