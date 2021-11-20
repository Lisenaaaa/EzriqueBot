package xyz.deftu.ezrique.config.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.guild.GuildAutoRoleConfig;
import xyz.deftu.ezrique.config.impl.guild.GuildWelcomeChannelConfig;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IConfigObject {

    private static final String LEAVE_CHANNEL = "leave_channel";
    private static final String LEAVE_MESSAGE = "leave_message";
    private static final String LEAVE_MESSAGE_TOGGLE = "leave_message_toggle";

    private static final String TICKET_TOGGLE = "ticket_toggle";
    private static final String TICKET_NAME = "ticket_name";
    private static final String TICKET_ROLE = "ticket_role";
    private static final String TICKET_CATEGORY = "ticket_category";

    private Ezrique instance;

    private MongoCollection<Document> collection;

    private final List<IConfigChild> children = new ArrayList<>();

    /* Children */
    private GuildWelcomeChannelConfig welcomeChannel;
    private GuildAutoRoleConfig autorole;

    public String getName() {
        return "guilds";
    }

    public void initialize(Ezrique instance, MongoDatabase database, MongoCollection<Document> collection) {
        this.instance = instance;
        this.collection = collection;

        addChild(welcomeChannel = new GuildWelcomeChannelConfig());
        addChild(autorole = new GuildAutoRoleConfig());
    }

    public void update(String id, Document updated) {
        collection.replaceOne(Filters.eq("identifier", id), updated);
    }

    public boolean hasLeaveChannel(String id) {
        checks(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(LEAVE_MESSAGE_TOGGLE) && guild.getBoolean(LEAVE_MESSAGE_TOGGLE) && guild.containsKey(LEAVE_CHANNEL);
    }

    public String getLeaveChannel(String id) {
        checks(id);
        return retrieveGuild(id).getString(LEAVE_CHANNEL);
    }

    public void setLeaveChannel(String id, String value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_CHANNEL, value);
        update(id, guild);
    }

    public boolean hasLeaveMessage(String id) {
        checks(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(LEAVE_MESSAGE_TOGGLE) && guild.getBoolean(LEAVE_MESSAGE_TOGGLE) && guild.containsKey(LEAVE_MESSAGE);
    }

    public void setLeaveMessageToggle(String id, boolean toggle) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_MESSAGE_TOGGLE, toggle);
        update(id, guild);
    }

    public String getLeaveMessage(String id) {
        checks(id);
        return retrieveGuild(id).getString(LEAVE_MESSAGE);
    }

    public void setLeaveMessage(String id, String value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(LEAVE_MESSAGE, value);
        update(id, guild);
    }

    public boolean hasTickets(String id) {
        checks(id);
        Document guild = retrieveGuild(id);
        return guild.containsKey(TICKET_TOGGLE) && guild.getBoolean(TICKET_TOGGLE);
    }

    public void setTicketToggle(String id, boolean value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_TOGGLE, value);
        update(id, guild);
    }

    public boolean hasTicketName(String id) {
        checks(id);
        return retrieveGuild(id).containsKey(TICKET_NAME);
    }

    public String getTicketName(String id) {
        checks(id);
        return retrieveGuild(id).getString(TICKET_NAME);
    }

    public void setTicketName(String id, String value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_NAME, value);
        update(id, guild);
    }

    public boolean hasTicketRole(String id) {
        checks(id);
        return retrieveGuild(id).containsKey(TICKET_ROLE);
    }

    public String getTicketRole(String id) {
        checks(id);
        return retrieveGuild(id).getString(TICKET_ROLE);
    }

    public void setTicketRole(String id, String value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_ROLE, value);
        update(id, guild);
    }

    public boolean hasTicketCategory(String id) {
        checks(id);
        return retrieveGuild(id).containsKey(TICKET_CATEGORY);
    }

    public String getTicketCategory(String id) {
        checks(id);
        return retrieveGuild(id).getString(TICKET_CATEGORY);
    }

    public void setTicketCategory(String id, String value) {
        checks(id);
        Document guild = retrieveGuild(id);
        guild.put(TICKET_CATEGORY, value);
        update(id, guild);
    }

    /*public boolean hasAutoRole(String id) {
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
    }*/

    public Document retrieveGuild(String id) {
        return collection.find(Filters.eq("identifier", id)).first();
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

    public GuildAutoRoleConfig getAutoRole() {
        return autorole;
    }

}