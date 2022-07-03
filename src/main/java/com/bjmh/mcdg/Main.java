package com.bjmh.mcdg;

import java.io.IOException;
import java.util.Scanner;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.lib.io.config.Configuration;
import com.bjmh.lib.io.config.ParserMethod;
import com.bjmh.lib.io.config.ParserMethods;

public class Main {
    // Internal option keys
    public static final String REGISTRY_KEY = "registry"; // Should not be used as an option

    // External task keys
    public static final String MODEL_KEY = "model"; // true|false
    public static final String LOCALE_KEY = "locale"; // true|false
    public static final String BLOCKSTATE_KEY = "blockstate"; // true|false
    public static final String TEXTURE_KEY = "texture"; // true|false

    // External option keys
    public static final String TYPE_KEY = "type"; // tile|item|itemGroup
    public static final String NAME_KEY = "name"; // Any string
    public static final String PATH_KEY = "path"; // relative path from textures/blocks|items/
    public static final String LAYER_KEY = "layer"; // true|false by appending _N this will be treated as a layer to add
                                                    // 0 < N < max int
    public static final String TRANSPARENT_KEY = "transparent"; // true|false
    public static final String SIZE_KEY = "size"; // true|false

    // External option and task values
    public static final String TRUE_KEY = "true"; // Any other value will be taken as false
    public static final String FALSE_KEY = "false"; // Any other value will be taken as false

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Configuration CONFIG = new Configuration("root");

    public static void main(String[] args) throws IOException {
        System.out.println("Enter The Mod ID");
        String modid = SCANNER.nextLine();

        System.out.println("Enter Config File Path");
        CONFIG.parse(SCANNER.nextLine(), new ParserMethod() {

            @Override
            public void parse(String line, Configuration config) {
                line = ParserMethods.removeComments(line);

                if (line.isEmpty())
                    return;

                if (ParserMethods.isComplexOption(line)) {
                    CONFIG.addChild(ParserMethods.parseComplexOption(line, config, config));
                }
            }

        });

        for (ConfigNode node : CONFIG.getChildren()) {
            if (!(node instanceof ConfigNode))
                continue;

            ConfigSection section = (ConfigSection) node;

            if (section.getChild(MODEL_KEY) != null
                    && Util.getChildAsConfigOption(MODEL_KEY, section).getValue().equals(TRUE_KEY)) {
                if (Util.getChildAsConfigOption(TYPE_KEY, section).getValue().equals("tile")) {
                    Generators.generateBlockModel(section, modid);
                }
                if (Util.getChildAsConfigOption(TYPE_KEY, section).getValue().equals("item")) {
                    Generators.generateItemModel(section, modid);
                }
            }

            if (section.getChild(BLOCKSTATE_KEY) != null
                    && Util.getChildAsConfigOption(BLOCKSTATE_KEY, section).getValue().equals(TRUE_KEY)) {
                if (Util.getChildAsConfigOption(TYPE_KEY, section).getValue().equals("tile")) {
                    Generators.generateBlockstate(section, modid);
                }
            }

            if (section.getChild(LOCALE_KEY) != null
                    && Util.getChildAsConfigOption(LOCALE_KEY, section).getValue().equals(TRUE_KEY)) {
                Generators.generateLocalisation(section, modid);
            }

            if (section.getChild(TEXTURE_KEY) != null
                    && Util.getChildAsConfigOption(TEXTURE_KEY, section).getValue().equals(TRUE_KEY)) {
                Generators.generateTexture(section, modid);
            }
        }
    }
}
