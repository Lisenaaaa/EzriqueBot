package xyz.deftu.ezrique.config.impl.guild.autorole;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.guild.AutoRoleConfig;

import java.util.List;

public class AutoRoleBotsConfig implements IConfigChild {

    private AutoRoleConfig parent;

    public String getName() {
        return "bots";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (AutoRoleConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document guild = retrieve(id);
        return guild.containsKey("toggle") && guild.getBoolean("toggle") && guild.containsKey("role");
    }

    public void setToggle(String id, boolean toggle) {
        Document guild = retrieve(id);
        guild.put("toggle", toggle);
        update(id, guild);
    }

    public String getRole(String id) {
        return retrieve(id).getString("role");
    }

    public void setRole(String id, String value) {
        Document guild = retrieve(id);
        guild.put("role", value);
        update(id, guild);
    }

    public Document retrieve(String id) {
        Document guild = parent.retrieve(id);
        guild.putIfAbsent(getName(), new Document());
        return guild.get(getName(), Document.class);
    }

    public void update(String id, Document self) {
        Document parent = this.parent.retrieve(id);
        parent.put(getName(), self);
        this.parent.update(id, parent);
    }

    public AutoRoleConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return null;
    }

}