package xyz.deftu.ezrique.networking.impl;

import net.dv8tion.jda.api.JDA;
import spark.Request;
import spark.Response;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.EzriqueInfo;
import xyz.deftu.ezrique.networking.EndpointType;
import xyz.deftu.ezrique.networking.IEndpoint;
import xyz.qalcyo.json.entities.JsonArray;
import xyz.qalcyo.json.entities.JsonObject;

public class StatsEndpoint implements IEndpoint {

    public EndpointType getType() {
        return EndpointType.GET;
    }

    public String getName() {
        return "stats";
    }

    public Object handle(Request request, Response response) {
        Ezrique instance = Ezrique.getInstance();
        JsonObject object = new JsonObject();

        object.add("bot", new JsonObject()
                .add("version", EzriqueInfo.VERSION)
                .add("commands", instance.getCommandManager().getCommands().size())
                .add("listeners", instance.getListenerManager().getListeners().size())
                .add("start_time", instance.getStartTime().toEpochSecond()));
        object.add("global", new JsonObject()
                .add("guilds", instance.getApi().getGuildCache().size())
                .add("users", instance.getApi().getUserCache().size())
                .add("shards", instance.getApi().getShardCache().size()));
        JsonArray shards = new JsonArray();
        for (JDA shard : instance.getApi().getShardCache()) {
            shards.add(new JsonObject()
                    .add("id", shard.getShardInfo().getShardId())
                    .add("guilds", shard.getGuildCache().size())
                    .add("users", shard.getUserCache().size()));
        }
        object.add("shards", shards);

        return object.getAsString();
    }

}