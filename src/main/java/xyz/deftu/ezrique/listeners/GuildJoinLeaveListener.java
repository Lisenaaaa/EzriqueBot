package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class GuildJoinLeaveListener extends ListenerBase {

    @SubscribeEvent
    private void onGuildJoin(GuildMemberJoinEvent event) {
        if (ezrique.getConfigManager().getGuild().getWelcomeChannel().isAvailable(event.getGuild().getId())) {
            GuildChannel channel = event.getGuild().getGuildChannelById(ezrique.getConfigManager().getGuild().getWelcomeChannel().getChannel(event.getGuild().getId()));
            if (channel != null) {
                if (channel instanceof TextChannel) {
                    ((TextChannel) channel).sendMessage(ezrique.getConfigManager().getGuild().getWelcomeChannel().getMessage(event.getGuild().getId())
                            .replace("{member.mention}", event.getMember().getAsMention())
                            .replace("{member.tag}", event.getMember().getUser().getAsTag())
                            .replace("{member.name}", event.getMember().getUser().getName())
                            .replace("{member.nick}", event.getMember().getNickname() == null ? event.getMember().getUser().getName() : event.getMember().getNickname())
                            .replace("{server.name}", event.getGuild().getName())
                            .replace("{server.members}", String.valueOf(event.getGuild().getMemberCount()))).queue();
                } else {
                    if (event.getGuild().getSystemChannel() != null) {
                        event.getGuild().getSystemChannel().sendMessage("I can't send a message to the welcomes channel because it's not a text channel!").queue();
                    }
                }
            } else {
                if (event.getGuild().getSystemChannel() != null) {
                    event.getGuild().getSystemChannel().sendMessage("I can't send a message to the welcomes channel because I can't find it!").queue();
                }
            }
        }
    }

    @SubscribeEvent
    private void onGuildRemove(GuildMemberRemoveEvent event) {
        if (ezrique.getConfigManager().getGuild().getLeaveChannel().isAvailable(event.getGuild().getId())) {
            GuildChannel channel = event.getGuild().getGuildChannelById(ezrique.getConfigManager().getGuild().getLeaveChannel().getChannel(event.getGuild().getId()));
            if (channel != null) {
                if (channel instanceof TextChannel) {
                    ((TextChannel) channel).sendMessage(ezrique.getConfigManager().getGuild().getLeaveChannel().getMessage(event.getGuild().getId())
                            .replace("{member.mention}", event.getMember().getAsMention())
                            .replace("{member.tag}", event.getMember().getUser().getAsTag())
                            .replace("{member.name}", event.getMember().getUser().getName())
                            .replace("{member.nick}", event.getMember().getNickname() == null ? event.getMember().getUser().getName() : event.getMember().getNickname())
                            .replace("{server.name}", event.getGuild().getName())
                            .replace("{server.members}", String.valueOf(event.getGuild().getMemberCount()))).queue();
                } else {
                    if (event.getGuild().getSystemChannel() != null) {
                        event.getGuild().getSystemChannel().sendMessage("I can't send a message to the leaves channel because it's not a text channel!").queue();
                    }
                }
            } else {
                if (event.getGuild().getSystemChannel() != null) {
                    event.getGuild().getSystemChannel().sendMessage("I can't send a message to the leaves channel because I can't find it!").queue();
                }
            }
        }
    }

}