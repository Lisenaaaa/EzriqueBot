package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;

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
                OptionMapping messageMapping = event.getOption("message");
                OptionMapping channelMapping = event.getOption("channel");

                String reply = "";
                boolean shouldReply = true;

                if (toggleMapping != null) {
                    boolean toggle = toggleMapping.getAsBoolean();
                    instance.getConfigManager().getGuild().setWelcomeMessageToggle(event.getGuild().getId(), toggle);
                    reply += "Changed welcome message toggle to " + (toggle ? "on" : "off") + ".";
                }

                if (channelMapping != null) {
                    GuildChannel channel = channelMapping.getAsGuildChannel();
                    if (channel instanceof TextChannel) {
                        instance.getConfigManager().getGuild().setWelcomeChannel(event.getGuild().getId(), channel.getId());

                        if (!reply.isEmpty())
                            reply += "\n\n";
                        reply += "Set welcome channel to " + channel.getAsMention() + ".";
                    } else {
                        if (!reply.isEmpty())
                            reply += "\n\n";
                        reply += "Did not set channel because it wasn't a text channel.";
                    }
                }

                if (messageMapping == null) {
                    if (reply.isEmpty()) {
                        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());
                        if (instance.getConfigManager().getGuild().hasWelcomeMessage(event.getGuild().getId()))
                            embedBuilder.addField("Current message", instance.getConfigManager().getGuild().getWelcomeMessage(event.getGuild().getId()), false);
                        embedBuilder.addField("Variables", retrieveVariables(event), false);
                        event.reply(new MessageBuilder().setEmbeds(embedBuilder.build()).build()).setEphemeral(true).queue();
                        shouldReply = false;
                    }
                } else {
                    String message = messageMapping.getAsString().replace("\\n", "\n");
                    instance.getConfigManager().getGuild().setWelcomeMessage(event.getGuild().getId(), message);
                    if (!reply.isEmpty())
                        reply += "\n\n";
                    reply += "Set welcome message to:\n" + message;
                }

                if (shouldReply) {
                    event.reply(reply).setEphemeral(true).queue();
                }
            } else {
                event.reply("Only members with the `Manage Server` permission can use this.").queue();
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