package xyz.deftu.ezrique.commands.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.ICommand;
import xyz.deftu.ezrique.features.TicketHandler;
import xyz.deftu.ezrique.util.EmojiHelper;
import xyz.deftu.ezrique.util.IdentificationHelper;
import xyz.deftu.ezrique.util.PermissionHelper;
import xyz.deftu.ezrique.util.TextHelper;

public class TicketCommand implements ICommand {

    public CommandData getData() {
        return new CommandData("ticket", "Base ticket command.")
                .addSubcommands(
                        new SubcommandData("new", "Opens a new ticket.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "reason", "The reason this ticket was opened.")
                                ),
                        new SubcommandData("create", "Creates a new ticket.")
                                .addOptions(
                                new OptionData(OptionType.STRING, "reason", "The reason this ticket was opened.")
                        ),
                        new SubcommandData("close", "Closes the ticket this command is run in.")
                                .addOptions(
                                new OptionData(OptionType.STRING, "reason", "The reason this ticket was closed.")
                        ),
                        new SubcommandData("add", "Adds a user to the current ticket.")
                                .addOptions(
                                        new OptionData(OptionType.USER, "user", "The user to add to this ticket.", true)
                        ),
                        new SubcommandData("menu", "Creates a ticket menu.")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "content", "The content inside the ticket menu.", true),
                                        new OptionData(OptionType.STRING, "name", "The name of the ticket menu.")
                        )
                )
                .addSubcommandGroups(
                        new SubcommandGroupData("config", "Allows you to change the configuration of your server's tickets.")
                                .addSubcommands(
                                        new SubcommandData("toggle", "Whether this feature will be available in your server or not.")
                                                .addOptions(
                                                        new OptionData(OptionType.BOOLEAN, "value", "The toggle of this feature.", true)
                                                ),
                                        new SubcommandData("name", "The name of the ticket channels created.")
                                                .addOptions(
                                                        new OptionData(OptionType.STRING, "name", "The name of newly created ticket channels.")
                                                ),
                                        new SubcommandData("role", "A role that is notified when a ticket is opened.")
                                                .addOptions(
                                                        new OptionData(OptionType.ROLE, "role", "A role notified when a ticket is opened.", true)
                                                ),
                                        new SubcommandData("category", "The category tickets will be created in.")
                                                .addOptions(
                                                        new OptionData(OptionType.CHANNEL, "category", "The category tickets will be made in.", true)
                                                )
                                )
                );
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        if (event.isFromGuild()) {
            OptionMapping reasonMapping = event.getOption("reason");
            switch (event.getSubcommandName()) {
                case "new":
                case "create":
                    handleCreate(instance, event, reasonMapping == null ? "No reason provided." : reasonMapping.getAsString());
                    break;
                case "close":
                    handleClose(instance, event, reasonMapping == null ? "No reason provided." : reasonMapping.getAsString());
                    break;
                case "add":
                    handleAdd(instance, event, event.getOption("user").getAsUser());
                    break;
                case "menu":
                    OptionMapping nameMapping = event.getOption("name");
                    handleMenu(instance, event, nameMapping == null ? null : nameMapping.getAsString(), event.getOption("content").getAsString());
                    break;
                case "toggle":
                    handleToggle(instance, event, event.getOption("value").getAsBoolean());
                    break;
                case "name":
                    nameMapping = event.getOption("name");
                    handleName(instance, event, nameMapping == null ? null : nameMapping.getAsString());
                    break;
                case "role":
                    handleRole(instance, event, event.getOption("role").getAsRole());
                    break;
                case "category":
                    handleCategory(instance, event, event.getOption("category").getAsGuildChannel());
                    break;
            }
        } else {
            event.reply(TextHelper.failure("This command can only be ran in servers!")).queue();
        }
    }

    private void handleCreate(Ezrique instance, SlashCommandEvent event, String reason) {
        TicketHandler.getInstance().confirmOpen(event.getGuild(), event.getMember(), reason, event.deferReply());
    }

    private void handleClose(Ezrique instance, SlashCommandEvent event, String reason) {
        TicketHandler.getInstance().confirmClose(event.getGuild(), event.getTextChannel(), event.getMember(), reason, event.deferReply());
    }

    private void handleAdd(Ezrique instance, SlashCommandEvent event, User user) {
        TextChannel channel = event.getTextChannel();
        String topic = channel.getTopic();
        if (topic != null && topic.startsWith("Ticket created by")) {
            Member userMember = event.getGuild().getMember(user);
            if (userMember != null) {
                Member author = event.getGuild().getMemberById(topic.substring(channel.getTopic().lastIndexOf("(")).replace("(", "").replace(")", ""));
                Member member = event.getMember();

                String supportRole = instance.getConfigManager().getGuild().getTickets().getRole(event.getGuild().getId());
                boolean support = supportRole != null && member.getRoles().contains(event.getGuild().getRoleById(supportRole));

                if (member.getId().equals(author.getId()) || support || member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.MANAGE_ROLES) || member.hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.putPermissionOverride(event.getGuild().getMember(user)).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_SEND).queue();
                    event.reply(TextHelper.success("Successfully added **" + user.getAsTag() + "** to this ticket.")).queue();
                } else {
                    event.reply(TextHelper.failure("You don't have permission to use this command.")).queue();
                }
            } else {
                event.reply(TextHelper.failure("Failed to find member.")).queue();
            }
        } else {
            event.reply(TextHelper.failure("This does not appear to be a ticket.")).queue();
        }
    }

    private void handleMenu(Ezrique instance, SlashCommandEvent event, String name, String content) {
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            content = content.replace("\\n", "\n");
            if (content.length() > 2000) {
                event.reply("The content of a ticket menu cannot be longer than 2000 characters.").setEphemeral(true).queue();
            } else {
                EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());
                if (name != null) {
                    embedBuilder.setTitle(name);
                }

                embedBuilder.setDescription(content);
                ActionRow actionRow = ActionRow.of(
                        Button.danger("ticketmenu|" + event.getChannel().getId(), "Open a ticket").withEmoji(EmojiHelper.fromUnicode("\uD83C\uDFAB"))
                );

                event.getChannel().sendMessage(new MessageBuilder().setEmbeds(embedBuilder.build()).setActionRows(actionRow).build()).queue();
                event.deferReply().complete().deleteOriginal().queue();
            }
        } else {
            event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.ADMINISTRATOR))).queue();
        }
    }

    private void handleToggle(Ezrique instance, SlashCommandEvent event, boolean toggle) {
        if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            instance.getConfigManager().getGuild().getTickets().setToggle(event.getGuild().getId(), toggle);
            event.reply(TextHelper.success("Successfully set ticket toggle.")).setEphemeral(true).queue();
        } else {
            event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_SERVER))).queue();
        }
    }

    private void handleName(Ezrique instance, SlashCommandEvent event, String name) {
        if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            if (name == null) {
                StringBuilder variableBuilder = new StringBuilder();
                variableBuilder.append("{name}: ").append(event.getMember().getUser().getName().replaceAll("\\P{Print}", "").replaceAll(" ", "-")).append("\n");
                variableBuilder.append("{id}: ").append(event.getMember().getId()).append("\n");
                variableBuilder.append("{uuid}: ").append(IdentificationHelper.generateUuid());

                EmbedBuilder embedBuilder = instance.getComponentCreator().createEmbed(event.getJDA());
                if (instance.getConfigManager().getGuild().getTickets().isAvailable(event.getGuild().getId()))
                    embedBuilder.addField("Current name", instance.getConfigManager().getGuild().getTickets().getName(event.getGuild().getId()), false);
                embedBuilder.addField("Variables", variableBuilder.toString(), false);
                event.reply(new MessageBuilder()
                        .setEmbeds(embedBuilder.build())
                        .build()).setEphemeral(true).queue();
            } else {
                instance.getConfigManager().getGuild().getTickets().setName(event.getGuild().getId(), name);
                event.reply(TextHelper.success("Successfully set ticket name.")).setEphemeral(true).queue();
            }
        } else {
            event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_SERVER))).queue();
        }
    }

    private void handleRole(Ezrique instance, SlashCommandEvent event, Role role) {
        if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            instance.getConfigManager().getGuild().getTickets().setRole(event.getGuild().getId(), role.getId());
            event.reply(TextHelper.success("Successfully set ticket role.")).setEphemeral(true).queue();
        } else {
            event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_ROLES))).queue();
        }
    }

    private void handleCategory(Ezrique instance, SlashCommandEvent event, GuildChannel channel) {
        if (event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            String id;
            if (channel instanceof ICategorizableChannel) {
                id = ((ICategorizableChannel) channel).getParentCategoryId();
            } else if (channel instanceof Category) {
                id = channel.getId();
            } else {
                event.reply(TextHelper.failure("That channel is invalid!")).setEphemeral(true).queue();
                return;
            }

            instance.getConfigManager().getGuild().getTickets().setCategory(event.getGuild().getId(), id);
            event.reply(TextHelper.success("Successfully set ticket category.")).setEphemeral(true).queue();
        } else {
            event.reply(TextHelper.failure(PermissionHelper.getInvalidPermissionsMessage(Permission.MANAGE_CHANNEL))).queue();
        }
    }

}