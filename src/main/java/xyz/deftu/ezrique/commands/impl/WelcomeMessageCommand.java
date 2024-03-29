package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class WelcomeMessageCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("welcomemessage", "Allows you to set a welcome message that is sent when a member joins your server.")
                .addOptions(
                        new OptionData(OptionType.BOOLEAN, "toggle", "Whether this feature should be enabled or not."),
                        new OptionData(OptionType.STRING, "message", "The message sent when someone joins your server."),
                        new OptionData(OptionType.CHANNEL, "channel", "The channel the joins message is sent to.")
                                .setChannelTypes(ChannelType.TEXT)
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                OptionMapping toggleMapping = event.getOption("toggle");
                boolean toggle = toggleMapping != null && toggleMapping.getAsBoolean() || instance.getConfigManager().getGuild().getWelcomeChannel().isAvailable(event.getGuild().getId());
                OptionMapping messageMapping = event.getOption("message");
                String message = messageMapping == null ? instance.getConfigManager().getGuild().getWelcomeChannel().getMessage(event.getGuild().getId()) : messageMapping.getAsString();
                OptionMapping channelMapping = event.getOption("channel");
                GuildChannel channel = channelMapping == null ? null : channelMapping.getAsGuildChannel();
                if (channel == null) {
                    String current = instance.getConfigManager().getGuild().getWelcomeChannel().getChannel(event.getGuild().getId());
                    if (current != null) {
                        channel = event.getJDA().getGuildChannelById(current);
                    }
                }

                Message reply = null;

                if (toggleMapping != null) {
                    instance.getConfigManager().getGuild().getWelcomeChannel().setToggle(event.getGuild().getId(), toggle);
                }

                if (channelMapping != null) {
                    if (channel instanceof TextChannel) {
                        instance.getConfigManager().getGuild().getWelcomeChannel().setChannel(event.getGuild().getId(), channel.getId());
                    }
                }

                if (messageMapping == null) {
                        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());
                        if (instance.getConfigManager().getGuild().getWelcomeChannel().isAvailable(event.getGuild().getId()))
                            embedBuilder.addField("Current message", instance.getConfigManager().getGuild().getWelcomeChannel().getMessage(event.getGuild().getId()), false);
                        embedBuilder.addField("Variables", retrieveVariables(event), false);
                        reply = new MessageBuilder().setEmbeds(embedBuilder.build()).build();
                } else {
                    instance.getConfigManager().getGuild().getWelcomeChannel().setMessage(event.getGuild().getId(), message.replace("\\n", "\n"));
                }

                event.reply(reply == null ?
                        new MessageBuilder(TextHelper.success("Welcome messages are " + (toggle ? "on" : "off") + "," +
                                " sent to " + (channel == null ? "none" : channel.getAsMention()) + " and " +
                                "the messages will be \"" + message + "\"."))
                                .build() :
                        reply).setEphemeral(true).queue();
            } else {
                event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_SERVER))).queue();
            }
        } else {
            event.reply("This command can only be used in servers.").queue();
        }
    }

    private String retrieveVariables(SlashCommandEvent event) {
        StringBuilder builder = new StringBuilder();

        builder.append("{member.mention}: ").append(event.getMember().getAsMention()).append("\n");
        builder.append("{member.tag}: ").append(event.getMember().getUser().getAsTag()).append("\n");
        builder.append("{member.name}: ").append(event.getMember().getUser().getName()).append("\n");
        builder.append("{member.nick}: ").append(event.getMember().getNickname() == null ? event.getMember().getUser().getName() : event.getMember().getNickname()).append("\n");
        builder.append("{server.name}: ").append(event.getGuild().getName()).append("\n");
        builder.append("{server.members}: ").append(event.getGuild().getMemberCount());

        return builder.toString();
    }

}