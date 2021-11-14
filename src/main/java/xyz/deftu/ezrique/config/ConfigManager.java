package xyz.deftu.ezrique.config;

import xyz.deftu.ezrique.config.impl.BotConfig;
import xyz.deftu.ezrique.config.impl.GuildConfig;

public class ConfigManager {

    private final BotConfig bot;
    private final GuildConfig guild;

    public ConfigManager() {
        bot = new BotConfig();
        guild = new GuildConfig();
    }

    public BotConfig getBot() {
        return bot;
    }

    public GuildConfig getGuild() {
        return guild;
    }

}