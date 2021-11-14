package xyz.deftu.ezrique.commands.impl.tickets;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.util.TextHelper;

import java.util.HashMap;
import java.util.Map;

public class TicketHandler {

    private static final TicketHandler INSTANCE = new TicketHandler();
    private final Map<Long, String> openConfirmations = new HashMap<>();
    private final Map<Long, String> closeConfirmations = new HashMap<>();

    public void open(Guild guild, Member member, String reason, ReplyAction reply) {
        Ezrique instance = Ezrique.getInstance();

        if (!instance.getConfigManager().getGuild().hasTickets(guild.getId())) {
            reply.setContent(TextHelper.buildFailure("This server has tickets disabled.")).queue();
            return;
        }

        if (!instance.getConfigManager().getGuild().hasTicketCategory(guild.getId())) {
            reply.setContent(TextHelper.buildFailure("This server does not have a ticket category.")).queue();
            return;
        }

        openConfirmations.put(member.getIdLong(), reason);
        reply.setContent("Are you sure you want to open a ticket?" + (reason == null ? "" : "\n**Reason:** " + reason)).setEphemeral(true)
                .addActionRow(
                        Button.success("ticket|open|confirmation|" + member.getId(), "Confirm")
                ).queue();
    }

    public void close(Guild guild, TextChannel channel, Member member, String reason, ReplyAction reply) {
        closeConfirmations.put(channel.getIdLong(), reason);
        reply.setContent("Are you sure you want to close this ticket?" + (reason == null ? "" : "\n**Reason:** " + reason))
                .addActionRow(
                        Button.success("ticket|close|confirmation|accept|" + channel.getId(), "Confirm"),
                        Button.danger("ticket|close|confirmation|deny|" + channel.getId(), "Deny")
                )
                .queue();
    }

    public String getOpenConfirmation(long id) {
        return openConfirmations.get(id);
    }

    public String getCloseConfirmation(long id) {
        return closeConfirmations.get(id);
    }

    public void removeConfirmation(long id) {
        closeConfirmations.remove(id);
    }

    public static TicketHandler getInstance() {
        return INSTANCE;
    }

}