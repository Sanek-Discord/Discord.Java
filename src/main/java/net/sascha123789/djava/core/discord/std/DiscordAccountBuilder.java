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

public class DiscordAccountBuilder implements Account {
    private String token;
    private DiscordAccountType accountType;
    private List<DiscordIntent> intents;
    private List<DiscordCache> caches;
    private boolean compress;
    private boolean logging;
    private GatewayEventListener eventListener;
    private boolean autoReconnect;

    public DiscordAccountBuilder(String token, DiscordAccountType accountType) {
        this.token = token;
        this.accountType = accountType;
        this.compress = false;
        this.logging = false;
        this.autoReconnect = true;
        this.caches = new ArrayList<>();
        this.intents = new ArrayList<>();
    }

    /**
     * Print more info in out stream**/
    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    /**
     * Set gateway event listener**/
    public void setEventListener(GatewayEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * True if additional logging turned on**/
    public boolean isLogging() {
        return logging;
    }

    /**
     * Sets bot token**/
    public void setToken(String token) {
        this.token = token;
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
    public
    List<DiscordCache> getCaches() {
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
            return new DiscordAccount(token, accountType, intents, compress, false, 0, false, logging, eventListener, autoReconnect, caches);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}