package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.EmojiHelper;
import xyz.qalcyo.mango.Objects;

public class StatisticsCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("statistics", "Provides bot statistics.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed();

        embedBuilder.addField("Guilds", String.valueOf(event.getJDA().getGuilds().size()), true);
        embedBuilder.addField("Users", String.valueOf(event.getJDA().getUsers().size()), true);
        embedBuilder.addField("REST latency", event.getJDA().getRestPing().complete() + "ms", true);
        embedBuilder.addField("Gateway latency", event.getJDA().getGatewayPing() + "ms", true);
        embedBuilder.addField("Commands", Objects.stringify(instance.getCommandManager().getCommands().size()), true);
        embedBuilder.addField("Uptime", "I have been online since <t:" + instance.getStartTime().toEpochSecond() + ":f>.", true);

        event.reply(new MessageBuilder().setEmbeds(embedBuilder.build()).build()).queue();
    }

}