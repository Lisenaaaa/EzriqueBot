package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.impl.GuildConfig;

public class AutoRoleListener extends ListenerBase {

    @SubscribeEvent
    private void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();
        GuildConfig guildConfig = Ezrique.getInstance().getConfigManager().getGuild();
        if (event.getUser().isBot() && guildConfig.getAutoRole().getBots().isAvailable(guildId)) {
            Role role = guild.getRoleById(guildConfig.getAutoRole().getBots().getRole(guildId));
            if (role != null) {
                guild.addRoleToMember(event.getUser().getId(), role).reason("AutoRole feature").queue();
            }
        } else if (guildConfig.getAutoRole().getMembers().isAvailable(guildId)) {
            Role role = guild.getRoleById(guildConfig.getAutoRole().getMembers().getRole(guildId));
            if (role != null) {
                guild.addRoleToMember(event.getUser().getId(), role).reason("AutoRole feature").queue();
            }
        }
    }

}