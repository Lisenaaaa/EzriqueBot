package xyz.deftu.ezrique;

import xyz.qalcyo.json.entities.JsonElement;
import xyz.qalcyo.simpleconfig.Configuration;

import java.awt.*;
import java.io.File;

public class BotMetadata {

    private Configuration configuration;

    private String token;
    private String mongoUsername;
    private String mongoPassword;

    private String ownerId;

    private String dblToken;

    public void initialize() {
        configuration = new Configuration("metadata", new File("./"));

        token = getAndCheck("token").getAsString();
        mongoUsername = getAndCheck("mongo_username").getAsString();
        mongoPassword = getAndCheck("mongo_password").getAsString();

        ownerId = getAndCheck("owner_id").getAsString();

        dblToken = getAndCheck("dbl_token").getAsString();
    }

    private JsonElement getAndCheck(String key) {
        if (!configuration.hasKey(key))
            throw new IllegalStateException(key + " was not found, but it's required!");
        return configuration.get(key);
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