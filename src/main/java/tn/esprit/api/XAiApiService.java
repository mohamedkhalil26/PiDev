package tn.esprit.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class XAiApiService {
    private static final String API_KEY = "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"; // Replace with your key
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions"; // Adjust if needed

    public static String sendMessage(String prompt) {
        try {
            // Préparation des messages
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "You are an AI assistant."));
            messages.put(new JSONObject().put("role", "user").put("content", prompt));

            // Corps de la requête
            JSONObject json = new JSONObject();
            json.put("model", "deepseek/deepseek-r1-distill-llama-70b:free");
            json.put("messages", messages);

            // Requête HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Affichage des infos de débogage
            System.out.println("HTTP Status: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (response.statusCode() != 200) {
                return "Erreur HTTP " + response.statusCode() + ": " + response.body();
            }

            JSONObject responseObject = new JSONObject(response.body());
            JSONArray choices = responseObject.getJSONArray("choices");
            return choices.getJSONObject(0).getJSONObject("message").getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur d'exécution : " + e.getMessage();
        }
    }


    public static void main(String[] args) {
        String response = sendMessage("Hello, how are you?");
        System.out.println("AI Response: " + response);
    }
}
