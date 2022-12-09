package net.sascha123789.djava.testbot;

import net.sascha123789.djava.core.discord.std.DiscordAccount;
import net.sascha123789.djava.core.discord.std.ShardedDiscordAccountBuilder;
import net.sascha123789.djava.core.discord.types.std.DiscordAccountType;
import net.sascha123789.djava.core.discord.types.std.DiscordCache;
import net.sascha123789.djava.core.discord.types.std.DiscordIntent;

public class Main {
    public static void main(String[] args) {
        // Non Sharding
        /*DiscordAccount bot = null;
        DiscordAccountBuilder botBuilder = new DiscordAccountBuilder("OTQ3NDA2NjA0MTkyNTg3Nzg3.GbV96u.hR-WtXH5LKyBg3BJDA7fdV6LQOErw7k_noEvwc", DiscordAccountType.BOT);
        botBuilder.addIntents(DiscordIntent.MESSAGE_CONTENT, DiscordIntent.DIRECT_MESSAGES);
        bot = botBuilder.build();
        bot.start();*/

        //With Sharding
        ShardedDiscordAccountBuilder botBuilder = new ShardedDiscordAccountBuilder("TOKEN", DiscordAccountType.BOT, true, 0);
        botBuilder.addIntents(DiscordIntent.getAll());
        botBuilder.setLogging(true);
        botBuilder.setEventListener(new EventListener());
        botBuilder.addCaches(DiscordCache.getAll());
        DiscordAccount bot = botBuilder.build();
        bot.start();
    }
}
