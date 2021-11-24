package xyz.deftu.ezrique.config.impl.guild;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;
import xyz.deftu.ezrique.config.impl.guild.autorole.AutoRoleBotsConfig;
import xyz.deftu.ezrique.config.impl.guild.autorole.AutoRoleMembersConfig;

import java.util.ArrayList;
import java.util.List;

public class AutoRoleConfig implements IConfigChild {

    private Ezrique ezrique;
    private GuildConfig parent;

    private final List<IConfigChild> children = new ArrayList<>();

    /* Children. */
    private AutoRoleMembersConfig members;
    private AutoRoleBotsConfig bots;

    public String getName() {
        return "autorole";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.ezrique = instance;
        this.parent = (GuildConfig) parent;

        addChild(members = new AutoRoleMembersConfig());
        addChild(bots = new AutoRoleBotsConfig());
    }

    public Document retrieve(String id) {
        Document guild = parent.retrieveGuild(id);
        if (guild == null) {
            Document created = new Document().append("identifier", id);
            parent.getCollection().insertOne(created);
            guild = created;
        }

        guild.putIfAbsent(getName(), new Document());
        return guild.get(getName(), Document.class);
    }

    public void update(String id, Document self) {
        Document parent = this.parent.retrieveGuild(id);
        parent.put(getName(), self);
        this.parent.update(id, parent);
    }

    public GuildConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

    public AutoRoleMembersConfig getMembers() {
        return members;
    }

    public AutoRoleBotsConfig getBots() {
        return bots;
    }

}