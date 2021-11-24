package xyz.deftu.ezrique.config.impl.guild;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;

import java.util.List;

public class LeaveChannelConfig implements IConfigChild {

    private GuildConfig parent;

    public String getName() {
        return "leave_channel";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (GuildConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document guild = retrieve(id);
        return guild.containsKey("toggle") && guild.getBoolean("toggle") && guild.containsKey("channel") && guild.containsKey("message");
    }

    public String getChannel(String id) {
        return retrieve(id).getString("channel");
    }

    public void setChannel(String id, String value) {
        Document guild = retrieve(id);
        guild.put("channel", value);
        update(id, guild);
    }

    public void setToggle(String id, boolean value) {
        Document document = retrieve(id);
        document.put("toggle", value);
        update(id, document);
    }

    public String getMessage(String id) {
        return retrieve(id).getString("message");
    }

    public void setMessage(String id, String value) {
        Document document = retrieve(id);
        document.put("message", value);
        update(id, document);
    }

    private Document retrieve(String id) {
        Document guild = parent.retrieveGuild(id);
        if (guild == null) {
            Document created = new Document().append("identifier", id);
            parent.getCollection().insertOne(created);
            guild = created;
        }

        guild.putIfAbsent(getName(), new Document());
        return guild.get(getName(), Document.class);
    }

    private void update(String id, Document self) {
        Document parent = this.parent.retrieveGuild(id);
        parent.put(getName(), self);
        this.parent.update(id, parent);
    }

    public GuildConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return null;
    }

}