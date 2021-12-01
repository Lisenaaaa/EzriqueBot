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

public class BanCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("ban", "Allows moderators to ban members in a server.")
                .addOptions(
                        new OptionData(OptionType.USER, "user", "The user you'd like to ban.", true),
                        new OptionData(OptionType.STRING, "reason", "The reason this user is being banned."),
                        new OptionData(OptionType.INTEGER, "days", "The amount of previous days' messages from this user that should be deleted.")
                                .addChoice("1", 1)
                                .addChoice("2", 2)
                                .addChoice("3", 3)
                                .addChoice("4", 4)
                                .addChoice("5", 5)
                                .addChoice("6", 6)
                                .addChoice("7", 7)
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                Member member = event.getGuild().getMember(event.getOption("user").getAsUser());
                OptionMapping reasonMapping = event.getOption("reason");
                String reason = reasonMapping == null ? null : reasonMapping.getAsString();
                OptionMapping daysMapping = event.getOption("days");
                int days = daysMapping == null ? 0 : (int) daysMapping.getAsLong();

                if (member != null) {
                    AuditableRestAction<Void> banAction = member.ban(days);
                    if (reason != null)
                        banAction.reason(reason);
                    banAction.queue();

                    AtomicBoolean messageSuccess = new AtomicBoolean(false);
                    try {
                        PrivateChannel privateChannel;
                        try {
                            privateChannel = member.getUser().openPrivateChannel().complete();
                        } catch (Exception ignored) {
                            privateChannel = null;
                        }

                        if (privateChannel != null) {
                            MessageAction action = privateChannel.sendMessage("You were banned in " + event.getGuild().getName() + "." + (reason == null ? "" : "\n**Reason:** " + reason));
                            messageSuccess.set(true);
                            action.queue(message -> messageSuccess.set(true), throwable -> messageSuccess.set(false));
                        } else {
                            messageSuccess.set(false);
                        }
                    } catch (Exception e) {
                        Ezrique.getInstance().getErrorHandler().handle(e);
                    }

                    String reply = "Successfully banned **" + member.getUser().getAsTag() + "**";
                    if (reasonMapping != null)
                        reply += " with the reason \"" + reason + "\"";
                    if (daysMapping != null)
                        reply += " and deleted all messages sent within the past " + days + " days";
                    if (!messageSuccess.get())
                        reply += " and was unable to message them about it";
                    event.reply(TextHelper.buildSuccess(reply + ".")).queue();
                } else {
                    event.reply(TextHelper.buildFailure("Failed to find member.")).queue();
                }
            } else {
                event.reply(TextHelper.buildFailure(PermissionHelper.getInvalidPermissionsMessage(Permission.BAN_MEMBERS))).queue();
            }
        } else {
            event.reply("Thus command ran only ran in servers!").queue();
        }
    }

}