package xyz.deftu.ezrique.features.github;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.deftu.ezrique.Ezrique;
import xyz.qalcyo.json.entities.JsonObject;

public interface IGitHubProcessor {
    EmbedBuilder process(Ezrique instance, JsonObject object, Guild guild);
}