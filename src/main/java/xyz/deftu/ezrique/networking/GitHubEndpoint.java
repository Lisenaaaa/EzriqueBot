package xyz.deftu.ezrique.networking;

import spark.Request;
import spark.Response;
import xyz.qalcyo.json.entities.JsonElement;
import xyz.qalcyo.json.entities.JsonObject;
import xyz.qalcyo.json.parser.JsonParser;
import xyz.qalcyo.json.util.JsonHelper;

public class GitHubEndpoint implements IEndpoint {

    public EndpointType getType() {
        return EndpointType.POST;
    }

    public String getName() {
        return "github";
    }

    public Object handle(Request request, Response response) {
        try {
            JsonElement element = JsonParser.parse(request.body());
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                System.out.println(JsonHelper.makePretty(object));
            }

            return 200;
        } catch (Exception e) {
            return 500;
        }
    }

}