package xyz.deftu.ezrique.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.deftu.ezrique.Ezrique;
import xyz.qalcyo.mango.Lists;

import java.util.Collections;
import java.util.List;

public class CommandManager {

    private final Logger logger = LogManager.getLogger("Ezrique (CommandManager)");
    private final List<ICommand> commands;

    public CommandManager() {
        this.commands = Lists.newArrayList();
    }

    public void initialize(JDA api) {
        CommandListUpdateAction globalUpdateAction = api.updateCommands();
        for (ICommand command : commands) {
            command.initialize(Ezrique.getInstance(), api);

            List<Long> guildIds = command.getGuildIds();
            if (!guildIds.isEmpty()) {
                for (Long guildId : guildIds) {
                    if (guildId != null) {
                        Guild guild = api.getGuildById(guildId);
                        if (guild != null) {
                            guild.upsertCommand(command.getData()).queue();
                        } else {
                            logger.error("Unable to find guild by ID ({}) for command {}.", guildId, command.getData().getName());
                        }
                    }
                }
            } else {
                globalUpdateAction.addCommands(command.getData());
            }
        }

        globalUpdateAction.queue();
        logger.info("Initialized command manager with {} command(s).", commands.size());
    }

    @SubscribeEvent
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        commands.stream().filter(command -> command.getData().getName().equals(event.getName())).findFirst().ifPresent(command -> command.execute(Ezrique.getInstance(), event));
    }

    public void addCommand(ICommand command) {
        commands.add(command);
    }

    public List<ICommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

}