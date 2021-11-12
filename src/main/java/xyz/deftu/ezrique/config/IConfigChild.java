package xyz.deftu.ezrique.config;

import xyz.qalcyo.simpleconfig.Configuration;
import xyz.qalcyo.simpleconfig.Subconfiguration;

public interface IConfigChild extends IConfigObject {
    String getName();
    void initialize(Configuration configuration, Subconfiguration self);
}