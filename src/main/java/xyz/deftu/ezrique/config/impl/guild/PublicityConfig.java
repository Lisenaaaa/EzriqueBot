package xyz.deftu.ezrique.config.impl.guild;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;

import java.util.List;

public class PublicityConfig implements IConfigChild {

    private GuildConfig parent;

    public String getName() {
        return "publicity";
    }

    public List<IConfigChild> getChildren() {
        return null;
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (GuildConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document guild = retrieve(id);
        return guild.containsKey("toggle") && guild.getBoolean("toggle");
    }

    public void setToggle(String id, boolean value) {
        Document guild = retrieve(id);
        guild.put("toggle", value);
        update(id, guild);
    }

    public boolean isInvite(String id) {
        Document guild = retrieve(id);
        return guild.containsKey("invite") && guild.getBoolean("invite");
    }

    public void setInvite(String id, boolean value) {
        Document guild = retrieve(id);
        guild.put("invite", value);
        update(id, guild);
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

}