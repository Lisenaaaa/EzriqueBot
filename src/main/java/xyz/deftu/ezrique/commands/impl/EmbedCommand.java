package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EmbedCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("embed", "Allows you to create custom embeds using Hastebin.")
                .addOptions(
                        new OptionData(OptionType.STRING, "haste", "The Hastebin URL to use for this embed.")
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                OptionMapping urlMapping = event.getOption("haste");
                if (urlMapping != null) {
                    String url = urlMapping.getAsString();
                    if (UrlValidator.getInstance().isValid(url)) {
                        String domain = getDomain(url);
                        if (domain != null) {
                            domain = domain.toLowerCase();
                            if (domain.contains("hst.sh")) {
                                url = convertRaw(url);
                                String content = getContent(url);
                                if (content != null) {
                                    DataObject data;
                                    try {
                                        data = DataObject.fromJson(content);
                                    } catch (Exception e) {
                                        data = null;
                                    }

                                    if (data != null) {
                                        MessageEmbed embed;
                                        Exception exception = null;
                                        try {
                                            if (!data.hasKey("type"))
                                                data.put("type", "rich");
                                            if (data.hasKey("color") && data.getString("color").startsWith("#"))
                                                data.put("color", Color.decode(data.getString("color")).getRGB());
                                            embed = ((JDAImpl) event.getJDA()).getEntityBuilder().createMessageEmbed(data);
                                        } catch (Exception e) {
                                            exception = e;
                                            embed = null;
                                        }

                                        if (embed != null) {
                                            event.getTextChannel().sendMessageEmbeds(embed).queue();
                                            event.deferReply().complete().deleteOriginal().queue();
                                        } else {
                                            String reason = null;
                                            if (exception != null) {
                                                if (exception.getMessage().startsWith("placeholder")) {
                                                } else {
                                                    reason = exception.getMessage() + " (this is the raw error message)";
                                                }
                                            }

                                            event.reply(TextHelper.failure("Failed to create embed from data provided." + (reason == null ? "" : "\n**Reason:** " + reason))).setEphemeral(true).queue();
                                        }
                                    } else {
                                        event.reply(TextHelper.failure("Failed to get data from the content of that URL.")).setEphemeral(true).queue();
                                    }
                                } else {
                                    event.reply(TextHelper.failure("Failed to get content from that URL.")).setEphemeral(true).queue();
                                }
                            } else {
                                event.reply(TextHelper.failure("This command only supports https://hst.sh/ for now.")).setEphemeral(true).queue();
                            }
                        } else {
                            event.reply(TextHelper.failure("Failed to check domain from that URL.")).setEphemeral(true).queue();
                        }
                    } else {
                        event.reply(TextHelper.failure("That is not a valid URL.")).setEphemeral(true).queue();
                    }
                } else {
                    event.reply("If you want to create an embed using this command, read the official Discord documentation on embed JSON schema.\nhttps://discord.com/developers/docs/resources/channel#embed-object").setEphemeral(true).queue();
                }
            } else {
                event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MESSAGE_MANAGE))).queue();
            }
        } else {
            event.reply(TextHelper.failure("This command can only be ran in servers!")).queue();
        }
    }

    private String getDomain(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            return null;
        }

        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private String convertRaw(String original) {
        if (!original.contains("raw")) {
            String identifier = original.substring(original.lastIndexOf("/"));
            return "https://hst.sh/raw" + identifier;
        } else {
            return original;
        }
    }

    private String getContent(String input) {
        try {
            URL url = new URL(input);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Ezrique (Mozilla Firefox)");
            List<String> lines = IOUtils.readLines(connection.getInputStream(), StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            lines.forEach(builder::append);
            return builder.toString();
        } catch (Exception e) {
            if (!Ezrique.getInstance().getErrorHandler().handle(e)) {
                e.printStackTrace();
            }

            return null;
        }
    }

}