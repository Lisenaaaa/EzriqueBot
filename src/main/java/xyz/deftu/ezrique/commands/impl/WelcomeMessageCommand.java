package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

public class WelcomeMessageCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("welcomemessage", "Allows you to set a welcome message that is sent when a member joins your server.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {

    }

}