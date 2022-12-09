package net.sascha123789.djava.core.discord.events.objects;

import net.sascha123789.djava.core.discord.models.DiscordAccountManager;

public class ReadyAction {
    private int apiVersion;
    private DiscordAccountManager manager;

    public ReadyAction(int apiVersion, DiscordAccountManager manager) {
        this.apiVersion = apiVersion;
        this.manager = manager;
    }

    public DiscordAccountManager getManager() {
        return manager;
    }

    public int getApiVersion() {
        return apiVersion;
    }
}
