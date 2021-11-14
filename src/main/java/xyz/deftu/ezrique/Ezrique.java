package xyz.deftu.ezrique;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deftu.ezrique.commands.CommandManager;
import xyz.deftu.ezrique.commands.impl.*;
import xyz.deftu.ezrique.commands.impl.exclusive.qalcyo.QalcyoTicketsCommand;
import xyz.deftu.ezrique.commands.impl.exclusive.testingserver.TestingServerCommand;
import xyz.deftu.ezrique.commands.impl.tickets.TicketCommand;
import xyz.deftu.ezrique.component.ComponentCreator;
import xyz.deftu.ezrique.config.ConfigManager;
import xyz.deftu.ezrique.config.impl.GuildConfig;
import xyz.deftu.ezrique.listeners.*;
import xyz.deftu.ezrique.listeners.exclusive.qalcyo.QalcyoTicketsListener;
import xyz.deftu.ezrique.mongo.MongoConnection;

import java.time.OffsetDateTime;

public class Ezrique extends Thread {

    private final Logger logger = LogManager.getLogger("Ezrique");
    private OffsetDateTime startTime;

    private BotMetadata metadata;
    private MongoConnection mongoConnection;
    private ConfigManager configManager;

    private ShardManager api;

    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private ComponentCreator componentCreator;

    public void initialize() throws Exception {
        logger.info("Starting...");
        startTime = OffsetDateTime.now();

        metadata = new BotMetadata();
        metadata.initialize();

        mongoConnection = new MongoConnection(metadata.getMongoUsername(), metadata.getMongoPassword());

        configManager = new ConfigManager();
        configManager.addConfiguration(new GuildConfig());
        configManager.initialize(this);

        api = DefaultShardManagerBuilder.createDefault(metadata.getToken())
                .setEventManagerProvider(id -> new AnnotatedEventManager())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        commandManager = new CommandManager();
        commandManager.addCommand(new EmojiCommand());
        commandManager.addCommand(new HelpCommand());
        commandManager.addCommand(new LeaveMessageCommand());
        commandManager.addCommand(new LinksCommand());
        commandManager.addCommand(new NukeCommand());
        commandManager.addCommand(new RestartCommand());
        commandManager.addCommand(new StatisticsCommand());
        commandManager.addCommand(new TicketCommand());
        commandManager.addCommand(new WelcomeMessageCommand());

        commandManager.addCommand(new QalcyoTicketsCommand());
        commandManager.addCommand(new TestingServerCommand());
        commandManager.initialize(api);

        listenerManager = new ListenerManager();
        listenerManager.addListener("GUILD_ADD_MESSAGE", new GuildAddMessageListener());
        listenerManager.addListener("GUILD_JOIN_LEAVE", new GuildJoinLeaveListener());
        listenerManager.addListener("TICKET_BUTTON", new TicketButtonListener());

        listenerManager.addListener("QALCYO_TICKETS", new QalcyoTicketsListener());
        listenerManager.initialize(api);

        componentCreator = new ComponentCreator(this);

        for (JDA shard : api.getShards()) {
            shard.getPresence().setActivity(Activity.playing(String.format("with Deftu's mind | %s/%s", shard.getShardInfo().getShardId(), api.getShardCache().size())));
        }
    }

    public void kill() {
        api.shutdown();
        stop();
    }

    public Logger getLogger() {
        return logger;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public BotMetadata getMetadata() {
        return metadata;
    }

    public MongoConnection getMongoConnection() {
        return mongoConnection;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ShardManager getApi() {
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