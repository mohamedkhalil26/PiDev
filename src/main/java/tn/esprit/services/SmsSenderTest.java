package tn.esprit.services;

import com.twilio.exception.ApiException;

public class SmsSenderTest {
    public static void main(String[] args) {
        try {
            // Remplacer par un numéro vérifié (ex. votre numéro, comme +21612345678)
            SmsSender.envoyerSms("+19785416403", "Test SMS depuis Twilio");
            System.out.println("✅ SMS envoyé avec succès");
        } catch (ApiException e) {
            System.err.println("❌ Erreur Twilio - Code: " + e.getCode() + ", Message: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}