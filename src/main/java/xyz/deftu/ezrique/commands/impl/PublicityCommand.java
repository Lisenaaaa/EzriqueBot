package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.config.impl.guild.PublicityConfig;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class PublicityCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("publicity", "Allows you to manage your server's publicity settings.")
                .addSubcommands(
                        new SubcommandData("toggle", "Allows you to toggle publicity as a whole.")
                                .addOptions(
                                        new OptionData(OptionType.BOOLEAN, "value", "The new value for this setting.", true)
                                ),
                        new SubcommandData("invite", "Show your server's invite in public leaderboards.")
                                .addOptions(
                                        new OptionData(OptionType.BOOLEAN, "value", "The new value for this setting.", true)
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                PublicityConfig publicity = instance.getConfigManager().getGuild().getPublicity();
                switch (event.getSubcommandName()) {
                    case "toggle":
                        publicity.setToggle(event.getGuild().getId(), event.getOption("value").getAsBoolean());
                        event.reply(TextHelper.success("Successfully set publicity toggle.")).setEphemeral(true).queue();
                        break;
                    case "invite":
                        publicity.setInvite(event.getGuild().getId(), event.getOption("value").getAsBoolean());
                        event.reply(TextHelper.success("Successfully set publicity invite toggle.")).setEphemeral(true).queue();
                        break;
                }
            } else {
                event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_SERVER))).queue();
            }
        } else {
            event.reply(TextHelper.failure("This command can only be ran in servers!")).queue();
        }
    }

}