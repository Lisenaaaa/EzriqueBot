package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class EmojiCommand implements ICommand {

    private static final String EMOJI_CDN = "https://cdn.discordapp.com/emojis/";

    public CommandData getData() {
        return new CommandData("emoji", "Makes changes to emojis in a server.")
                .addSubcommands(
                        new SubcommandData("add", "Adds emojis through a variety of ways.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "name", "The name of the emoji you'd like to add.", true),
                                        new OptionData(OptionType.STRING, "emoji", "The emoji you'd like to add.", true)
                                ),
                        new SubcommandData("remove", "Removes an emoji by ID or name.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "identifier", "The name or ID of the emoji you'd like to remove.", true)
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            assert event.getSubcommandName() != null;
            switch (event.getSubcommandName()) {
                case "add":
                    handleAdd(instance, event, event.getOption("name").getAsString(), event.getOption("emoji").getAsString());
                    break;
                case "remove":
                    handleRemove(instance, event, event.getOption("identifier").getAsString());
                    break;
            }
        } else {
            event.reply(TextHelper.buildFailure("This command can only be ran in servers!")).queue();
        }
    }

    private void handleAdd(Ezrique instance, SlashCommandEvent event, String name, String emoji) {
        if (event.getMember().hasPermission(Permission.MANAGE_EMOTES_AND_STICKERS)) {
            try {
                OkHttpClient httpClient = new OkHttpClient();
                Request.Builder httpRequest = new Request.Builder()
                        .get();
                if (emoji.startsWith("<") && emoji.endsWith(">")) {
                    emoji = emoji.substring(emoji.lastIndexOf(":")).replace(":", "").replace(">", "");
                    httpRequest.url(EMOJI_CDN + emoji);
                } else if (emoji.startsWith("http")) {
                    httpRequest.url(emoji);
                } else {
                    event.reply(TextHelper.buildFailure("Unable to fetch emoji from parameter given.")).setEphemeral(true).queue();
                    return;
                }

                Guild guild = event.getGuild();
                Emote emote = guild.createEmote(name, Icon.from(httpClient.newCall(httpRequest.build()).execute().body().byteStream())).complete();
                event.reply(TextHelper.buildSuccess("Successfully added emoji. [" + emote.getAsMention() + "]")).queue();
            } catch (Exception e) {
                event.reply(TextHelper.buildFailure("Failed to add emoji.")).setEphemeral(true).queue();
            }
        } else {
            event.reply(TextHelper.buildFailure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_EMOTES_AND_STICKERS))).queue();
        }
    }

    private void handleRemove(Ezrique instance, SlashCommandEvent event, String identifier) {
        if (event.getMember().hasPermission(Permission.MANAGE_EMOTES_AND_STICKERS)) {
            Guild guild = event.getGuild();
            Emote emote = null;
            if (identifier.startsWith("<") && identifier.endsWith(">")) {
                try {
                    identifier = identifier.substring(identifier.lastIndexOf(":")).replace(":", "").replace(">", "");
                } catch (Exception e) {
                    event.reply(TextHelper.buildFailure("Failed to find emoji.")).setEphemeral(true).queue();
                }
            }

            try {
                emote = guild.getEmoteById(identifier);
            } catch (Exception e) {
                for (Emote found : guild.getEmotesByName(identifier, false)) {
                    if (found.getName().equals(identifier)) {
                        emote = found;
                        break;
                    }
                }
            }

            if (emote != null) {
                emote.delete().queue();
                event.reply(TextHelper.buildSuccess("Successfully removed emoji.")).queue();
            } else {
                event.reply(TextHelper.buildFailure("Failed to find emoji.")).setEphemeral(true).queue();
            }
        } else {
            event.reply(TextHelper.buildFailure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_EMOTES_AND_STICKERS))).queue();
        }
    }

}