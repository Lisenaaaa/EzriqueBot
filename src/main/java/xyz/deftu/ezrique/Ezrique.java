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
import org.discordbots.api.client.DiscordBotListAPI;
import xyz.deftu.ezrique.commands.CommandManager;
import xyz.deftu.ezrique.commands.impl.*;
import xyz.deftu.ezrique.commands.impl.exclusive.testingserver.TestingServerCommand;
import xyz.deftu.ezrique.commands.impl.TicketCommand;
import xyz.deftu.ezrique.component.ComponentCreator;
import xyz.deftu.ezrique.config.ConfigManager;
import xyz.deftu.ezrique.config.impl.GuildConfig;
import xyz.deftu.ezrique.features.github.GitHubHandler;
import xyz.deftu.ezrique.listeners.*;
import xyz.deftu.ezrique.listeners.TicketMenuListener;
import xyz.deftu.ezrique.mongo.MongoConnection;
import xyz.deftu.ezrique.networking.impl.GitHubEndpoint;
import xyz.deftu.ezrique.networking.NetworkManager;
import xyz.deftu.ezrique.networking.impl.StatsEndpoint;
import xyz.qalcyo.mango.Multithreading;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class Ezrique extends Thread {

    private final Logger logger = LogManager.getLogger("Ezrique");
    private OffsetDateTime startTime;

    private BotMetadata metadata;
    private MongoConnection mongoConnection;
    private ConfigManager configManager;

    private ShardManager api;
    private DiscordBotListAPI dbl;
    private NetworkManager networkManager;

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
        if (!metadata.isBeta()) {
            dbl = new DiscordBotListAPI.Builder()
                    .token(metadata.getDblToken())
                    .botId(api.getShardById(0).getSelfUser().getApplicationId())
                    .build();
            dbl.setStats((int) api.getGuildCache().size());
            Multithreading.schedule(() -> dbl.setStats((int) api.getGuildCache().size()), 30, TimeUnit.MINUTES);
        }

        GitHubHandler.getInstance().initialize();

        networkManager = new NetworkManager();
        networkManager.addEndpoint(new GitHubEndpoint());
        networkManager.addEndpoint(new StatsEndpoint());
        networkManager.initialize();

        commandManager = new CommandManager();
        commandManager.addCommand(new AutoRoleCommand());
        commandManager.addCommand(new EmojiCommand());
        commandManager.addCommand(new GitHubCommand());
        commandManager.addCommand(new HelpCommand());
        commandManager.addCommand(new LeaveMessageCommand());
        commandManager.addCommand(new LinksCommand());
        commandManager.addCommand(new NukeCommand());
        commandManager.addCommand(new RestartCommand());
        commandManager.addCommand(new StatisticsCommand());
        commandManager.addCommand(new TicketCommand());
        commandManager.addCommand(new WelcomeMessageCommand());

        commandManager.addCommand(new TestingServerCommand());
        commandManager.initialize(api);

        listenerManager = new ListenerManager();
        listenerManager.addListener("AUTO_ROLE", new AutoRoleListener());
        listenerManager.addListener("GUILD_ADD_MESSAGE", new GuildAddMessageListener());
        listenerManager.addListener("GUILD_JOIN_LEAVE", new GuildJoinLeaveListener());
        listenerManager.addListener("TICKET_BUTTON", new TicketButtonListener());
        listenerManager.addListener("TICKET_MENU", new TicketMenuListener());
        listenerManager.initialize(api);

        componentCreator = new ComponentCreator(this);

        for (JDA shard : api.getShards()) {
            shard.getPresence().setActivity(Activity.playing(String.format("with Deftu's mind | %s", shard.getShardInfo().getShardString())));
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

    public ShardManager getApi() {
        return api;
    }

    public DiscordBotListAPI getDbl() {
        return dbl;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
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