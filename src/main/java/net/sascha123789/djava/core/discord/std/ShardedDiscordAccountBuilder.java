package net.sascha123789.djava.core.discord.std;

import net.sascha123789.djava.core.discord.accessors.Account;
import net.sascha123789.djava.core.discord.events.GatewayEventListener;
import net.sascha123789.djava.core.discord.types.std.DiscordAccountType;
import net.sascha123789.djava.core.discord.types.std.DiscordCache;
import net.sascha123789.djava.core.discord.types.std.DiscordIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShardedDiscordAccountBuilder implements Account {
    private String token;
    private DiscordAccountType accountType;
    private List<DiscordIntent> intents;
    private List<DiscordCache> caches;
    private boolean compress;
    private int shardCount;
    private boolean recommendedShards;
    private boolean logging;
    private GatewayEventListener eventListener;
    private boolean autoReconnect;

    /**
     * @param token Discord bot token
     * @param accountType Discord account type
     * @param recommendedShards If true, shardCount will be ignored and equals Discord recommended count
     * @param shardCount Total shard count**/
    public ShardedDiscordAccountBuilder(String token, DiscordAccountType accountType, boolean recommendedShards, int shardCount) {
        this.token = token;
        this.accountType = accountType;
        this.compress = false;
        this.shardCount = shardCount;
        if(shardCount < 0) {
            throw new RuntimeException("Shard count must starting with zero/0");
        }
        this.recommendedShards = recommendedShards;
        this.logging = false;
        this.autoReconnect = true;
        this.caches = new ArrayList<>();
        this.intents = new ArrayList<>();
    }

    /**
     * Set gateway event listener**/
    public void setEventListener(GatewayEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Sets bot token**/
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * If true, shardCount will be ignored and equals Discord recommended count**/
    public void setRecommendedShards(boolean recommendedShards) {
        this.recommendedShards = recommendedShards;
    }

    /**
     * Returns true if recommended sharding turned on, else false**/
    public boolean isRecommendedShards() {
        return recommendedShards;
    }

    /**
     * Returns provided shards count**/
    public int getShardCount() {
        return shardCount;
    }

    /**
     * Setting totally shard count
     * Ignoring if recommended shards turned on**/
    public void setShardCount(int shardCount) {
        if(shardCount < 0) {
            throw new RuntimeException("Shard count starting with zero/0");
        }
        this.shardCount = shardCount;
    }

    /**
     * Print more info in out stream**/
    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    /**
     * True if additional logging turned on**/
    public boolean isLogging() {
        return logging;
    }

    /**
     * True if compress is enabled**/
    public boolean isCompress() {
        return compress;
    }

    /**
     * Add intents to bot gateway**/
    public void addIntents(DiscordIntent... intents) {
        this.intents.addAll(Arrays.asList(intents));
    }

    /**
     * Add intents to bot gateway**/
    public void addIntents(Collection<DiscordIntent> intents) {
        this.intents.addAll(intents);
    }

    /**
     * Sets compress in Discord API**/
    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    /**
     * Caching slows down the launch of the api, but greatly speeds up the process of obtaining some time-consuming data like a server emoji**/
    public void addCaches(DiscordCache... caches) {
        this.caches.addAll(Arrays.asList(caches));
    }

    /**
     * Caching slows down the launch of the api, but greatly speeds up the process of obtaining some time-consuming data like a server emoji**/
    public void addCaches(Collection<DiscordCache> caches) {
        this.caches.addAll(caches);
    }

    /**
     * Get provided bot token**/
    public String getToken() {
        return token;
    }

    /**
     * Get account type**/
    public DiscordAccountType getAccountType() {
        return accountType;
    }

    /**
     * Get provided bot intents**/
    public List<DiscordIntent> getIntents() {
        return intents;
    }

    /**
     * Get provided bot caching elements**/
    public List<DiscordCache> getCaches() {
        return caches;
    }

    /**
     * If true, on websocket close bot will attempt reconnect**/
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    /**
     * Returns true if auto-reconnect enabled**/
    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    /**
     * Build builder to DiscordAccount**/
    public DiscordAccount build() {
        try {
            return new DiscordAccount(token, accountType, intents, compress, true, shardCount, recommendedShards, logging, eventListener, autoReconnect, caches);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}