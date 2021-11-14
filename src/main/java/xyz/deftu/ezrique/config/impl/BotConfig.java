package xyz.deftu.ezrique.config.impl;

import xyz.deftu.ezrique.config.IConfigChild;
import xyz.qalcyo.simpleconfig.Configuration;
import xyz.qalcyo.simpleconfig.Subconfiguration;

import java.util.ArrayList;
import java.util.List;

public class BotConfig implements IConfigChild {

    private Configuration configuration;
    private Subconfiguration self;

    private final List<IConfigChild> children = new ArrayList<>();

    private String token;
    private String ownerId;
    private String supportInvite;

    public String getName() {
        return "bot";
    }

    public void initialize(Configuration configuration, Subconfiguration self) {
        this.configuration = configuration;
        this.self = self;

        if (!self.hasKey("token"))
            throw new IllegalStateException("Token is not present.");
        token = self.getAsString("token");
        if (!self.hasKey("owner_id"))
            throw new IllegalStateException("Owner ID is not present.");
        ownerId = self.getAsString("owner_id");
        if (!self.hasKey("support_invite"))
            throw new IllegalStateException("Support invite is not present.");
        supportInvite = self.getAsString("support_invite");
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

    public String getToken() {
        return token;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getSupportInvite() {
        return supportInvite;
    }

}