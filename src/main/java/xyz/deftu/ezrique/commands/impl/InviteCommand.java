package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

public class InviteCommand implements ICommand {

    public CommandData data() {
        return new CommandData("invite", "Gives you an invite link for the bot.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.reply(new MessageBuilder()
                .setEmbeds(instance.getComponentCreator().createEmbed()
                        .setDescription("You can invite me using the buttons on this message!")
                        .build())
                .setActionRows(ActionRow.of(
                        Button.link(event.getJDA().getInviteUrl(Permission.ADMINISTRATOR), "Invite me!")
                ))
                .build()).queue();
    }

}