package xyz.deftu.ezrique.features.github.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.features.github.IGitHubProcessor;
import xyz.deftu.ezrique.util.TextHelper;
import xyz.qalcyo.json.entities.JsonArray;
import xyz.qalcyo.json.entities.JsonElement;
import xyz.qalcyo.json.entities.JsonObject;

import java.time.OffsetDateTime;

public class GitHubCommitProcessor implements IGitHubProcessor {

    public EmbedBuilder process(Ezrique instance, JsonObject object, Guild guild) {
        EmbedBuilder value = new EmbedBuilder();

        JsonObject repository = object.getAsObject("repository");
        JsonObject sender = object.getAsObject("sender");
        JsonArray commits = object.getAsArray("commits");

        value.setTitle(repository.getAsString("full_name"), repository.getAsString("html_url"));
        value.setAuthor(sender.getAsString("login"), sender.getAsString("html_url"), sender.getAsString("avatar_url"));
        for (JsonElement element : commits) {
            boolean last = commits.indexOf(element) == commits.size();
            if (element.isJsonObject()) {
                JsonObject commit = element.getAsJsonObject();

                JsonObject committer = commit.getAsObject("committer");
                String id = commit.getAsString("id").substring(0, 7);
                String message = commit.getAsString("message");
                String url = commit.getAsString("url");
                OffsetDateTime timestamp = OffsetDateTime.parse(commit.getAsString("timestamp"));

                value.appendDescription("[").appendDescription(TextHelper.dateTime(timestamp.toEpochSecond())).appendDescription(" - ")
                        .appendDescription("[").appendDescription(id).appendDescription("](").appendDescription(url).appendDescription(")").appendDescription("] ")
                        .appendDescription(message).appendDescription(" - ").appendDescription(committer.getAsString("username"));
                if (!last) {
                    value.appendDescription("\n");
                }
            }
        }

        return value;
    }

}