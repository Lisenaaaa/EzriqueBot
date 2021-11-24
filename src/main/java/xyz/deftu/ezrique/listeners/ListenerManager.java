package xyz.deftu.ezrique.listeners;

import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListenerManager {

    private ShardManager api;
    private final Logger logger = LogManager.getLogger("Ezrique (ListenerManager)");
    private final Map<String, ListenerBase> listeners = new HashMap<>();

    public void initialize(ShardManager api) {
        this.api = api;
        for (Map.Entry<String, ListenerBase> entry : listeners.entrySet()) {
            entry.getValue().initialize(api);
        }

        logger.info("Initialized listener manager with {} listener(s).", listeners.size());
    }

    public void addListener(String id, ListenerBase listener) {
        listeners.put(id, listener);
    }

    public void removeListener(String id) {
        api.removeEventListener(listeners.get(id));
        listeners.remove(id);
    }

    public Map<String, ListenerBase> getListeners() {
        return Collections.unmodifiableMap(listeners);
    }

}