package xyz.deftu.ezrique.config;

import com.mongodb.client.MongoDatabase;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.config.impl.GuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigManager {

    private final List<IConfigObject> configurations = new ArrayList<>();

    public void initialize(Ezrique instance) {
        for (IConfigObject configuration : configurations) {
            MongoDatabase database = instance.getMongoConnection().getConfigurationsClient().getDatabase("configurations");
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(configuration.getName())) {
                database.createCollection(configuration.getName());
            }

            configuration.initialize(instance, database, database.getCollection(configuration.getName()));
        }
    }

    public void addConfiguration(IConfigObject configuration) {
        configurations.add(configuration);
    }

    public <T extends IConfigObject> T getConfiguration(String name) {
        AtomicReference<T> configuration = new AtomicReference<>();
        configurations.stream().filter(object -> object.getName().equalsIgnoreCase(name)).findFirst().ifPresent(object -> configuration.set((T) object));
        return configuration.get();
    }

    /* Specifics. */

    public GuildConfig getGuild() {
        return getConfiguration("guilds");
    }

}