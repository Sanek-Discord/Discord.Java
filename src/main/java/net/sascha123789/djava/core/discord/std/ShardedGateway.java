package net.sascha123789.djava.core.discord.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

public class ShardedGateway implements IGateway{
    private String url;
    private int recommendedShards;
    private static HttpClient httpClient = HttpClient.newBuilder().build();
    private static ObjectMapper objectMapper = new ObjectMapper();

    private ShardedGateway(String url, int recommendedShards) {
        this.url = url;
        this.recommendedShards = recommendedShards;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public int getRecommendedShards() {
        return recommendedShards;
    }

    public static ShardedGateway getBotGateway(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI("https://discord.com/api/v10/gateway/bot"))
                    .header("Authorization", "Bot " + token)
                    .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "gateway/bot" + ", 10)")
                    .build();

            AtomicReference<String> json = new AtomicReference<>();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(msg -> {
                        System.out.println("[DiscordBotGateway]: Successfully received gateway url!");
                        json.set(msg);
                    })
                    .join();

            ObjectNode jsonNode = objectMapper.readValue(json.get(), ObjectNode.class);
            JsonNode objNode = null;

            if(jsonNode.has("message")) {
                throw new RuntimeException("Error!Message: " + jsonNode.get("message").asText() + (jsonNode.get("message").asText().equals("401: Unauthorized") ? "(Invalid token)" : ""));
            }

            if(jsonNode.has("url")) {
                objNode = jsonNode.get("url");
            }

            String url = objNode.asText();
            int recommendedShards = jsonNode.get("shards").asInt();

            return new ShardedGateway(url, recommendedShards);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
