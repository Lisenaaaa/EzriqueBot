package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.features.TicketHandler;

public class TicketMenuListener extends ListenerBase {

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button button = event.getButton();
            if (button.getId().startsWith("ticketmenu")) {
                TicketHandler.getInstance().open(event.getGuild(), event.getMember(), "Ticket opened via ticket menu button.", event.deferReply());
            }
        }
    }

}