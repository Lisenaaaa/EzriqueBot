package xyz.deftu.ezrique.config.impl.guild;

import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.deftu.ezrique.config.impl.GuildConfig;
import xyz.deftu.ezrique.config.impl.guild.autorole.GuildAutoRoleBotsConfig;
import xyz.deftu.ezrique.config.impl.guild.autorole.GuildAutoRoleMembersConfig;

import java.util.ArrayList;
import java.util.List;

public class GuildAutoRoleConfig implements IConfigChild {

    private Ezrique ezrique;
    private GuildConfig parent;

    private final List<IConfigChild> children = new ArrayList<>();

    /* Children. */
    private GuildAutoRoleMembersConfig members;
    private GuildAutoRoleBotsConfig bots;

    public String getName() {
        return "autorole";
    }

    public void initialize(Ezrique instance, IConfigObject parent) {
        this.ezrique = instance;
        this.parent = (GuildConfig) parent;

        addChild(members = new GuildAutoRoleMembersConfig());
        addChild(bots = new GuildAutoRoleBotsConfig());
    }

    public GuildConfig getParent() {
        return parent;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

    public GuildAutoRoleMembersConfig getMembers() {
        return members;
    }

    public GuildAutoRoleBotsConfig getBots() {
        return bots;
    }

}