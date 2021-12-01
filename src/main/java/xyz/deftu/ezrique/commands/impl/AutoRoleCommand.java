package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class AutoRoleCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("autorole", "Adds roles to members on join.")
                .addSubcommands(
                        new SubcommandData("members", "The autorole for standard users.")
                                .addOptions(
                                        new OptionData(OptionType.ROLE, "role", "The role to add.", true),
                                        new OptionData(OptionType.BOOLEAN, "toggle", "The toggle for this feature.")
                                ),
                        new SubcommandData("bots", "The autorole for bots.")
                                .addOptions(
                                        new OptionData(OptionType.ROLE, "role", "The role to add.", true),
                                        new OptionData(OptionType.BOOLEAN, "toggle", "The toggle for this feature.")
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
                boolean bots = event.getSubcommandName().equals("bots");
                Role role = event.getOption("role").getAsRole();

                OptionMapping toggleMapping = event.getOption("toggle");
                boolean toggle = toggleMapping != null && toggleMapping.getAsBoolean();

                if (event.getGuild().getBotRole().canInteract(role)) {
                    updateRole(instance, event.getGuild(), role, bots);
                    if (toggleMapping != null) {
                        updateToggle(instance, event.getGuild(), toggle, bots);
                    }

                    event.reply(TextHelper.buildSuccess("Autorole is " + (toggle ? "on" : "off") + ", new users will " + (toggle ? "receive" : "no longer") + " receive " + role.getAsMention() + " upon join.")).setEphemeral(true).queue();
                } else {
                    event.reply(TextHelper.buildFailure("I don't have permission to give that role to members!")).queue();
                }
            } else {
                event.reply(TextHelper.buildFailure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_ROLES))).queue();
            }
        } else {
            event.reply(TextHelper.buildFailure("This command can only be ran in servers!")).queue();
        }
    }

    private void updateRole(Ezrique instance, Guild guild, Role role, boolean bots) {
        if (bots) {
            instance.getConfigManager().getGuild().getAutoRole().getBots().setRole(guild.getId(), role.getId());
        } else {
            instance.getConfigManager().getGuild().getAutoRole().getMembers().setRole(guild.getId(), role.getId());
        }
    }

    private void updateToggle(Ezrique instance, Guild guild, boolean toggle, boolean bots) {
        if (bots) {
            instance.getConfigManager().getGuild().getAutoRole().getBots().setToggle(guild.getId(), toggle);
        } else {
            instance.getConfigManager().getGuild().getAutoRole().getMembers().setToggle(guild.getId(), toggle);
        }
    }

}