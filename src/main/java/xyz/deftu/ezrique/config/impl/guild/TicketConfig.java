package xyz.deftu.ezrique.config.impl.guild;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;

import java.util.List;

public class TicketConfig implements IConfigChild {

    private GuildConfig parent;

    public String getName() {
        return "tickets";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (GuildConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document document = retrieve(id);
        return document.containsKey("toggle") && document.getBoolean("toggle") && document.containsKey("name") && document.containsKey("category");
    }

    public void setToggle(String id, boolean value) {
        Document document = retrieve(id);
        document.put("toggle", value);
        update(id, document);
    }

    public String getName(String id) {
        return retrieve(id).getString("name");
    }

    public void setName(String id, String value) {
        Document document = retrieve(id);
        document.put("name", value);
        update(id, document);
    }

    public boolean isRoleAvailable(String id) {
        return retrieve(id).containsKey("role");
    }

    public String getRole(String id) {
        return retrieve(id).getString("role");
    }

    public void setRole(String id, String value) {
        Document document = retrieve(id);
        document.put("role", value);
        update(id, document);
    }

    public String getCategory(String id) {
        return retrieve(id).getString("category");
    }

    public void setCategory(String id, String value) {
        Document document = retrieve(id);
        document.put("category", value);
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