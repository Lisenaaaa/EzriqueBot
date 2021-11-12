package xyz.deftu.ezrique.config;

import xyz.deftu.ezrique.config.impl.Config;
import xyz.deftu.ezrique.config.impl.children.BotConfig;
import xyz.deftu.ezrique.config.impl.children.GuildConfig;

import java.io.File;

public class ConfigManager {

    private final Config config;
    private final BotConfig bot;
    private final GuildConfig guild;

    public ConfigManager() {
        config = new Config("config", new File("./"));
        config.addChild(bot = new BotConfig());
        config.addChild(guild = new GuildConfig());
    }

    public Config getConfig() {
        return config;
    }

    public BotConfig getBot() {
        return bot;
    }

    public GuildConfig getGuild() {
        return guild;
    }

}