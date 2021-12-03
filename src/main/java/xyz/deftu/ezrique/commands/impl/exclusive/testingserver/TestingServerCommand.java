package xyz.deftu.ezrique.commands.impl.exclusive.testingserver;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandImpl;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class TestingServerCommand extends CommandImpl {

    public CommandData getData() {
        return new CommandData("testingserver", "Test.");
    }

    public void initialize(Ezrique instance, ShardManager api) {
        addGuildId(690263476089782428L); /* Deftu's Bot Testing */
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_ROLES, Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL, Permission.MESSAGE_SEND))).queue();
    }

}