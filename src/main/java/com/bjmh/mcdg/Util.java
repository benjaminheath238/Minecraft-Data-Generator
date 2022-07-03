package com.bjmh.mcdg;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Util {
    public static ConfigSection getChildAsConfigSection(String name, ConfigSection from) {
        return (ConfigSection) from.getChild(name);
    }

    public static ConfigOption getChildAsConfigOption(String name, ConfigSection from) {
        return (ConfigOption) from.getChild(name);
    }
}
