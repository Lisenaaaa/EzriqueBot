package xyz.deftu.ezrique.config.impl.guild;

import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;

import java.util.List;

public class GuildWelcomeChannelConfig implements IConfigChild {

    private GuildConfig parent;

    public String getName() {
        return "welcome_channel";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.parent = (GuildConfig) parent;
    }

    public boolean isAvailable(String id) {
        Document guild = parent.retrieveGuild(id);
        return guild.containsKey("toggle") && guild.getBoolean("toggle") && guild.containsKey("channel") && guild.containsKey("message");
    }

    public String getChannel(String id) {
        return parent.retrieveGuild(id).getString("channel");
    }

    public void setChannel(String id, String value) {
        Document guild = parent.retrieveGuild(id);
        guild.put("channel", value);
        parent.update(id, guild);
    }

    public void setToggle(String id, boolean value) {
        Document document = parent.retrieveGuild(id);
        document.put("toggle", value);
        parent.update(id, document);
    }

    public String getMessage(String id) {
        return parent.retrieveGuild(id).getString("message");
    }

    public void setMessage(String id, String value) {
        Document document = parent.retrieveGuild(id);
        document.put("message", value);
        parent.update(id, document);
    }

    public GuildConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return null;
    }

}