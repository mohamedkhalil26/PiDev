package tn.esprit.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SentimentAnalyzer {
    private static final String API_URL = "https://api-inference.huggingface.co/models/nlptown/bert-base-multilingual-uncased-sentiment";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String API_KEY = "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";

    public static String analyze(String text) {
        if (text == null || text.trim().isEmpty()) return "neutral";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");

            ObjectNode payload = mapper.createObjectNode();
            payload.put("inputs", text);
            post.setEntity(new StringEntity(mapper.writeValueAsString(payload)));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() != 200) {
                    System.err.println("API Error Details: " + responseBody);
                    return "neutral";
                }

                JsonNode root = mapper.readTree(responseBody);
                JsonNode results = root.get(0);

                String bestLabel = "";
                double bestScore = -1;

                for (JsonNode result : results) {
                    double score = result.get("score").asDouble();
                    if (score > bestScore) {
                        bestScore = score;
                        bestLabel = result.get("label").asText(); // Ex: "5 stars"
                    }
                }

                return switch (bestLabel) {
                    case "1 star" -> "very negative";
                    case "2 stars" -> "negative";
                    case "3 stars" -> "neutral";
                    case "4 stars" -> "positive";
                    case "5 stars" -> "very positive";
                    default -> "neutral";
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "pas de connexion";
        }
    }
}
