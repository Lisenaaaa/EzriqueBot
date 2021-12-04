package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.EzriqueInfo;
import xyz.deftu.ezrique.commands.ICommand;

public class StatisticsCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("statistics", "Provides bot statistics.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        InteractionHook interaction = event.deferReply().complete();
        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());

        embedBuilder.addField("Bot", retrieveBotStatistics(instance), false);
        embedBuilder.addField("Global", retrieveGlobalStatistics(instance), false);
        for (JDA shard : instance.getApi().getShards()) {
            embedBuilder.addField("Shard " + shard.getShardInfo().getShardId(), retrieveShardInfo(instance, shard), false);
        }

        interaction.editOriginal(new MessageBuilder()
                .setEmbeds(embedBuilder.build())
                .build()).queue();
    }

    private String retrieveBotStatistics(Ezrique instance) {
        StringBuilder value = new StringBuilder();

        value.append("**Version:** ").append(EzriqueInfo.VERSION).append("\n");
        value.append("**Commands:** ").append(instance.getCommandManager().getCommands().size()).append("\n");
        value.append("**Listeners:** ").append(instance.getListenerManager().getListeners().size()).append("\n");
        value.append("**Online since:** ").append("<t:" + instance.getStartTime().toEpochSecond() + ":f>");

        return value.toString();
    }

    private String retrieveGlobalStatistics(Ezrique instance) {
        StringBuilder value = new StringBuilder();

        value.append("**Guilds:** ").append(instance.getApi().getGuildCache().size()).append("\n");
        value.append("**Users:** ").append(instance.getApi().getUserCache().size()).append("\n");
        value.append("**Average Gateway latency:** ").append(instance.getApi().getAverageGatewayPing()).append("\n");

        return value.toString();
    }

    private String retrieveShardInfo(Ezrique instance, JDA shard) {
        StringBuilder value = new StringBuilder();

        value.append("**Guilds:** ").append(shard.getGuildCache().size()).append("\n");
        value.append("**Users:** ").append(shard.getUserCache().size()).append("\n");
        value.append("**REST latency:** ").append(shard.getRestPing().complete()).append("\n");
        value.append("**Gateway latency:** ").append(shard.getGatewayPing()).append("\n");

        return value.toString();
    }

}