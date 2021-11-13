package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

public class SupportCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("support", "Gives you the invite to Ezrique's support server.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.reply(new MessageBuilder()
                .setEmbeds(instance.getComponentCreator().createEmbed()
                        .setDescription("You can join my support server with the button on this message.")
                        .build())
                .setActionRows(ActionRow.of(
                        Button.link("https://discord.gg/kEv2YQ2M4D", "Join!")
                ))
                .build()).queue();
    }

}