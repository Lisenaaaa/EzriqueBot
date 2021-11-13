package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.deftu.ezrique.Ezrique;

public abstract class ListenerBase {

    protected Ezrique ezrique;
    protected ShardManager api;

    public final void initialize(ShardManager api) {
        this.ezrique = Ezrique.getInstance();
        this.api = api;
        this.api.addEventListener(this);
    }

}