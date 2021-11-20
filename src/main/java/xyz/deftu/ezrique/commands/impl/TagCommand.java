package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.TextHelper;

public class TagCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("tag", "Gateway to tag commands.")
                .addSubcommands(
                        generateCreate("create"),
                        generateCreate("new"),
                        new SubcommandData("delete", "Deletes a tag.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "name", "The name of the tag to be deleted.", true)
                                ),
                        new SubcommandData("edit", "Edit the content of a tag.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "name", "The name of the tag to be edited.", true),
                                        new OptionData(OptionType.STRING, "content", "The new content of this tag.", true)
                                ),
                        new SubcommandData("list", "Lists all tags in the guild."),
                        new SubcommandData("retrieve", "Sends the content of a tag.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "name", "The name of the tag to send.")
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            assert event.getSubcommandName() != null;
            switch (event.getSubcommandName()) {
                case "create":
                case "new":
                    OptionMapping ephemeralMapping = event.getOption("ephemeral");
                    handleCreate(instance, event, event.getOption("name").getAsString(), event.getOption("content").getAsString(), ephemeralMapping == null ? false : ephemeralMapping.getAsBoolean());
                    break;
            }
        } else {
            event.reply(TextHelper.buildFailure("This command can only be ran in servers!")).queue();
        }
    }

    private void handleCreate(Ezrique instance, SlashCommandEvent event, String name, String content, boolean ephemeral) {

    }

    private static SubcommandData generateCreate(String name) {
        return new SubcommandData(name, "Creates a new tag.")
                .addOptions(
                        new OptionData(OptionType.STRING, "name", "The name of this tag.", true),
                        new OptionData(OptionType.STRING, "content", "The content of this new tag.", true),
                        new OptionData(OptionType.BOOLEAN, "ephemeral", "Whether this tag will be \"secret\" or not.")
                );
    }

}