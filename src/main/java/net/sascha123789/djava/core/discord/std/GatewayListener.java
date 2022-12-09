package net.sascha123789.djava.core.discord.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.core.discord.events.objects.ReadyAction;
import net.sascha123789.djava.core.discord.models.DiscordAccountManager;

import java.net.URI;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

public class GatewayListener implements WebSocket.Listener {
    private static boolean logging;
    private static ObjectMapper mapper = new ObjectMapper();
    StringBuilder text = new StringBuilder();
    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("[DiscordGateway] Successfully connected to Discord Gateway!");
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        text.append(data);
        webSocket.request(1);

        try {
            if (last) {
                if (logging) {
                    System.out.println("[DiscordBotGateway]: Received data:\n" + text);
                }
                ObjectNode node = mapper.readValue(text.toString(), ObjectNode.class);
                JsonNode eventNode = null;
                StringBuilder event = new StringBuilder();

                if(node.has("t")) {
                    eventNode = node.get("t");
                    event.append(eventNode.asText());
                }
                String eventName = event.toString();

                switch(eventName) {
                    case "READY":
                        if(GeneralConfig.getEventListener() != null) {
                            JsonNode dNode = null;
                            int apiVersion = 0;

                            if(node.has("d")) {
                                dNode = node.get("d");
                                if(dNode.has("v")) {
                                    apiVersion = dNode.get("v").asInt();
                                }
                            }

                            int sequenceNumber = node.get("s").asInt();
                            String sessionId = dNode.get("session_id").asText();
                            String resumeGatewayUrl = dNode.get("resume_gateway_url").asText();

                            GeneralConfig.initReady(sessionId, resumeGatewayUrl, sequenceNumber);

                            ReadyAction action = new ReadyAction(apiVersion, new DiscordAccountManager());
                            GeneralConfig.getEventListener().onReady(action);
                        }
                }

                text = new StringBuilder();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        text.append(new String(data.array(), StandardCharsets.UTF_8));
        webSocket.request(1);

        if(last) {
            if(logging) {
                System.out.println("[DiscordBotGateway] Received binary!Data:\n" + text.toString());
            }
            text = new StringBuilder();
        }
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        webSocket.request(1);
        try {
            throw error;
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        text.append(new String(message.array(), StandardCharsets.UTF_8));
        webSocket.request(1);

        if(logging) {
            System.out.println("[DiscordBotGateway]: Received ping!Message:\n" + text.toString());
        }
        text = new StringBuilder();

        return null;
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        text.append(new String(message.array(), StandardCharsets.UTF_8));

        if(logging) {
            System.out.println("[DiscordBotGateway]: Received pong!Message:\n" + text.toString());
        }
        text = new StringBuilder();

        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        if(statusCode == 4013) {
            throw new RuntimeException("Invalid intents provided!");
        } else if(statusCode == 4014) {
            throw new RuntimeException("Privileged intents must be approved in your bot account!");
        }

        System.out.println("[DiscordBotGateway]: Gateway connection closed!Status code: " + statusCode + "\nReason: " + reason);

        if(GeneralConfig.isAutoReconnect()) {
            try {
                System.out.println("[DiscordBotGateway] Reconnecting...");

                StringBuilder json = new StringBuilder();
                json.append("{\n");
                json.append("\t\"op\": 6,\n");
                json.append("\t\"d\": {\n");
                json.append("\t\t\"token\": \"" + GeneralConfig.getToken() + "\",\n");
                json.append("\t\t\"session_id\": \"" + GeneralConfig.getSessionId() + "\",\n");
                json.append("\t\t\"seq\": " + GeneralConfig.getSequenceNumber() + "\n");
                json.append("\t}");
                json.append("}\n");
                WebSocket socket = DiscordAccount.getHttpClient().newWebSocketBuilder().buildAsync(URI.create(GeneralConfig.getResumeGatewayUrl()), new GatewayListener()).get();

                socket.sendText(json.toString(), true);
                DiscordAccount.getHeartbeatThread().interrupt();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                StringBuilder healthJson = new StringBuilder();
                                healthJson.append("{\n");
                                healthJson.append("\t\"op\": 1,\n");
                                healthJson.append("\t\"d\": {\n");
                                healthJson.append("\t\t\"heartbeat_interval\": 45000\n");
                                healthJson.append("\t}");
                                healthJson.append("}\n");

                                socket.sendText(healthJson.toString(), true);
                                Thread.sleep(44000);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, "Heartbeat");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void initVars(boolean logging) {
        GatewayListener.logging = logging;
    }
}
