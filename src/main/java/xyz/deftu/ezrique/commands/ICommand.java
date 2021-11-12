package xyz.deftu.ezrique.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;

import java.util.ArrayList;
import java.util.List;

public interface ICommand {
    CommandData data();

    default void initialize(Ezrique instance, JDA api) {
    }

    void execute(Ezrique instance, SlashCommandEvent event);

    default List<Long> getGuildIds() {
        return new ArrayList<>();
    }

    default void addGuildId(long id) {
        List<Long> guildIds = getGuildIds();
        if (guildIds != null && !guildIds.contains(id)) {
            guildIds.add(id);
        }
    }
}