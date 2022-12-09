package net.sascha123789.djava.core.discord.std;

import net.sascha123789.djava.core.discord.events.GatewayEventListener;
import net.sascha123789.djava.core.discord.types.std.DiscordCache;
import net.sascha123789.djava.core.discord.types.std.DiscordIntent;

import java.util.List;

public class GeneralConfig {
    private static String token;
    private static GatewayEventListener eventListener;
    private static boolean autoReconnect;
    private static String sessionId;
    private static String resumeGatewayUrl;
    private static int sequenceNumber;
    public static String API_STRING = "https://discord.com/api/v10/";
    private static List<DiscordCache> caches;
    private static List<DiscordIntent> intents;

    protected static void init(String token, GatewayEventListener eventListener, boolean autoReconnect, List<DiscordCache> caches, List<DiscordIntent> intents) {
        GeneralConfig.token = token;
        GeneralConfig.eventListener = eventListener;
        GeneralConfig.autoReconnect = autoReconnect;
        GeneralConfig.caches = caches;
        GeneralConfig.intents = intents;
    }

    protected static void initReady(String sessionId, String resumeGatewayUrl, int sequenceNumber) {
        GeneralConfig.sessionId = sessionId;
        GeneralConfig.resumeGatewayUrl = resumeGatewayUrl;
        GeneralConfig.sequenceNumber = sequenceNumber;
    }

    public static List<DiscordIntent> getIntents() {
        return intents;
    }

    public static String getToken() {
        return token;
    }

    public static GatewayEventListener getEventListener() {
        return eventListener;
    }

    public static boolean isAutoReconnect() {
        return autoReconnect;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static String getResumeGatewayUrl() {
        return resumeGatewayUrl;
    }

    public static int getSequenceNumber() {
        return sequenceNumber;
    }

    public static List<DiscordCache> getCaches() {
        return caches;
    }
}
