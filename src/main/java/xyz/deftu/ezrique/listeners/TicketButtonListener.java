package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.features.TicketHandler;
import xyz.deftu.ezrique.util.TextHelper;

public class TicketButtonListener extends ListenerBase {

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button button = event.getButton();
            String id = button.getId();
            if (id.startsWith("ticket|open|confirmation")) {
                TicketHandler.getInstance().open(event.getGuild(), event.getMember(), event.deferReply());
            }

            if (id.startsWith("ticket|close") && !id.contains("confirmation")) {
                TicketHandler.getInstance().confirmClose(event.getGuild(), event.getTextChannel(), event.getMember(), "Closed via close button, there is no valid way to input a reason this way.", event.deferReply());
            }

            if (id.startsWith("ticket|close|confirmation|")) {
                String reason = TicketHandler.getInstance().getCloseConfirmation(event.getChannel().getIdLong());
                if (reason != null) {
                    if (id.contains("accept"))
                        TicketHandler.getInstance().closeAccept(event.getGuild(), event.getMember(), event.getTextChannel(), event.deferReply());
                    if (id.contains("deny"))
                        TicketHandler.getInstance().closeDeny(event.deferReply());
                    TicketHandler.getInstance().invalidateCloseConfirmation(event.getChannel().getIdLong());
                } else {
                    event.reply(TextHelper.buildFailure("This confirmation is invalid.")).setEphemeral(true).queue();
                }
            }
        }
    }

}