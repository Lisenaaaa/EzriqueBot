package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.JDA;
import xyz.deftu.ezrique.Ezrique;

public abstract class ListenerBase {

    protected Ezrique ezrique;
    protected JDA api;

    public final void initialize(JDA api) {
        this.ezrique = Ezrique.getInstance();
        this.api = api;
        this.api.addEventListener(this);
    }

}