package xyz.deftu.ezrique.networking;

import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class NetworkManager {

    private final List<IEndpoint> endpoints = new ArrayList<>();

    public void initialize() {
        Spark.port(4357);
        for (IEndpoint endpoint : endpoints) {
            initializeEndpoint("/", endpoint);
        }
    }

    private void initializeEndpoint(String previous, IEndpoint endpoint) {
        switch (endpoint.getType()) {
            case GET:
                Spark.get(endpoint.getName(previous), endpoint::handle);
                break;
            case POST:
                Spark.post(endpoint.getName(previous), endpoint::handle);
                break;
            case PUT:
                Spark.put(endpoint.getName(previous), endpoint::handle);
                break;
            case DELETE:
                Spark.delete(endpoint.getName(previous), endpoint::handle);
                break;
            case PATCH:
                Spark.patch(endpoint.getName(previous), endpoint::handle);
        }

        List<IEndpoint> children = endpoint.getChildren();
        if (children != null && !children.isEmpty()) {
            for (IEndpoint child : children) {
                initializeEndpoint(endpoint.getName(), child);
            }
        }
    }

    public void addEndpoint(IEndpoint endpoint) {
        endpoints.add(endpoint);
    }

}