package net.sascha123789.djava.testbot;

import net.sascha123789.djava.core.discord.events.GatewayEventListener;
import net.sascha123789.djava.core.discord.events.objects.ReadyAction;
import net.sascha123789.djava.core.discord.models.Guild;
import net.sascha123789.djava.core.discord.models.SelfUser;

public class EventListener implements GatewayEventListener {
    @Override
    public void onReady(ReadyAction action) {
        SelfUser self = action.getManager().getSelfUser();

        System.out.println("[Ready] Its ready event!Current API Version: " + action.getApiVersion());
        System.out.println("Kobrex name: " + self.getUsername());
        System.out.println("Kobrex locale: " + self.getLocaleAsString());
        System.out.println("Kobrex nitro type: " + self.getNitroType());
        System.out.println("Kobrex bot?: " + self.isBot());

        Guild guild = action.getManager().getGuildById(922404056599781397L);

        //self.changeUsername("Kobrex");
        //self.changeAvatar("https://cdn.discordapp.com/attachments/1013132474945126400/1049988649955115038/Hug.png");
        //self.changeAvatar(guild.getIconUrl());

        System.out.println(guild.getName() + " verify level: " + guild.getVerificationLevel());

        System.out.println("Role name: " + guild.getRoleById("1023968752821342290").getName());
    }
}
