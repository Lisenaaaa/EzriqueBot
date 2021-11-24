package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.features.TicketHandler;
import xyz.deftu.ezrique.util.IdentificationHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class TicketButtonListener extends ListenerBase {

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button button = event.getButton();
            String id = button.getId();
            if (id.startsWith("ticket|open|confirmation")) {
                Ezrique instance = Ezrique.getInstance();
                Guild guild = event.getGuild();
                Member member = event.getMember();
                ReplyAction reply = event.deferReply();

                String reason = TicketHandler.getInstance().getOpenConfirmation(member.getIdLong());

                if (reason != null) {
                    Category category = guild.getCategoryById(instance.getConfigManager().getGuild().getTickets().getCategory(guild.getId()));
                    if (category == null) {
                        reply.setContent(TextHelper.buildFailure("Cannot find this server's ticket category.")).queue();
                    } else {
                        try {
                            TextChannel channel = category.createTextChannel(instance.getConfigManager().getGuild().getTickets().getName(guild.getId())
                                            .replace("{name}", member.getUser().getName())
                                            .replace("{id}", member.getId())
                                            .replace("{uuid}", IdentificationHelper.generateUuid()))
                                    .setTopic("Ticket created by " + member.getUser().getAsTag() + ". (" + member.getId() + ")")
                                    .syncPermissionOverrides()
                                    .complete();
                            channel.putPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                            channel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_READ, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_WRITE).queue();

                            MessageBuilder messageBuilder = new MessageBuilder();
                            if (instance.getConfigManager().getGuild().getTickets().isRoleAvailable(guild.getId())) {
                                Role role = guild.getRoleById(instance.getConfigManager().getGuild().getTickets().getRole(guild.getId()));
                                if (role == null) {
                                    messageBuilder.append("Could not find this server's ticket role.");
                                } else {
                                    messageBuilder.append(role.getAsMention());
                                }
                            }

                            EmbedBuilder embed = instance.getComponentCreator().createEmbed(guild.getJDA());
                            if (reason != null) {
                                embed.addField("Reason", reason, false);
                            }

                            embed.addField("User", String.format("**Created:** <t:%s:f>\n**Joined:** <t:%s:f>", member.getTimeCreated().toEpochSecond(), member.getTimeJoined().toEpochSecond()), false);

                            channel.sendMessage(messageBuilder.setEmbeds(embed.build()).setActionRows(ActionRow.of(Button.danger("ticket|close|" + channel.getId(), "Close"))).build()).queue();
                            reply.setContent(TextHelper.buildSuccess("Successfully created ticket. " + channel.getAsMention())).setEphemeral(true).queue();

                            TicketHandler.getInstance().invalidateOpenConfirmation(member.getIdLong());
                        } catch (Exception e) {
                            if (e instanceof InsufficientPermissionException) {
                                reply.setContent(TextHelper.buildFailure("Unable to create ticket because I don't have permission to do so! Please contact a server admin.")).queue();
                            }
                        }
                    }
                } else {
                    event.reply(TextHelper.buildFailure("This confirmation is invalid.")).setEphemeral(true).queue();
                }
            }

            if (id.startsWith("ticket|close") && !id.contains("confirmation")) {
                TicketHandler.getInstance().close(event.getGuild(), event.getTextChannel(), event.getMember(), "Closed via close button, there is no valid way to input a reason this way.", event.deferReply());
            }

            if (id.startsWith("ticket|close|confirmation|")) {
                String reason = TicketHandler.getInstance().getCloseConfirmation(event.getChannel().getIdLong());

                if (reason != null) {
                    if (id.contains("accept")) {
                        event.getTextChannel().delete().reason("Ticket closed" + (reason == null ? "" : " - " + reason)).queue();
                        try {
                            TextChannel channel = event.getTextChannel();
                            String topic = channel.getTopic();
                            Member member = event.getGuild().getMemberById(topic.substring(channel.getTopic().lastIndexOf("(")).replace("(", "").replace(")", ""));
                            PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
                            privateChannel.sendMessage("Your ticket in " + event.getGuild().getName() + " (" + event.getTextChannel().getName() + ") was deleted." + (reason == null ? "" : "\n**Reason:** " + reason)).queue(message -> {}, throwable -> {});
                        } catch (Exception ignored) {
                        }
                    }

                    if (id.contains("deny")) {
                        event.reply(TextHelper.buildFailure("Cancelled.")).setEphemeral(true).queue();
                    }

                    TicketHandler.getInstance().invalidateCloseConfirmation(event.getChannel().getIdLong());
                } else {
                    event.reply(TextHelper.buildFailure("This confirmation is invalid.")).setEphemeral(true).queue();
                }
            }
        }
    }

}