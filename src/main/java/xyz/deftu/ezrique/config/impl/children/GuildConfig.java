package xyz.deftu.ezrique.config.impl.children;

import xyz.deftu.ezrique.config.IConfigChild;
import xyz.qalcyo.simpleconfig.Configuration;
import xyz.qalcyo.simpleconfig.Subconfiguration;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IConfigChild {

    private static final String BOOST_MESSAGE = "boost_message";
    private static final String BOOST_MESSAGE_TOGGLE = "boost_message_toggle";
    private static final String WELCOME_CHANNEL = "welcome_channel";
    private static final String WELCOME_MESSAGE = "welcome_message";
    private static final String WELCOME_MESSAGE_TOGGLE = "welcome_message_toggle";
    private static final String LEAVE_CHANNEL = "leave_channel";
    private static final String LEAVE_MESSAGE = "leave_message";
    private static final String LEAVE_MESSAGE_TOGGLE = "leave_message_toggle";

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

        if (!guild.hasKey(BOOST_MESSAGE_TOGGLE)) {
            guild.add(BOOST_MESSAGE_TOGGLE, false);
            updated = true;
        }

        if (!guild.hasKey(WELCOME_MESSAGE_TOGGLE)) {
            guild.add(WELCOME_MESSAGE_TOGGLE, false);
            updated = true;
        }

        if (!guild.hasKey("leave_message_toggle")) {
            guild.add("leave_message_toggle", false);
            updated = true;
        }

        if (updated) {
            configuration.save();
        }
    }

    public boolean hasBoostMessage(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey(BOOST_MESSAGE_TOGGLE) && retrieveGuild(id).getAsBoolean(BOOST_MESSAGE_TOGGLE) && retrieveGuild(id).hasKey(BOOST_MESSAGE);
    }

    public void setBoostMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        retrieveGuild(id).add(BOOST_MESSAGE_TOGGLE, toggle);
        configuration.save();
    }

    public String getBoostMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString(BOOST_MESSAGE);
    }

    public void setBoostMessage(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add("boost_message", value);
        configuration.save();
    }

    public boolean hasWelcomeChannel(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey(WELCOME_MESSAGE_TOGGLE) && retrieveGuild(id).getAsBoolean(WELCOME_MESSAGE_TOGGLE) && retrieveGuild(id).hasKey(WELCOME_CHANNEL);
    }

    public String getWelcomeChannel(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString(WELCOME_CHANNEL);
    }

    public void setWelcomeChannel(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add(WELCOME_CHANNEL, value);
        configuration.save();
    }

    public boolean hasWelcomeMessage(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey(WELCOME_MESSAGE_TOGGLE) && retrieveGuild(id).getAsBoolean(WELCOME_MESSAGE_TOGGLE) && retrieveGuild(id).hasKey(WELCOME_MESSAGE);
    }

    public void setWelcomeMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        retrieveGuild(id).add(WELCOME_MESSAGE_TOGGLE, toggle);
        configuration.save();
    }

    public String getWelcomeMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString(WELCOME_MESSAGE);
    }

    public void setWelcomeMessage(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add(WELCOME_MESSAGE, value);
        configuration.save();
    }

    public boolean hasLeaveChannel(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey(LEAVE_MESSAGE_TOGGLE) && retrieveGuild(id).getAsBoolean(LEAVE_MESSAGE_TOGGLE) && retrieveGuild(id).hasKey(LEAVE_CHANNEL);
    }

    public String getLeaveChannel(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString(LEAVE_CHANNEL);
    }

    public void setLeaveChannel(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add(LEAVE_CHANNEL, value);
        configuration.save();
    }

    public boolean hasLeaveMessage(String id) {
        ensureExistence(id);
        return self.hasKey(id) && retrieveGuild(id).hasKey(LEAVE_MESSAGE_TOGGLE) && retrieveGuild(id).getAsBoolean(LEAVE_MESSAGE_TOGGLE) && retrieveGuild(id).hasKey(LEAVE_MESSAGE);
    }

    public void setLeaveMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        retrieveGuild(id).add(LEAVE_MESSAGE, toggle);
        configuration.save();
    }

    public String getLeaveMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getAsString(LEAVE_MESSAGE);
    }

    public void setLeaveMessage(String id, String value) {
        ensureExistence(id);
        retrieveGuild(id).add(LEAVE_MESSAGE, value);
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