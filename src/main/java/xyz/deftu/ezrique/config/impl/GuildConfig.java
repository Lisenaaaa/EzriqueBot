package xyz.deftu.ezrique.config.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IConfigObject {

    private static final String WELCOME_CHANNEL = "welcome_channel";
    private static final String WELCOME_MESSAGE = "welcome_message";
    private static final String WELCOME_MESSAGE_TOGGLE = "welcome_message_toggle";
    private static final String LEAVE_CHANNEL = "leave_channel";
    private static final String LEAVE_MESSAGE = "leave_message";
    private static final String LEAVE_MESSAGE_TOGGLE = "leave_message_toggle";

    private static final String TICKET_TOGGLE = "ticket_toggle";
    private static final String TICKET_NAME = "ticket_name";
    private static final String TICKET_ROLE = "ticket_role";
    private static final String TICKET_CATEGORY = "ticket_category";

    private static final String AUTOROLE = "autorole";
    private static final String AUTOROLE_TOGGLE = "autorole_toggle";
    private static final String AUTOROLE_BOTS = "autorole_bots";
    private static final String AUTOROLE_BOTS_TOGGLE = "autorole_bots_toggle";

    private static final String GITHUB_TOGGLE = "github_toggle";
    private static final String GITHUB_CHANNEL = "github_channel";
    private static final String GITHUB_ROLE = "github_role";

    private Ezrique instance;

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private final List<IConfigChild> children = new ArrayList<>();

    public String getName() {
        return "guilds";
    }

    public void initialize(Ezrique instance, MongoDatabase database, MongoCollection<Document> collection) {
        this.instance = instance;
        this.database = database;
        this.collection = collection;
    }

    public void update(String id, Document updated) {
        collection.replaceOne(Filters.eq("identifier", id), updated);
    }

    private void ensureExistence(String id) {
        Document guild = retrieveGuild(id);
        boolean updated = false;

        if (guild == null) {
            Document document = new Document();
            document.put("identifier", id);
            collection.insertOne(document);
            guild = retrieveGuild(id);
        }

        assert guild != null;

        if (!guild.containsKey(WELCOME_MESSAGE_TOGGLE)) {
            guild.put(WELCOME_MESSAGE_TOGGLE, false);
            updated = true;
        }

        if (!guild.containsKey(LEAVE_MESSAGE_TOGGLE)) {
            guild.put(LEAVE_MESSAGE_TOGGLE, false);
            updated = true;
        }

        if (!guild.containsKey(TICKET_NAME)) {
            guild.put(TICKET_NAME, "{name}-{uuid}");
            updated = true;
        }

        if (!guild.containsKey(TICKET_TOGGLE)) {
            guild.put(TICKET_TOGGLE, false);
            updated = true;
        }

        if (!guild.containsKey(AUTOROLE_TOGGLE)) {
            guild.put(AUTOROLE_TOGGLE, false);
            updated = true;
        }

        if (!guild.containsKey(AUTOROLE_BOTS_TOGGLE)) {
            guild.put(AUTOROLE_BOTS_TOGGLE, false);
            updated = true;
        }

        if (!guild.containsKey(GITHUB_TOGGLE)) {
            guild.put(GITHUB_TOGGLE, false);
            updated = true;
        }

        if (updated) {
            update(id, guild);
        }
    }

    public boolean hasWelcomeChannel(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(WELCOME_MESSAGE_TOGGLE) && guild.getBoolean(WELCOME_MESSAGE_TOGGLE) && guild.containsKey(WELCOME_CHANNEL);
    }

    public String getWelcomeChannel(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(WELCOME_CHANNEL);
    }

    public void setWelcomeChannel(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(WELCOME_CHANNEL, value);
        update(id, guild);
    }

    public boolean hasWelcomeMessage(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(WELCOME_MESSAGE_TOGGLE) && guild.getBoolean(WELCOME_MESSAGE_TOGGLE) && guild.containsKey(WELCOME_MESSAGE);
    }

    public void setWelcomeMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        Document document = retrieveGuild(id);
        document.put(WELCOME_MESSAGE_TOGGLE, toggle);
        update(id, document);
    }

    public String getWelcomeMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(WELCOME_MESSAGE);
    }

    public void setWelcomeMessage(String id, String value) {
        ensureExistence(id);
        Document document = retrieveGuild(id);
        document.put(WELCOME_MESSAGE, value);
        update(id, document);
    }

    public boolean hasLeaveChannel(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(LEAVE_MESSAGE_TOGGLE) && guild.getBoolean(LEAVE_MESSAGE_TOGGLE) && guild.containsKey(LEAVE_CHANNEL);
    }

    public String getLeaveChannel(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(LEAVE_CHANNEL);
    }

    public void setLeaveChannel(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_CHANNEL, value);
        update(id, guild);
    }

    public boolean hasLeaveMessage(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(LEAVE_MESSAGE_TOGGLE) && guild.getBoolean(LEAVE_MESSAGE_TOGGLE) && guild.containsKey(LEAVE_MESSAGE);
    }

    public void setLeaveMessageToggle(String id, boolean toggle) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_MESSAGE_TOGGLE, toggle);
        update(id, guild);
    }

    public String getLeaveMessage(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(LEAVE_MESSAGE);
    }

    public void setLeaveMessage(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_MESSAGE, value);
        update(id, guild);
    }

    public boolean hasTickets(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(TICKET_TOGGLE) && guild.getBoolean(TICKET_TOGGLE);
    }

    public void setTicketToggle(String id, boolean value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_TOGGLE, value);
        update(id, guild);
    }

    public boolean hasTicketName(String id) {
        ensureExistence(id);
        return retrieveGuild(id).containsKey(TICKET_NAME);
    }

    public String getTicketName(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(TICKET_NAME);
    }

    public void setTicketName(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_NAME, value);
        update(id, guild);
    }

    public boolean hasTicketRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).containsKey(TICKET_ROLE);
    }

    public String getTicketRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(TICKET_ROLE);
    }

    public void setTicketRole(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_ROLE, value);
        update(id, guild);
    }

    public boolean hasTicketCategory(String id) {
        ensureExistence(id);
        return retrieveGuild(id).containsKey(TICKET_CATEGORY);
    }

    public String getTicketCategory(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(TICKET_CATEGORY);
    }

    public void setTicketCategory(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_CATEGORY, value);
        update(id, guild);
    }

    public boolean hasAutoRole(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.getBoolean(AUTOROLE_TOGGLE) && guild.containsKey(AUTOROLE);
    }

    public void setAutoRoleToggle(String id, boolean toggle) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(AUTOROLE_TOGGLE, toggle);
        update(id, guild);
    }

    public String getAutoRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(AUTOROLE);
    }

    public void setAutoRole(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(AUTOROLE, value);
        update(id, guild);
    }

    public boolean hasBotsAutoRole(String id) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        return guild.getBoolean(AUTOROLE_BOTS_TOGGLE) && guild.containsKey(AUTOROLE_BOTS);
    }

    public void setBotsAutoRoleToggle(String id, boolean toggle) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(AUTOROLE_BOTS_TOGGLE, toggle);
        update(id, guild);
    }

    public String getBotsAutoRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(AUTOROLE_BOTS);
    }

    public void setBotsAutoRole(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(AUTOROLE_BOTS, value);
        update(id, guild);
    }

    public boolean hasGitHub(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getBoolean(GITHUB_TOGGLE);
    }

    public void setGitHubToggle(String id, boolean value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(GITHUB_TOGGLE, value);
        update(id, guild);
    }

    public String getGitHubChannel(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(GITHUB_CHANNEL);
    }

    public void setGitHubChannel(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(GITHUB_CHANNEL, value);
        update(id, guild);
    }

    public boolean hasGitHubRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).containsKey(GITHUB_ROLE);
    }

    public String getGitHubRole(String id) {
        ensureExistence(id);
        return retrieveGuild(id).getString(GITHUB_ROLE);
    }

    public void setGitHubRole(String id, String value) {
        ensureExistence(id);
        Document guild = retrieveGuild(id);
        guild.put(GITHUB_ROLE, value);
        update(id, guild);
    }

    private Document retrieveGuild(String id) {
        return collection.find(Filters.eq("identifier", id)).first();
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

}