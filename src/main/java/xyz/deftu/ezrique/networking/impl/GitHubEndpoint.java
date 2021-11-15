package xyz.deftu.ezrique.networking.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;
import xyz.deftu.ezrique.features.github.GitHubHandler;
import xyz.deftu.ezrique.networking.EndpointType;
import xyz.deftu.ezrique.networking.IEndpoint;
import xyz.qalcyo.json.entities.JsonElement;
import xyz.qalcyo.json.parser.JsonParser;

public class GitHubEndpoint implements IEndpoint {

    private final Logger logger = LogManager.getLogger("Ezrique (GitHubEndpoint)");

    public EndpointType getType() {
        return EndpointType.POST;
    }

    public String getName() {
        return "github";
    }

    public Object handle(Request request, Response response) {
        try {
            long id = request.queryMap("id").longValue();
            JsonElement element = JsonParser.parse(request.body());
            if (element.isJsonObject()) {
                GitHubHandler.getInstance().handle(element.getAsJsonObject(), id);
                return 200;
            } else {
                logger.warn("GitHub Endpoint sent an invalid request!");
                return 500;
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred handling GitHub POST.", e);
            return 500;
        }
    }

}