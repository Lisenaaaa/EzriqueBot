package xyz.deftu.ezrique.commands.impl.exclusive;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.TextHelper;
import xyz.qalcyo.mango.Strings;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AdminCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("admin", "Gateway to the admin operations. [OWNER ONLY]")
                .addSubcommands(
                        new SubcommandData("gsearch", "Searches through guilds by identifier.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "type", "The type of identifier to search for.", true)
                                                .addChoice("Name", "name")
                                                .addChoice("ID", "id")
                                                .addChoice("Member count", "count"),
                                        new OptionData(OptionType.STRING, "identifier", "The identifier value to use for this search.", true)
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.getUser().getId().equals(instance.getMetadata().getOwnerId())) {
            switch (event.getSubcommandName()) {
                case "gsearch":
                    handleGuildSearch(instance, event, SearchType.of(event.getOption("type").getAsString()), event.getOption("identifier").getAsString());
                    break;
            }
        } else {
            event.reply(TextHelper.failure("Only the bot owner can use this command!")).queue();
        }
    }

    private void handleGuildSearch(Ezrique instance, SlashCommandEvent event, SearchType type, String identifier) {
        JDA api = event.getJDA();
        List<Guild> guilds = api.getGuilds().stream().filter(new GuildSearchPredicate(type, identifier)).collect(Collectors.toList());

        EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(api);
        embedBuilder.setTitle("Guild search");
        for (Guild guild : guilds) {
            int index = guilds.indexOf(guild);
            if (index > 15) {
                break;
            }

            Invite invite = null;
            if (guild.getDefaultChannel() != null) {
                invite = guild.getDefaultChannel().createInvite().complete();
            } else if (guild.getSystemChannel() != null) {
                invite = guild.getSystemChannel().createInvite().complete();
            }

            if (index > 0) {
                embedBuilder.appendDescription("\n");
            }

            embedBuilder.appendDescription("**");
            embedBuilder.appendDescription(invite != null ? "[" : "");
            embedBuilder.appendDescription(guild.getName());
            embedBuilder.appendDescription(invite != null ? "]" : "");
            if (invite != null) {
                embedBuilder.appendDescription("(");
                embedBuilder.appendDescription(invite.getUrl());
                embedBuilder.appendDescription(")");
            }
            embedBuilder.appendDescription("**");
        }

        event.reply(new MessageBuilder()
                .setEmbeds(embedBuilder.build())
                .build()).queue();
    }

    private enum SearchType {
        NAME("name"),
        ID("id"),
        MEMBER_COUNT("count");

        private final String identifier;
        SearchType(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }

        public static SearchType of(String input) {
            for (SearchType value : values()) {
                if (Objects.equals(value.identifier, input)) {
                    return value;
                }
            }

            return null;
        }
    }

    private static class GuildSearchPredicate implements Predicate<Guild> {
        private final SearchType type;
        private final String identifier;

        private GuildSearchPredicate(SearchType type, String identifier) {
            this.type = type;
            this.identifier = identifier;
        }

        public boolean test(Guild guild) {
            return type == SearchType.NAME ? Strings.containsIgnoreCase(guild.getName(), identifier) : (type == SearchType.ID ? guild.getId().contains(identifier) : Objects.equals(guild.getMemberCount(), identifier));
        }
    }

}