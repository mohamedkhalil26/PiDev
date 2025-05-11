package tn.esprit.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;
import io.github.cdimascio.dotenv.Dotenv;

public class SmsSender {
    private static final Dotenv dotenv = Dotenv.load();
    public static final String ACCOUNT_SID = dotenv.get("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = dotenv.get("TWILIO_AUTH_TOKEN");
    public static final String TWILIO_NUMBER = dotenv.get("TWILIO_PHONE_NUMBER");

    static {
        if (ACCOUNT_SID == null || AUTH_TOKEN == null || TWILIO_NUMBER == null) {
            throw new IllegalStateException("Les variables d'environnement TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN et TWILIO_PHONE_NUMBER doivent être définies dans le fichier .env.");
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void envoyerSms(String numeroDestinataire, String contenu) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(numeroDestinataire),
                    new PhoneNumber(TWILIO_NUMBER),
                    contenu
            ).create();
            System.out.println("✅ SMS envoyé avec SID : " + message.getSid());
        } catch (ApiException e) {
            System.err.println("❌ Erreur Twilio - Code: " + e.getCode() + ", Message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Erreur inattendue lors de l'envoi du SMS : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}