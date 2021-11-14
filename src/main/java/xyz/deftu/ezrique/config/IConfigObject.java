package xyz.deftu.ezrique.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import xyz.deftu.ezrique.Ezrique;

import java.util.List;

public interface IConfigObject {
    String getName();
    List<IConfigChild> getChildren();

    void initialize(Ezrique instance, MongoDatabase database, MongoCollection<Document> collection);

    default void addChild(IConfigChild child) {
        List<IConfigChild> children = getChildren();
        if (children != null) {
            children.add(child);
        } else {
            throw new UnsupportedOperationException("This configuration object does not support child objects.");
        }
    }
}