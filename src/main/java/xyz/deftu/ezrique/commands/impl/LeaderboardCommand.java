package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.config.impl.guild.PublicityConfig;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("leaderboard", "Provides a list of servers, their member counts and invites to them.");
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        InteractionHook interaction = event.deferReply().complete();

        PublicityConfig publicity = instance.getConfigManager().getGuild().getPublicity();

        JDA api = event.getJDA();
        List<Guild> guilds = new ArrayList<>(api.getGuilds());
        guilds.sort(Comparator.comparingInt(Guild::getMemberCount));
        Collections.reverse(guilds);

        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(api);
        embedBuilder.setTitle("Leaderboard");

        for (Guild guild : guilds) {
            int index = guilds.indexOf(guild);
            if (index > 15) {
                break;
            }

            if (publicity.isAvailable(guild.getId())) {
                boolean inviteToggle = publicity.isInvite(guild.getId());
                Invite invite = null;
                if (inviteToggle) {
                    try {
                        if (guild.getDefaultChannel() != null) {
                            invite = guild.getDefaultChannel().createInvite().complete();
                        } else if (guild.getSystemChannel() != null) {
                            invite = guild.getSystemChannel().createInvite().complete();
                        } else {
                            inviteToggle = false;
                        }
                    } catch (Exception ignored) {
                    }
                }

                if (index > 0) {
                    embedBuilder.appendDescription("\n");
                }

                embedBuilder.appendDescription("**");
                embedBuilder.appendDescription(inviteToggle ? "[" : "");
                embedBuilder.appendDescription(guild.getName());
                embedBuilder.appendDescription(inviteToggle ? "]" : "");
                if (inviteToggle) {
                    embedBuilder.appendDescription("(");
                    embedBuilder.appendDescription(invite.getUrl());
                    embedBuilder.appendDescription(")");
                }

                embedBuilder.appendDescription("** - ");
                embedBuilder.appendDescription(NumberFormat.getInstance().format(guild.getMemberCount()));
            }
        }

        interaction.editOriginal(new MessageBuilder()
                .setEmbeds(embedBuilder.build())
                .build()).queue();
    }

}