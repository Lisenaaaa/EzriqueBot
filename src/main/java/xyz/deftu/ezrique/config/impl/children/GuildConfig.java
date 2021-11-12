package xyz.deftu.ezrique.config.impl.children;

import xyz.deftu.ezrique.config.IConfigChild;
import xyz.qalcyo.simpleconfig.Configuration;
import xyz.qalcyo.simpleconfig.Subconfiguration;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IConfigChild {

    private Configuration configuration;
    private Subconfiguration self;

    private final List<IConfigChild> children = new ArrayList<>();

    public String getName() {
        return "guilds";
    }

    public void initialize(Configuration configuration, Subconfiguration self) {
        this.configuration = configuration;
        this.self = self;
    }

    private void ensureExistence(String id) {
        boolean updated = false;
        if (!self.hasKey(id)) {
            self.createSubconfiguration(id);
            updated = true;
        }

        Subconfiguration guild = retrieveGuild(id);

        if (!guild.hasKey("boost_message_toggle")) {
            guild.add("boost_message_toggle", false);
            System.out.println(guild);
            System.out.println(self);
            System.out.println(configuration);
            updated = true;
        }

        if (updated) {
            configuration.save();
        }
    }

    public boolean hasBoostMessage(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey("boost_message_toggle") && retrieveGuild(id).getAsBoolean("boost_message_toggle") && retrieveGuild(id).hasKey("boost_message");
    }

    public void setBoostMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        retrieveGuild(id).add("boost_message_toggle", toggle);
        configuration.save();
    }

    public String getBoostMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString("boost_message");
    }

    public void setBoostMessage(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add("boost_message", value);
        configuration.save();
    }

    private Subconfiguration retrieveGuild(String id) {
        return self.getSubconfiguration(id);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

}