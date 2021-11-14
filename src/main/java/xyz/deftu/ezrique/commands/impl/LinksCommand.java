package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.EmojiHelper;

public class LinksCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("links", "Provides all links relating to Ezrique.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.reply(new MessageBuilder()
                .setEmbeds(instance.getComponentCreator().createEmbed(event.getJDA())
                        .setTitle(EmojiHelper.fromUnicode("\uD83D\uDD17").getAsMention() + " Links")
                        .build())
                .setActionRows(ActionRow.of(
                        Button.link(event.getJDA().setRequiredScopes("applications.commands").getInviteUrl(Permission.ADMINISTRATOR), "Invite me!"),
                        Button.link("https://discord.gg/kEv2YQ2M4D", "Join my support server!"),
                        Button.link("https://discord.gg/BJzuuc398G", "Join Qalcyo!")
                ))
                .build()).queue();
    }

}