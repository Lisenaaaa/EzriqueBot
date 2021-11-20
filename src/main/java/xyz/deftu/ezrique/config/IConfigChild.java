package xyz.deftu.ezrique.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;

public interface IConfigChild extends IConfigObject {
    String getName();

    void initialize(Ezrique instance, IConfigObject parent);

    IConfigObject getParent();

    default void initialize(Ezrique instance, MongoDatabase database, MongoCollection<Document> collection) {
    }

}