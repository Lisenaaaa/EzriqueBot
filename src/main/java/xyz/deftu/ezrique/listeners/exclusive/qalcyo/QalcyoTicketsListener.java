package xyz.deftu.ezrique.listeners.exclusive.qalcyo;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.commands.impl.tickets.TicketHandler;
import xyz.deftu.ezrique.listeners.ListenerBase;

public class QalcyoTicketsListener extends ListenerBase {

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button button = event.getButton();
            if (button.getId().startsWith("qalcyoticket")) {
                TicketHandler.getInstance().open(event.getGuild(), event.getMember(), null, event.deferReply());
            }
        }
    }

}