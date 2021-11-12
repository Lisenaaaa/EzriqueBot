package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Bootstrap;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

public class RestartCommand implements ICommand {

    public CommandData data() {
        return new CommandData("restart", "Restarts the bot. [OWNER ONLY]");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.getUser().getId().equals(instance.getConfigManager().getBot().getOwnerId())) {
            event.deferReply().complete().deleteOriginal().queue();
            Bootstrap.restart();
        } else {
            event.reply("Only the bot owner can use this command!").queue();
        }
    }

}