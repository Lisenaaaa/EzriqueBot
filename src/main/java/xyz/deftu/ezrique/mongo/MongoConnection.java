package xyz.deftu.ezrique.mongo;

import com.mongodb.client.MongoClient;
import xyz.deftu.ezrique.util.MongoHelper;

public class MongoConnection {

    private final MongoClient client;

    public MongoConnection(String username, String password) {
        client = MongoHelper.createClient(username, password, "cluster-0", "configurations");
    }

    public MongoClient getClient() {
        return client;
    }

}