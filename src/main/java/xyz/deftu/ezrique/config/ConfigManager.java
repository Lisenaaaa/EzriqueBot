package xyz.deftu.ezrique.config;

import xyz.deftu.ezrique.config.impl.Config;
import xyz.deftu.ezrique.config.impl.children.BotConfig;

import java.io.File;

public class ConfigManager {

    private final Config config;
    private final BotConfig bot;

    public ConfigManager() {
        config = new Config("config", new File("./"));
        config.addChild(bot = new BotConfig());
    }

    public Config getConfig() {
        return config;
    }

    public BotConfig getBot() {
        return bot;
    }

}