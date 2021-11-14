package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.util.EmojiHelper;

public class GuildAddMessageListener extends ListenerBase {

    @SubscribeEvent
    private void onGuildAdd(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (textChannel != null && textChannel.getName().toLowerCase().contains("bot")) {
                textChannel.sendMessage(new MessageBuilder()
                        .setEmbeds(ezrique.getComponentCreator().createEmbed(event.getJDA())
                                .appendDescription("Hey! Thanks for adding me. Use the `/help` command to see everything I have to offer.")
                                .build())
                        .setActionRows(ActionRow.of(
                                Button.secondary("guildadd|" + event.getGuild().getId(), "Remove").withEmoji(EmojiHelper.fromUnicode("\u274C"))
                        ))
                        .build()).queue();
                break;
            }
        }
    }

    @SubscribeEvent
    private void onButtonClick(ButtonClickEvent event) {
        if (event.isFromGuild()) {
            Button component = event.getComponent();
            if (component.getId().startsWith("guildadd")) {
                assert event.getMember() != null;
                if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    event.getMessage().delete().reason("Delete button clicked.").queue();
                } else {
                    event.reply("Only members with the `Manage Messages` permission can remove this message.").setEphemeral(true).queue();
                }
            }
        }
    }

}