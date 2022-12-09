package net.sascha123789.djava.core.discord.types.std;

import java.util.ArrayList;
import java.util.List;

public enum DiscordIntent { GUILDS(1), GUILD_MEMBERS(2), GUILD_BANS(4), GUILD_EMOJIS_AND_STICKERS(8), GUILD_INTEGRATIONS(16), GUILD_WEBHOOKS(32), GUILD_INVITES(64), GUILD_VOICE_STATES(128), GUILD_PRESENCES(256), GUILD_MESSAGES(512), GUILD_MESSAGE_REACTIONS(1024), GUILD_MESSAGE_TYPING(2048), DIRECT_MESSAGES(4096), DIRECT_MESSAGE_REACTIONS(8192), DIRECT_MESSAGE_TYPING(16384), MESSAGE_CONTENT(32768), GUILD_SCHEDULED_EVENTS(65536), AUTO_MODERATION_CONFIGURATION(1048576), AUTO_MODERATION_EXECUTION(2097152);
    private int code;

    private DiscordIntent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * Get all intents(including privileged intents)**/
    public static List<DiscordIntent> getAll() {
        List<DiscordIntent> list = new ArrayList<>();

        list.add(GUILDS);
        list.add(GUILD_MEMBERS);
        list.add(GUILD_BANS);
        list.add(GUILD_EMOJIS_AND_STICKERS);
        list.add(GUILD_INTEGRATIONS);
        list.add(GUILD_WEBHOOKS);
        list.add(GUILD_INVITES);
        list.add(GUILD_VOICE_STATES);
        list.add(GUILD_PRESENCES);
        list.add(GUILD_MESSAGES);
        list.add(GUILD_MESSAGE_REACTIONS);
        list.add(GUILD_MESSAGE_TYPING);
        list.add(DIRECT_MESSAGES);
        list.add(DIRECT_MESSAGE_REACTIONS);
        list.add(DIRECT_MESSAGE_TYPING);
        list.add(MESSAGE_CONTENT);
        list.add(GUILD_SCHEDULED_EVENTS);
        list.add(AUTO_MODERATION_CONFIGURATION);
        list.add(AUTO_MODERATION_EXECUTION);

        return list;
    }

    /**
     * Returns all intents without privileged**/
    public static List<DiscordIntent> getAllWithoutPrivileged() {
        List<DiscordIntent> list = new ArrayList<>();

        for(DiscordIntent intent: getAll()) {
            if(intent != MESSAGE_CONTENT || intent != GUILD_PRESENCES || intent != GUILD_MEMBERS) {
                list.add(intent);
            }
        }

        return list;
    }

    public static List<DiscordIntent> getAllWithout(DiscordIntent... intents) {
        List<DiscordIntent> list = new ArrayList<>();

        for(DiscordIntent i: getAll()) {
            for(DiscordIntent j: intents) {
                if(i != j) {
                    list.add(i);
                }
            }
        }

        return list;
    }
}
