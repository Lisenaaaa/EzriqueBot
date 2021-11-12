package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.EmojiHelper;
import xyz.qalcyo.mango.Objects;

public class PingCommand implements ICommand {

    public CommandData data() {
        return new CommandData("ping", "Provides the bot's latency information.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(instance.getPrimaryColour());
        embedBuilder.appendDescription(EmojiHelper.fromUnicode("\uD83C\uDFD3").getAsMention()).appendDescription(" **").appendDescription(event.getJDA().getRestPing().complete().toString()).appendDescription("ms**\n");
        embedBuilder.appendDescription(EmojiHelper.fromUnicode("\uD83D\uDC97").getAsMention()).appendDescription(" **").appendDescription(Objects.stringify(event.getJDA().getGatewayPing())).appendDescription("ms**");

        event.reply(messageBuilder.setEmbeds(embedBuilder.build()).build()).queue();
    }

}