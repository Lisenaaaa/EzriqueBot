package xyz.deftu.ezrique.features.github;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.features.github.impl.GitHubStarProcessor;
import xyz.deftu.ezrique.features.github.impl.GitHubUnknownProcessor;
import xyz.qalcyo.json.entities.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class GitHubHandler {

    private static final GitHubHandler INSTANCE = new GitHubHandler();
    private final Map<GitHubType, IGitHubProcessor> processors = new HashMap<>();

    public void initialize() {
        addProcessor(GitHubType.UNKNOWN, new GitHubUnknownProcessor());
        addProcessor(GitHubType.STAR, new GitHubStarProcessor());
    }

    public void handle(JsonObject object, long id) {
        Ezrique instance = Ezrique.getInstance();
        String guildId = String.valueOf(id);
        Guild guild = instance.getApi().getGuildById(id);
        if (guild != null) {
            if (instance.getConfigManager().getGuild().hasGitHub(guildId)) {
                TextChannel channel = guild.getTextChannelById(instance.getConfigManager().getGuild().getGitHubChannel(guildId));
                if (channel != null) {
                    GitHubType type = GitHubType.UNKNOWN;
                    MessageBuilder messageBuilder = new MessageBuilder();

                    if (object.hasKey("starred_at"))
                        type = GitHubType.STAR;
                    if (object.hasKey("head_commit"))
                        type = GitHubType.COMMIT;

                    IGitHubProcessor processor = processors.get(type);
                    if (processor != null) {
                        EmbedBuilder processed = processor.process(instance, object, guild);
                        if (processed != null) {
                            channel.sendMessage(messageBuilder.setEmbeds(instance.getComponentCreator().createEmbed(guild.getJDA(), processed).build()).build()).queue();
                        }
                    }
                }
            }
        }
    }

    public void addProcessor(GitHubType type, IGitHubProcessor processor) {
        processors.put(type, processor);
    }

    public static GitHubHandler getInstance() {
        return INSTANCE;
    }

}