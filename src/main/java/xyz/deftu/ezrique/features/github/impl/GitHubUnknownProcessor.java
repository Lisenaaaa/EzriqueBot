package xyz.deftu.ezrique.features.github.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.features.github.IGitHubProcessor;
import xyz.qalcyo.json.entities.JsonObject;
import xyz.qalcyo.json.util.JsonHelper;

public class GitHubUnknownProcessor implements IGitHubProcessor {

    private final Logger logger = LogManager.getLogger("Ezrique (GitHubUnknownProcessor)");

    public EmbedBuilder process(Ezrique instance, JsonObject object, Guild guild) {
        logger.warn("Could not process GitHub payload, delegated to UNKNOWN.\n{}", JsonHelper.makePretty(object));
        return null;
    }

}