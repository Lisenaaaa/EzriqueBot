package xyz.deftu.ezrique.commands.impl.exclusive.testingserver;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandImpl;

public class TestingServerCommand extends CommandImpl {

    public CommandData getData() {
        return new CommandData("testingserver", "Test.");
    }

    public void initialize(Ezrique instance, ShardManager api) {
        addGuildId(690263476089782428L); /* Deftu's Bot Testing */
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        try {
            throw new IllegalAccessException("Imagine, fucking dumbass trying to use this command LMFAOOO");
        } catch (Exception e) {
            if (!instance.getErrorHandler().handle(e)) {
                e.printStackTrace();
            }
        }
    }

}