package xyz.deftu.ezrique.mongo;

import com.mongodb.client.MongoClient;
import xyz.deftu.ezrique.util.MongoHelper;

public class MongoConnection {

    private final MongoClient configurationsClient;

    public MongoConnection(String username, String password) {
        configurationsClient = MongoHelper.createClient(username, password, "cluster-0", "configurations");
    }

    public MongoClient getConfigurationsClient() {
        return configurationsClient;
    }

}