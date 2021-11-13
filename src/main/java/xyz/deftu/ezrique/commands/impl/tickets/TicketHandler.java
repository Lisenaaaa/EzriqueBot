package xyz.deftu.ezrique.commands.impl.tickets;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.util.IdentificationHelper;
import xyz.deftu.ezrique.util.TextHelper;

import java.util.HashMap;
import java.util.Map;

public class TicketHandler {

    private static final TicketHandler INSTANCE = new TicketHandler();
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

        Category category = guild.getCategoryById(instance.getConfigManager().getGuild().getTicketCategory(guild.getId()));
        if (category == null) {
            reply.setContent(TextHelper.buildFailure("Cannot find this server's ticket category.")).queue();
        } else {
            try {
                TextChannel channel = category.createTextChannel(instance.getConfigManager().getGuild().getTicketName(guild.getId())
                                .replace("{name}", member.getUser().getName())
                                .replace("{id}", member.getId())
                                .replace("{uuid}", IdentificationHelper.generateUuid()))
                        .setTopic("Ticket created by " + member.getUser().getAsTag() + ". (" + member.getId() + ")")
                        .syncPermissionOverrides()
                        .complete();
                channel.putPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                channel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_READ, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_WRITE).queue();

                MessageBuilder messageBuilder = new MessageBuilder();
                if (instance.getConfigManager().getGuild().hasTicketRole(guild.getId())) {
                    Role role = guild.getRoleById(instance.getConfigManager().getGuild().getTicketRole(guild.getId()));
                    if (role == null) {
                        messageBuilder.append("Could not find this server's ticket role.");
                    } else {
                        messageBuilder.append(role.getAsMention());
                    }
                }

                EmbedBuilder embed = instance.getComponentCreator().createEmbed();
                if (reason != null) {
                    embed.addField("Reason", reason, false);
                }

                embed.addField("User", String.format("**Created:** <t:%s:f>\n**Joined:** <t:%s:f>", member.getTimeCreated().toEpochSecond(), member.getTimeJoined().toEpochSecond()), false);

                channel.sendMessage(messageBuilder.setEmbeds(embed.build()).setActionRows(ActionRow.of(Button.danger("ticket|close|" + channel.getId(), "Close"))).build()).queue();
                reply.setContent(TextHelper.buildSuccess("Successfully created ticket. " + channel.getAsMention())).setEphemeral(true).queue();
            } catch (Exception e) {
                if (e instanceof InsufficientPermissionException) {
                    reply.setContent(TextHelper.buildFailure("Unable to create ticket because I don't have permission to do so! Please contact a server admin.")).queue();
                }
            }
        }
    }

    public void close(Guild guild, TextChannel channel, Member member, String reason, ReplyAction reply) {
        Ezrique instance = Ezrique.getInstance();
        long channelId = channel.getIdLong();

        if (closeConfirmations.containsKey(channelId)) {
            reply.setContent(TextHelper.buildFailure("You already have an active confirmation in this ticket."));
        } else {
            closeConfirmations.put(channel.getIdLong(), reason);
            reply.setContent("Are you sure you want to close this ticket?" + (reason == null ? "" : "\n**Reason:** " + reason))
                    .addActionRow(
                            Button.success("ticket|close|confirmation|accept|" + channel.getId(), "Confirm"),
                            Button.danger("ticket|close|confirmation|deny|" + channel.getId(), "Deny")
                    )
                    .queue();
        }
    }

    public String getConfirmationOf(long id) {
        return closeConfirmations.get(id);
    }

    public void removeConfirmation(long id) {
        closeConfirmations.remove(id);
    }

    public static TicketHandler getInstance() {
        return INSTANCE;
    }

}