package xyz.deftu.ezrique;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deftu.ezrique.commands.CommandManager;
import xyz.deftu.ezrique.commands.impl.*;
import xyz.deftu.ezrique.component.ComponentCreator;
import xyz.deftu.ezrique.config.ConfigManager;
import xyz.deftu.ezrique.listeners.GuildAddMessageListener;
import xyz.deftu.ezrique.listeners.GuildBoostListener;
import xyz.deftu.ezrique.listeners.ListenerManager;

import java.awt.*;

public class Ezrique extends Thread {

    /* Core. */
    private final Logger logger = LogManager.getLogger("Ezrique");
    private ConfigManager configManager;
    private CommandManager commandManager;
    private JDA api;
    private ListenerManager listenerManager;
    private ComponentCreator componentCreator;

    public void initialize() throws Exception {
        logger.info("Starting...");
        configManager = new ConfigManager();

        commandManager = new CommandManager();
        commandManager.addCommand(new BoostMessageCommand());
        commandManager.addCommand(new EmojiCommand());
        commandManager.addCommand(new HelpCommand());
        commandManager.addCommand(new InviteCommand());
        commandManager.addCommand(new LeaveMessageCommand());
        commandManager.addCommand(new PingCommand());
        commandManager.addCommand(new RestartCommand());
        commandManager.addCommand(new WelcomeMessageCommand());

        api = JDABuilder.createDefault(configManager.getBot().getToken())
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(commandManager)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
        api.awaitReady();
        logger.info("Connected as {}!", api.getSelfUser().getAsTag());
        commandManager.initialize(api);

        listenerManager = new ListenerManager();
        listenerManager.addListener("GUILD_ADD_MESSAGE", new GuildAddMessageListener());
        listenerManager.addListener("GUILD_BOOST", new GuildBoostListener());
        listenerManager.initialize(api);

        componentCreator = new ComponentCreator(this);

        Runtime.getRuntime().addShutdownHook(new Thread(this::kill, "Ezrique shutdown"));
    }

    public void kill() {
        api.shutdown();
        stop();
    }

    public boolean isBeta() {
        return api.getSelfUser().getName().toLowerCase().endsWith("beta");
    }

    public Color getPrimaryColour() {
        return isBeta() ? new Color(63, 56, 232) : new Color(153, 0, 0);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public JDA getApi() {
        return api;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public ComponentCreator getComponentCreator() {
        return componentCreator;
    }

    public static Ezrique getInstance() {
        return Bootstrap.getBot();
    }

    /* Thread implementation. */

    public synchronized void start() {
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}