package xyz.deftu.ezrique.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandImpl implements ICommand {

    private final List<Long> guildIds = new ArrayList<>();

    public List<Long> getGuildIds() {
        return guildIds;
    }

    public void addGuildId(long id) {
        if (!guildIds.contains(id)) {
            guildIds.add(id);
        }
    }

}