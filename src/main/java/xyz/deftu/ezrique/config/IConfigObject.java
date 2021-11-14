package xyz.deftu.ezrique.config;

import java.util.List;

public interface IConfigObject {
    String getName();
    List<IConfigChild> getChildren();
    default void addChild(IConfigChild child) {
        List<IConfigChild> children = getChildren();
        if (children != null) {

        } else {
            throw new UnsupportedOperationException("This configuration object does not support child objects.");
        }
    }
}