package xyz.deftu.ezrique.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.deftu.ezrique.Ezrique;

import java.util.List;

public interface ICommand {

    CommandData getData();

    default void initialize(Ezrique instance, ShardManager api) {
    }

    void execute(Ezrique instance, SlashCommandEvent event);

    default List<Long> getGuildIds() {
        return null;
    }

    default void addGuildId(long id) {
        throw new UnsupportedOperationException();
    }

}