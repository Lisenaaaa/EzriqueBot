package xyz.deftu.ezrique.config.impl.guild.autorole;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.guild.GuildAutoRoleConfig;

import java.util.List;

public class GuildAutoRoleBotsConfig implements IConfigChild {

    private GuildAutoRoleConfig parent;

    public String getName() {
        return "bots";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (GuildAutoRoleConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document guild = parent.getParent().retrieveGuild(id);
        return guild.containsKey("toggle") && guild.getBoolean("toggle") && guild.containsKey("role");
    }

    public void setToggle(String id, boolean toggle) {
        Document guild = parent.getParent().retrieveGuild(id);
        guild.put("toggle", toggle);
        parent.getParent().update(id, guild);
    }

    public String getRole(String id) {
        return parent.getParent().retrieveGuild(id).getString("role");
    }

    public void setRole(String id, String value) {
        Document guild = parent.getParent().retrieveGuild(id);
        guild.put("role", value);
        parent.getParent().update(id, guild);
    }

    public GuildAutoRoleConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return null;
    }

}