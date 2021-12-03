package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class NukeCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("nuke", "Deletes and re-creates a channel to clear all messages.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MESSAGE_MANAGE)) {
                TextChannel old = event.getTextChannel();
                TextChannel copy = old.createCopy().setPosition(old.getPosition()).complete();
                old.delete().reason("Nuked.").queue();
                copy.sendMessage(TextHelper.success("Nuked channel.")).queue();
            } else {
                event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_CHANNEL, Permission.MESSAGE_MANAGE))).queue();
            }
        } else {
            event.reply(TextHelper.failure("This command can only be ran in servers!")).queue();
        }
    }

}