package net.sascha123789.djava.core.discord.events;

import net.sascha123789.djava.core.discord.events.objects.ReadyAction;

public interface GatewayEventListener {
    default void onReady(ReadyAction action) {

    }
}