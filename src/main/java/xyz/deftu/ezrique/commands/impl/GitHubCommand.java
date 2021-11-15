package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.TextHelper;

public class GitHubCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("github", "The gateway to Ezrique's GitHub tracker.")
                .addSubcommands(
                        new SubcommandData("link", "The webhook link for this server."),
                        new SubcommandData("toggle", "Allows you to toggle the GitHub tracker.")
                                .addOptions(
                                        new OptionData(OptionType.BOOLEAN, "value", "The new value for this setting.", true)
                                ),
                        new SubcommandData("channel", "The channel GitHub updates will be sent to.")
                                .addOptions(
                                        new OptionData(OptionType.CHANNEL, "value", "The new value for this setting.", true)
                                                .setChannelTypes(ChannelType.TEXT)
                                ),
                        new SubcommandData("role", "A role notified of GitHub updates.")
                                .addOptions(
                                        new OptionData(OptionType.ROLE, "value", "The new value for this setting.", true)
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                assert event.getSubcommandName() != null;
                switch (event.getSubcommandName()) {
                    case "link":
                        event.reply("http://ezrique.deftu.xyz:4357/github?id=" + event.getGuild().getId()).setEphemeral(true).queue();
                        break;
                    case "toggle":
                        handleToggle(instance, event, event.getOption("value").getAsBoolean());
                        break;
                    case "channel":
                        handleChannel(instance, event, (TextChannel) event.getOption("value").getAsGuildChannel());
                        break;
                    case "role":
                        handleRole(instance, event, event.getOption("value").getAsRole());
                        break;
                }
            } else {
                event.reply(TextHelper.buildFailure("Only members with the `Manage server` permission can use this command.")).queue();
            }
        } else {
            event.reply(TextHelper.buildFailure("This command can only be ran in servers!")).queue();
        }
    }

    private void handleToggle(Ezrique instance, SlashCommandEvent event, boolean value) {
        instance.getConfigManager().getGuild().setGitHubToggle(event.getGuild().getId(), value);
        event.reply(TextHelper.buildSuccess("Successfully set GitHub toggle.")).queue();
    }

    private void handleChannel(Ezrique instance, SlashCommandEvent event, TextChannel value) {
        instance.getConfigManager().getGuild().setGitHubChannel(event.getGuild().getId(), value.getId());
        event.reply(TextHelper.buildSuccess("Successfully set GitHub channel.")).queue();
    }

    private void handleRole(Ezrique instance, SlashCommandEvent event, Role value) {
        instance.getConfigManager().getGuild().setGitHubRole(event.getGuild().getId(), value.getId());
        event.reply(TextHelper.buildSuccess("Successfully set GitHub role.")).queue();
    }

}