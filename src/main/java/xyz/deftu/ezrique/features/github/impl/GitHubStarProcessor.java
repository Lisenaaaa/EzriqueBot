package xyz.deftu.ezrique.features.github.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.features.github.IGitHubProcessor;
import xyz.qalcyo.json.entities.JsonElement;
import xyz.qalcyo.json.entities.JsonObject;

import java.time.OffsetDateTime;

public class GitHubStarProcessor implements IGitHubProcessor {

    public EmbedBuilder process(Ezrique instance, JsonObject object, Guild guild) {
        EmbedBuilder value = new EmbedBuilder();

        JsonObject repository = object.getAsObject("repository");
        JsonObject sender = object.getAsObject("sender");

        value.setAuthor(sender.getAsString("login"), sender.getAsString("url"), sender.getAsString("avatar_url"));
        value.setTitle(repository.getAsString("full_name"), repository.getAsString("html_url"));
        value.appendDescription(object.getAsString("starred_at").equals("null") ? "Star removed." : "New star added!");

        return value;
    }

}