package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.commands.impl.tickets.TicketHandler;
import xyz.deftu.ezrique.util.TextHelper;
import xyz.qalcyo.mango.collections.Pair;

public class TicketButtonListener extends ListenerBase {

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button button = event.getButton();
            String id = button.getId();
            if (id.startsWith("ticket|close") && !id.contains("confirmation")) {
                TicketHandler.getInstance().close(event.getGuild(), event.getTextChannel(), event.getMember(), "Closed via close button, there is no valid way to input a reason this way.", event.deferReply());
            }

            if (id.startsWith("ticket|close|confirmation|")) {
                if (id.contains("accept")) {
                    Pair<String, Member> confirmation = TicketHandler.getInstance().getConfirmationOf(event.getChannel().getIdLong());
                    String reason = confirmation.left();
                    Member member = confirmation.right();
                    event.getTextChannel().delete().reason("Ticket closed" + (reason == null ? "" : " - " + reason)).queue();
                    try {
                        PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
                        privateChannel.sendMessage("Your ticket in " + event.getGuild().getName() + " (" + event.getTextChannel().getName() + ") was deleted." + (reason == null ? "" : "\n**Reason:** " + reason)).queue(message -> {}, throwable -> {});
                    } catch (Exception ignored) {
                    }
                }

                if (id.contains("deny")) {
                    TicketHandler.getInstance().removeConfirmation(event.getTextChannel().getIdLong());
                    event.reply(TextHelper.buildFailure("Cancelled.")).queue();
                    event.getMessage().delete().queue();
                }
            }
        }
    }

}