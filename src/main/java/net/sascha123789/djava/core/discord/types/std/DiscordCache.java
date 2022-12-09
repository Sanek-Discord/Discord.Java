package net.sascha123789.djava.core.discord.types.std;

import java.util.ArrayList;
import java.util.List;

public enum DiscordCache {
    /**
     * Emojis will be caching**/
    EMOJIS,
    /**
     * Guilds will be caching**/
    GUILDS,

    /**
     * Stickers will be caching**/
    STICKERS;

    /**
     * Returns ArrayList with all caches**/
    public static List<DiscordCache> getAll() {
        List<DiscordCache> list = new ArrayList<>();
        list.add(EMOJIS);
        list.add(STICKERS);
        list.add(GUILDS);

        return list;
    }
}
