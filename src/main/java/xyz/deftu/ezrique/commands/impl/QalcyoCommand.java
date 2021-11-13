package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

public class QalcyoCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("qalcyo", "Qalcyo is a Minecraft mod development company owned by Deftu.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.reply(new MessageBuilder()
                .setEmbeds(instance.getComponentCreator().createEmbed()
                        .setDescription("You can join the Qalcyo Discord server using the button on this message.")
                        .build())
                .setActionRows(ActionRow.of(
                        Button.link("https://discord.gg/BJzuuc398G", "Join!")
                ))
                .build()).queue();
    }

}