package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class KickCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("kick", "Allows moderators to kick members in a server.")
                .addOptions(
                        new OptionData(OptionType.USER, "user", "The you'd like to kick.", true),
                        new OptionData(OptionType.STRING, "reason", "The reason this user is being kicked.")
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                Member member = event.getGuild().getMember(event.getOption("user").getAsUser());
                OptionMapping reasonMapping = event.getOption("reason");
                String reason = reasonMapping == null ? null : reasonMapping.getAsString();

                if (member != null) {
                    AuditableRestAction<Void> kickAction = member.kick();
                    if (reason != null)
                        kickAction.reason(reason);
                    kickAction.queue();

                    AtomicBoolean messageSuccess = new AtomicBoolean(false);
                    try {
                        PrivateChannel privateChannel;
                        try {
                            privateChannel = member.getUser().openPrivateChannel().complete();
                        } catch (Exception ignored) {
                            privateChannel = null;
                        }

                        if (privateChannel != null) {
                            MessageAction action = privateChannel.sendMessage("You were kicked from " + event.getGuild().getName() + "." + (reason == null ? "" : "\n**Reason:** " + reason));
                            messageSuccess.set(true);
                            action.queue(message -> messageSuccess.set(true), throwable -> messageSuccess.set(false));
                        } else {
                            messageSuccess.set(false);
                        }
                    } catch (Exception e) {
                        Ezrique.getInstance().getErrorHandler().handle(e);
                    }

                    String reply = "Successfully kicked **" + member.getUser().getAsTag() + "**";
                    if (reasonMapping != null)
                        reply += " with the reason \"" + reason + "\"";
                    if (!messageSuccess.get())
                        reply += " and was unable to message them about it";
                    event.reply(TextHelper.success(reply + ".")).queue();
                } else {
                    event.reply(TextHelper.failure("Failed to find member.")).queue();
                }
            } else {
                event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.KICK_MEMBERS))).queue();
            }
        } else {
            event.reply("Thus command ran only ran in servers!").queue();
        }
    }

}