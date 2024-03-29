package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandManager;
import xyz.deftu.ezrique.commands.ICommand;

import java.util.List;

public class HelpCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("help", "Lists all commands currently in the bot.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());

        CommandManager commandManager = instance.getCommandManager();
        for (ICommand command : commandManager.getCommands()) {
            List<Long> guildIds = command.getGuildIds();
            if (guildIds != null && !guildIds.isEmpty()) {
                continue;
            }

            List<SubcommandData> subcommands = command.getData().getSubcommands();
            if (subcommands.isEmpty()) {
                appendCommand(embedBuilder, command.getData().getName(), command.getData().getDescription());
            } else {
                for (SubcommandGroupData group : command.getData().getSubcommandGroups()) {
                    for (SubcommandData subcommand : group.getSubcommands()) {
                        embedBuilder.appendDescription("**/").appendDescription(command.getData().getName()).appendDescription(" ").appendDescription(group.getName()).appendDescription(" ").appendDescription(subcommand.getName()).appendDescription("** - ").appendDescription(subcommand.getDescription());
                        if (group.getSubcommands().indexOf(subcommand) != group.getSubcommands().size() - 1) {
                            embedBuilder.appendDescription("\n");
                        }
                    }
                }

                for (SubcommandData subcommand : subcommands) {
                    embedBuilder.appendDescription("**/").appendDescription(command.getData().getName()).appendDescription(" ").appendDescription(subcommand.getName()).appendDescription("** - ").appendDescription(subcommand.getDescription());
                    if (subcommands.indexOf(subcommand) != subcommands.size() - 1) {
                        embedBuilder.appendDescription("\n");
                    }
                }
            }

            if (commandManager.getCommands().indexOf(command) != commandManager.getCommands().size() - 1) {
                embedBuilder.appendDescription("\n");
            }
        }

        event.reply(messageBuilder.setEmbeds(embedBuilder.build()).build()).queue();
    }

    private void appendCommand(EmbedBuilder embedBuilder, String name, String description) {
        embedBuilder.appendDescription("**/").appendDescription(name).appendDescription("** - ").appendDescription(description);
    }

}