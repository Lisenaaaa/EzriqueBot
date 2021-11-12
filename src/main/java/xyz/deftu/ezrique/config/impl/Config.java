package xyz.deftu.ezrique.config.impl;

import xyz.deftu.ezrique.config.IConfigChild;
import xyz.deftu.ezrique.config.IConfigObject;
import xyz.qalcyo.mango.Lists;
import xyz.qalcyo.simpleconfig.Configuration;

import java.io.File;
import java.util.List;

public class Config implements IConfigObject {

    private final Configuration configuration;
    private final List<IConfigChild> children;

    public Config(String filename, File directory) {
        this.configuration = new Configuration(filename, directory);
        this.children = Lists.newArrayList();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<IConfigChild> getChildren() {
        return children;
    }

}