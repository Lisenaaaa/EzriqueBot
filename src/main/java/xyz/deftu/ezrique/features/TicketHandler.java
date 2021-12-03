package xyz.deftu.ezrique.features;

import com.github.benmanes.caffeine.cache.Cache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.util.CacheHelper;
import xyz.deftu.ezrique.util.ChannelHelper;
import xyz.deftu.ezrique.util.IdentificationHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class TicketHandler {

    private static final TicketHandler INSTANCE = new TicketHandler();
    private final Cache<Long, String> openConfirmations = CacheHelper.createCache();
    private final Cache<Long, String> closeConfirmations = CacheHelper.createCache();

    public void confirmOpen(Guild guild, Member member, String reason, ReplyAction reply) {
        Ezrique instance = Ezrique.getInstance();

        if (!instance.getConfigManager().getGuild().getTickets().isAvailable(guild.getId())) {
            reply.setContent(TextHelper.failure("This server does not have tickets set up.")).queue();
            return;
        }

        openConfirmations.put(member.getIdLong(), reason);
        reply.setContent("Are you sure you want to open a ticket?" + (reason == null ? "" : "\n**Reason:** " + reason)).setEphemeral(true)
                .addActionRow(
                        Button.success("ticket|open|confirmation|" + member.getId(), "Confirm")
                ).queue();
    }

    public void open(Guild guild, Member member, ReplyAction reply) {
        Ezrique instance = Ezrique.getInstance();

        String reason = getOpenConfirmation(member.getIdLong());

        if (reason != null) {
            Category category = guild.getCategoryById(instance.getConfigManager().getGuild().getTickets().getCategory(guild.getId()));
            if (category == null) {
                reply.setContent(TextHelper.failure("Cannot find this server's ticket category.")).queue();
            } else {
                try {
                    TextChannel channel = category.createTextChannel(instance.getConfigManager().getGuild().getTickets().getName(guild.getId())
                                    .replace("{name}", member.getUser().getName().replaceAll("\\P{Print}", "").replaceAll(" ", "-"))
                                    .replace("{id}", member.getId())
                                    .replace("{uuid}", IdentificationHelper.generateUuid()))
                            .setTopic("Ticket created by " + member.getUser().getAsTag() + ". (" + member.getId() + ")")
                            .syncPermissionOverrides()
                            .complete();
                    channel.putPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    channel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_SEND).queue();

                    MessageBuilder messageBuilder = new MessageBuilder();
                    if (instance.getConfigManager().getGuild().getTickets().isRoleAvailable(guild.getId())) {
                        Role role = guild.getRoleById(instance.getConfigManager().getGuild().getTickets().getRole(guild.getId()));
                        if (role == null) {
                            messageBuilder.append("Could not find this server's ticket role.");
                        } else {
                            messageBuilder.append(role.getAsMention());
                            channel.putPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_SEND).queue();
                        }
                    }

                    EmbedBuilder embed = instance.getComponentCreator().createEmbed(guild.getJDA());
                    if (!reason.isEmpty()) {
                        embed.addField("Reason", reason, false);
                    }

                    embed.addField("User", String.format("**Created:** <t:%s:f>\n**Joined:** <t:%s:f>", member.getTimeCreated().toEpochSecond(), member.getTimeJoined().toEpochSecond()), false);

                    channel.sendMessage(messageBuilder.setEmbeds(embed.build()).setActionRows(ActionRow.of(Button.danger("ticket|close|" + channel.getId(), "Close"))).build()).queue();
                    reply.setContent(TextHelper.success("Successfully created ticket. " + channel.getAsMention())).setEphemeral(true).queue();

                    invalidateOpenConfirmation(member.getIdLong());
                } catch (Exception e) {
                    if (e instanceof InsufficientPermissionException) {
                        reply.setContent(TextHelper.failure("Unable to create ticket because I don't have permission to do so! Please contact a server admin.")).queue();
                    }
                }
            }
        } else {
            reply.setContent(TextHelper.failure("This confirmation is invalid.")).setEphemeral(true).queue();
        }
    }

    public void confirmClose(Guild guild, TextChannel channel, Member member, String reason, ReplyAction reply) {
        String topic = channel.getTopic();
        if (topic != null && topic.startsWith("Ticket created by")) {
            closeConfirmations.put(channel.getIdLong(), reason);
            reply.setContent("Are you sure you want to close this ticket?" + (reason == null ? "" : "\n**Reason:** " + reason))
                    .addActionRow(
                            Button.success("ticket|close|confirmation|accept|" + channel.getId(), "Confirm"),
                            Button.danger("ticket|close|confirmation|deny|" + channel.getId(), "Deny")
                    )
                    .setEphemeral(true)
                    .queue();
        } else {
            reply.setContent(TextHelper.failure("This is not a ticket!")).setEphemeral(true).queue();
        }
    }

    public void closeAccept(Guild guild, Member member, TextChannel channel, ReplyAction reply) {
        String reason = getCloseConfirmation(channel.getIdLong());
        if (reason != null) {
            try {
                String topic = channel.getTopic();
                Member creator = guild.getMemberById(topic.substring(channel.getTopic().lastIndexOf("(")).replace("(", "").replace(")", ""));
                PrivateChannel privateChannel = creator.getUser().openPrivateChannel().complete();
                String archive = ChannelHelper.archive(channel);
                MessageAction action = privateChannel.sendMessage("Your ticket in " + guild.getName() + " (" + channel.getName() + ") was closed by **" + member.getUser().getAsTag() + "**." + (reason == null ? "" : "\n**Reason:** " + reason));
                if (archive != null) {
                    action.setActionRow(
                            Button.link(archive, "Transcript")
                    );
                }

                action.queue(message -> {}, throwable -> {});
            } catch (Exception e) {
                Ezrique.getInstance().getErrorHandler().handle(e);
            }

            channel.delete().reason("Ticket closed" + (reason == null ? "" : " - " + reason)).queue();
        } else {
            reply.setContent(TextHelper.failure("This confirmation is invalid.")).setEphemeral(true).queue();
        }
    }

    public void closeDeny(ReplyAction reply) {
        reply.setContent(TextHelper.failure("Cancelled.")).setEphemeral(true).queue();
    }

    public String getOpenConfirmation(long id) {
        return openConfirmations.getIfPresent(id);
    }

    public String getCloseConfirmation(long id) {
        return closeConfirmations.getIfPresent(id);
    }

    public void invalidateOpenConfirmation(long id) {
        openConfirmations.invalidate(id);
    }

    public void invalidateCloseConfirmation(long id) {
        closeConfirmations.invalidate(id);
    }

    public static TicketHandler getInstance() {
        return INSTANCE;
    }

}