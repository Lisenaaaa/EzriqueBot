package xyz.deftu.ezrique.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.deftu.ezrique.Ezrique;
import xyz.qalcyo.mango.Lists;

import java.util.*;

public class CommandManager {

    private final Logger logger = LogManager.getLogger("Ezrique (CommandManager)");
    private final List<ICommand> commands;

    private final Map<Long, List<ICommand>> awaitingUpsertion = new HashMap<>();

    public CommandManager() {
        this.commands = Lists.newArrayList();
    }

    public void initialize(ShardManager api) {
        api.addEventListener(this);

        CommandListUpdateAction globalUpdateAction = api.getShardById(0).updateCommands();
        for (ICommand command : commands) {
            command.initialize(Ezrique.getInstance(), api);

            List<Long> guildIds = command.getGuildIds();
            if (guildIds != null && !guildIds.isEmpty()) {
                for (Long guildId : guildIds) {
                    if (guildId != null) {
                        awaitingUpsertion.putIfAbsent(guildId, new ArrayList<>());
                        awaitingUpsertion.get(guildId).add(command);
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
    private void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();
        long id = guild.getIdLong();
        if (awaitingUpsertion.containsKey(id)) {
            CommandListUpdateAction guildUpdateAction = guild.updateCommands();
            for (ICommand command : awaitingUpsertion.get(id)) {
                guildUpdateAction.addCommands(command.getData());
            }

            guildUpdateAction.queue();
        }
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