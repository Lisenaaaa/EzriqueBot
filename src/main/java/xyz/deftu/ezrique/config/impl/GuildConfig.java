package xyz.deftu.ezrique.config.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.guild.AutoRoleConfig;
import xyz.deftu.ezrique.config.impl.guild.LeaveChannelConfig;
import xyz.deftu.ezrique.config.impl.guild.TicketConfig;
import xyz.deftu.ezrique.config.impl.guild.WelcomeChannelConfig;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IConfigObject {

    private static final String TICKET_TOGGLE = "ticket_toggle";
    private static final String TICKET_NAME = "ticket_name";
    private static final String TICKET_ROLE = "ticket_role";
    private static final String TICKET_CATEGORY = "ticket_category";

    private Ezrique instance;

    private MongoCollection<Document> collection;

    private final List<IConfigChild> children = new ArrayList<>();

    /* Children */
    private WelcomeChannelConfig welcomeChannel;
    private LeaveChannelConfig leaveChannel;
    private TicketConfig tickets;
    private AutoRoleConfig autorole;

    public String getName() {
        return "guilds";
    }

    public void initialize(Ezrique instance, MongoDatabase database, MongoCollection<Document> collection) {
        this.instance = instance;
        this.collection = collection;

        addChild(welcomeChannel = new WelcomeChannelConfig());
        addChild(leaveChannel = new LeaveChannelConfig());
        addChild(tickets = new TicketConfig());
        addChild(autorole = new AutoRoleConfig());
    }

    public void update(String id, Document updated) {
        collection.replaceOne(Filters.eq("identifier", id), updated);
    }

    public Document retrieveGuild(String id) {
        return collection.find(Filters.eq("identifier", id)).first();
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

    public WelcomeChannelConfig getWelcomeChannel() {
        return welcomeChannel;
    }

    public LeaveChannelConfig getLeaveChannel() {
        return leaveChannel;
    }

    public TicketConfig getTickets() {
        return tickets;
    }

    public AutoRoleConfig getAutoRole() {
        return autorole;
    }

}