package xyz.deftu.ezrique.config;

import xyz.qalcyo.simpleconfig.Configuration;

import java.util.List;

public interface IConfigObject {
    Configuration getConfiguration();
    List<IConfigChild> getChildren();
    default void addChild(IConfigChild child) {
        List<IConfigChild> children = getChildren();
        if (children != null) {
            Configuration configuration = getConfiguration();
            if (configuration != null) {
                String name = child.getName();
                if (name != null) {
                    if (!configuration.hasKey(name)) {
                        configuration.createSubconfiguration(name).save();
                    }

                    child.initialize(configuration, configuration.getSubconfiguration(name));
                    children.add(child);
                } else {
                    throw new NullPointerException("The child's name is null... Maybe you made a mistake?");
                }
            } else {
                throw new NullPointerException("It seems that the configuration is null... Maybe you made a mistake?");
            }
        } else {
            throw new UnsupportedOperationException("This configuration object does not support child objects.");
        }
    }
}