package xyz.deftu.ezrique.commands.impl.exclusive;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Bootstrap;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.TextHelper;

public class RestartCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("restart", "Restarts the bot. [OWNER ONLY]");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.getUser().getId().equals(instance.getMetadata().getOwnerId())) {
            event.deferReply().complete().deleteOriginal().queue();
            Bootstrap.restart();
        } else {
            event.reply(TextHelper.failure("Only the bot owner can use this command!")).queue();
        }
    }

}