package net.sascha123789.djava.core.discord.models;

import net.sascha123789.djava.core.discord.std.GeneralConfig;

public class DiscordAccountManager {
    public DiscordAccountManager() {
        if(GeneralConfig.getToken() == null) {
            throw new RuntimeException("Before creating manager, you need to create Discord account!");
        }
    }

    /**
     * Returns api user object**/
    public SelfUser getSelfUser() {
        return SelfUser.getUser();
    }

    /**
     * Returns Guild object by id**/
    public Guild getGuildById(String id) {
        return Guild.getGuild(id);
    }

    /**
     * Returns Guild object by id**/
    public Guild getGuildById(long id) {
        String guildId = String.valueOf(id);

        return Guild.getGuild(guildId);
    }
}
