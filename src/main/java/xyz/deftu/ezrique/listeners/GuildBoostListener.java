package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class GuildBoostListener extends ListenerBase {

    @SubscribeEvent
    private void onGuildBoost(GuildMemberUpdateBoostTimeEvent event) {
        if (ezrique.getConfigManager().getGuild().hasBoostMessage(event.getGuild().getId()) && event.getGuild().getSystemChannel() != null) {
            event.getGuild().getSystemChannel().sendMessage(ezrique.getConfigManager().getGuild().getBoostMessage(event.getGuild().getId())
                    .replace("{member.mention}", event.getMember().getAsMention())
                    .replace("{member.tag}", event.getMember().getUser().getAsTag())
                    .replace("{member.name}", event.getMember().getUser().getName())
                    .replace("{member.nick}", event.getMember().getNickname() == null ? event.getMember().getUser().getName() : event.getMember().getNickname())
                    .replace("{server.name}", event.getGuild().getName())
                    .replace("{count}", String.valueOf(event.getGuild().getBoostCount()))).queue();
        }
    }

}