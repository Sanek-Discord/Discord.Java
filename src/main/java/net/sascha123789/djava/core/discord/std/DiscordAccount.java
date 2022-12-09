package net.sascha123789.djava.core.discord.std;

import net.sascha123789.djava.core.discord.accessors.Account;
import net.sascha123789.djava.core.discord.events.GatewayEventListener;
import net.sascha123789.djava.core.discord.models.DiscordAccountManager;
import net.sascha123789.djava.core.discord.types.std.DiscordAccountType;
import net.sascha123789.djava.core.discord.types.std.DiscordCache;
import net.sascha123789.djava.core.discord.types.std.DiscordIntent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.List;

public class DiscordAccount implements Account {
    private String token;
    private DiscordAccountType accountType;
    private List<DiscordIntent> intents;
    private List<DiscordCache> caches;
    private static HttpClient httpClient = HttpClient.newBuilder().build();
    private boolean compress;
    private boolean sharding;
    private int[] shards;
    private int shardsCount;
    private boolean recommendedShards;
    private boolean logging;
    private GatewayEventListener eventListener;
    private boolean autoReconnect;
    private static Thread thread;

    protected DiscordAccount(String token, DiscordAccountType accountType, List<DiscordIntent> intents, boolean compress, boolean sharding, int shardsCount, boolean recommendedShards, boolean logging, GatewayEventListener eventListener, boolean autoReconnect, List<DiscordCache> caches) {
        this.token = token;
        this.accountType = accountType;
        this.intents = intents;
        this.caches = caches;
        this.compress = compress;
        this.sharding = sharding;
        this.recommendedShards = recommendedShards;
        if(sharding && !recommendedShards) {
            this.shardsCount = shardsCount;
            shards = new int[shardsCount];

            for(int i = 0; i < shards.length; i++) {
                shards[i] = i;
            }
        }
        this.logging = logging;
        this.eventListener = eventListener;
        this.autoReconnect = autoReconnect;
    }

    /**
     * Get used HttpClient**/
    public static HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Returns provided in builder bot token**/
    public String getBotToken() {
        return token;
    }

    /**
     * Returns account type**/
    public DiscordAccountType getAccountType() {
        return accountType;
    }

    /**
     * Returns provided intents**/
    public List<DiscordIntent> getIntents() {
        if(intents == null) {
            throw new NullPointerException("Intents is empty");
        }

        return intents;
    }

    /**
     * Returns enabled caches**/
    public List<DiscordCache> getEnabledCaches() {
        if(caches == null) {
            throw new NullPointerException("Enabled caches is empty");
        }

        return caches;
    }

    /**
     * Returns true if compress enabled**/
    public boolean isCompress() {
        return compress;
    }

    /**
     * Returns true if sharding enabled**/
    public boolean isSharding() {
        return sharding;
    }

    public DiscordAccountManager getManager() {
        DiscordAccountManager manager = new DiscordAccountManager();

        return manager;
    }

    /**
     * If possible, returns bot shards array**/
    public int[] getShardsArray() {
        if(!sharding) {
            throw new RuntimeException("Shards array available only in ShardedDiscordAccountBuilder");
        }

        return shards;
    }

    /**
     * If possible, returns totally shards count**/
    public int getShardsCount() {
        if(!sharding) {
            throw new RuntimeException("Shards count available only in ShardedDiscordAccountBuilder");
        }

        return shards.length;
    }

    /**
     * If possible, returns provided in builder shards count**/
    public int getProvidedShardsCount() {
        if(!sharding) {
            throw new RuntimeException("Shards count available only in ShardedDiscordAccountBuilder");
        }

        return shardsCount;
    }

    /**
     * If possible, returns true if bot using recommended shards count**/
    public boolean isUsingRecommendedShardsCount() {
        if(!sharding) {
            throw new RuntimeException("Shards available only in ShardedDiscordAccountBuilder");
        }

        return recommendedShards;
    }

    /**
     * Returns true if additional logging enabled**/
    public boolean isLogging() {
        return logging;
    }

    /**
     * Returns true if auto-reconnecting enabled**/
    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public static Thread getHeartbeatThread() {

        if(GeneralConfig.getToken() == null) {
            throw new RuntimeException("Before getting heartbeat thread you must create Discord account and start it!");
        }

        return thread;
    }

    /**
     * Start bot gateway**/
    public void start() {
        GeneralConfig.init(token, eventListener, autoReconnect, caches, intents);
        if(accountType == DiscordAccountType.SELF) {
            System.out.println("Self accounts in developing...");
            throw new RuntimeException();
        }
        System.out.println("[DiscordApi] Starting " + (accountType == DiscordAccountType.BOT ? "bot" : "self") + " account...");

        try {
            GatewayListener.initVars(logging);
            IGateway gateway = null;
            if(sharding) {
                gateway = ShardedGateway.getBotGateway(token);

            } else {
                gateway = Gateway.getBotGateway(token);
            }
            WebSocket socket = httpClient.newWebSocketBuilder().buildAsync(new URI(gateway.getUrl() + "/?v=10&encoding=json"), new GatewayListener()).get();
            socket.request(1);
            int total = 0;

            if(sharding && recommendedShards) {
                int recommended = gateway.getRecommendedShards();
                shards = new int[recommended + 1];
                for(int i = 0; i < recommended + 1; i++) {
                    shards[i] = i;
                }
            }

            if(this.intents == null) {
                total = 0;
            } else {
                for(DiscordIntent intent: this.intents) {
                    total += intent.getCode();
                }
            }

            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("\"op\": 2,\n");
            json.append("\"d\": {\n");
            json.append("\t\t\"token\": \"" + token + "\",\n");
            json.append("\t\t\"intents\": " + String.valueOf(total) + ",\n");
            json.append("\t\t\"properties\": {\n");
            json.append("\t\t\t\"os\": \"windows\",\n");
            json.append("\t\t\t\"browser\": \"djava\",\n");
            json.append("\t\t\t\"device\": \"djava\"\n");
            json.append("\t\t},\n");
            json.append("\t\t\"compress\": " + compress + (sharding ? "," : "") + "\n");
            if (sharding) {
                json.append("\t\t\"shard\": " + Arrays.toString(shards) + "\n");
            }
            json.append("\t}\n");
            json.append("}\n");

            socket.sendText(json.toString(), true);
            System.out.println("[DiscordBotGateway] Trying to start(if your account not sending ready event, check your approved intents and retry)");
            thread = new Thread(new Runnable() {
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

            Thread keepAlive = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {

                    }
                }
            }, "Keep alive");
            thread.start();
            keepAlive.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}