package xyz.deftu.ezrique.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

public class MongoHelper {

    public static boolean hasKey(MongoCollection<Document> collection, String key) {
        boolean value = false;
        for (Document document : collection.find(Filters.eq(key))) {
            if (document.containsKey(key)) {
                value = true;
                break;
            }
        }

        return value;
    }

    public static <T> boolean hasValue(MongoCollection<Document> collection, String key, T value) {
        boolean returned = false;
        for (Document document : collection.find(Filters.eq(key))) {
            if (document.containsKey(key)) {
                if (document.get(key, value).equals(value)) {
                    returned = true;
                    break;
                }
            }
        }

        return returned;
    }

    public static MongoClient createClient(String username, String password, String cluster, String database) {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb+srv://%s:%s@%s.2s9b7.mongodb.net/%s?retryWrites=true&w=majority", username, password, cluster, database));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToSocketSettings(builder -> builder.applySettings(SocketSettings.builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .build()))
                .applyToSslSettings(builder -> builder.applySettings(SslSettings.builder()
                        .enabled(true)
                        .build()))
                .applyToServerSettings(builder -> builder.applySettings(ServerSettings.builder()
                        .applyConnectionString(connectionString)
                        .build()))
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

}