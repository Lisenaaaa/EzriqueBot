package xyz.deftu.ezrique.networking;

import spark.Request;
import spark.Response;

import java.util.List;

public interface IEndpoint {
    EndpointType getType();
    String getName();
    default String getName(String previous) {
        return previous + "/" + getName();
    }

    Object handle(Request request, Response response);

    default List<IEndpoint> getChildren() {
        return null;
    }
}