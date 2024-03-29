package xyz.deftu.ezrique;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BotMetadata {

    private JsonObject object;

    private String token;
    private String mongoUsername;
    private String mongoPassword;

    private String ownerId;

    private String primaryGuildId;
    private String errorLogId;

    private String dblToken;

    public void initialize() {
        try {
            List<String> lines = IOUtils.readLines(new FileInputStream("./metadata.json"), StandardCharsets.UTF_8);
            StringBuilder content = new StringBuilder();
            for (String line : lines) {
                content.append(line);
            }

            object = new JsonParser().parse(content.toString()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        token = getAndCheck("token").getAsString();
        mongoUsername = getAndCheck("mongo_username").getAsString();
        mongoPassword = getAndCheck("mongo_password").getAsString();

        ownerId = getAndCheck("owner_id").getAsString();

        primaryGuildId = getAndCheck("primary_guild_id").getAsString();
        errorLogId = getAndCheck("error_log_id").getAsString();

        dblToken = getAndCheck("dbl_token").getAsString();
    }

    private JsonElement getAndCheck(String key) {
        if (!object.has(key))
            throw new IllegalStateException(key + " was not found, but it's required!");
        return object.get(key);
    }

    public String getToken() {
        return token;
    }

    public String getMongoUsername() {
        return mongoUsername;
    }

    public String getMongoPassword() {
        return mongoPassword;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPrimaryGuildId() {
        return primaryGuildId;
    }

    public String getErrorLogId() {
        return errorLogId;
    }

    public String getDblToken() {
        return dblToken;
    }

    public boolean isBeta() {
        return Ezrique.getInstance().getApi().getShardById(0).getSelfUser().getName().toLowerCase().endsWith("beta");
    }

    public Color getPrimaryColour() {
        return isBeta() ? new Color(63, 56, 232) : new Color(153, 0, 0);
    }

}