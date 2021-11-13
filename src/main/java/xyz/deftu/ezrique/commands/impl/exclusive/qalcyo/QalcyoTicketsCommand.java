package xyz.deftu.ezrique.commands.impl.exclusive.qalcyo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandImpl;
import xyz.deftu.ezrique.util.EmojiHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class QalcyoTicketsCommand extends CommandImpl {

    public CommandData getData() {
        return new CommandData("qalcyotickets", "Creates the Qalcyo exclusive tickets menu.");
    }

    public void initialize(Ezrique instance, ShardManager api) {
        addGuildId(884212442664697937L);
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed();
            embedBuilder.appendDescription("Hello! Click the button below to open a ticket.");
            embedBuilder.appendDescription("\n");
            embedBuilder.appendDescription("We offer support for non-Qalcyo products, however we cannot guarantee that we can help you.");
            embedBuilder.appendDescription("\n");
            embedBuilder.appendDescription("\n");
            embedBuilder.appendDescription("**Please check <#906259339562598420> and <#906259660003217438> before creating a ticket!**");
            embedBuilder.appendDescription("\n");
            embedBuilder.appendDescription("\n");
            embedBuilder.appendDescription("**__Clicking the button on this message will open a ticket between just you, our staff and our support team. Please don't open a ticket unless you actually need support, and remember everyone here also has lives and other things to do.__**");

            ActionRow actionRow = ActionRow.of(
                    Button.danger("qalcyoticket|" + event.getChannel().getId(), "Open a ticket").withEmoji(EmojiHelper.fromUnicode("\uD83C\uDFAB"))
            );

            event.getChannel().sendMessage(new MessageBuilder().setEmbeds(embedBuilder.build()).setActionRows(actionRow).build()).queue();
        } else {
            event.reply(TextHelper.buildFailure("Only members with the `Administrator` permission can use this command.")).queue();
        }
    }

}