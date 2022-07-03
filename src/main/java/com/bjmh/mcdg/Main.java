package com.bjmh.mcdg;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import com.bjmh.lib.io.config.ConfigConsumer;
import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigOption;
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
    public static final String TILE_KEY = "tile";
    public static final String ITEM_KEY = "item";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String USER_DIR = System.getProperty("user.dir");

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Configuration CONFIG = new Configuration("root");

    public static void main(String[] args) throws IOException {
        System.setErr(new PrintStream(new File(Util.createSystemPath("latest.log", USER_DIR))));

        System.out.println("Enter The Mod ID");
        String modid = SCANNER.nextLine();

        System.out.println("Enter Config File Path");
        CONFIG.parse(SCANNER.nextLine(), new ParserMethod() {
            private ConfigSection section = null;

            public void parse(String line, Configuration config) {
                line = ParserMethods.removeComments(line);

                if (line.isEmpty())
                    return;

                if (ParserMethods.isSubHeader(line)) {
                    section = ParserMethods.inheritOptions(ParserMethods.parseSubHeader(line, config), config);
                } else if (ParserMethods.isHeader(line)) {
                    section = ParserMethods.parseHeader(line, config);
                } else if (ParserMethods.isComplexOption(line)) {
                    line = line.replaceAll("[\\(\\)]", "").trim();
                    ConfigSection complex = ParserMethods
                            .inheritOptions(ParserMethods.parseComplexOption(line, config, section), config);

                    ConfigOption option = config.newConfigOption();

                    option.setName(REGISTRY_KEY);
                    option.setParent(complex);
                    option.setType(ConfigNode.Type.SIMPLE_OPTION);
                    option.setValue(complex.getName());

                    complex.addChild(option);

                    section.addChild(complex);
                } else {
                    section.addChild(ParserMethods.parseSimpleOption(line, config, section == null ? config : section));
                }
            }
        });

        System.err.println(CONFIG);

        CONFIG.foreach(new ConfigConsumer() {

            @Override
            public void accept(ConfigNode node) {

                if (!(node instanceof ConfigSection && node.getType().equals(ConfigNode.Type.COMPLEX_OPTION)))
                    return;

                ConfigSection section = (ConfigSection) node;

                if (section.getChild(TYPE_KEY) == null)
                    return;

                if (section.getChild(LAYER_KEY) != null && Util.getChildValue(LAYER_KEY, section).equals(TRUE_KEY))
                    return;

                System.out.println("+- Parsing: " + section.getName());
                System.err.println("+- Parsing: " + section);

                try {
                    if (section.getChild(MODEL_KEY) != null
                            && Util.getChildValue(MODEL_KEY, section).equals(TRUE_KEY)) {
                        System.out.println("| Generating Model");
                        System.err.println("| Generating Model");
                        if (Util.getChildValue(TYPE_KEY, section).equals(TILE_KEY)) {
                            Generators.generateBlockModel(section, modid);
                        } else if (Util.getChildValue(TYPE_KEY, section).equals(ITEM_KEY)) {
                            Generators.generateItemModel(section, modid);
                        }
                    }

                    if (section.getChild(BLOCKSTATE_KEY) != null
                            && Util.getChildValue(BLOCKSTATE_KEY, section).equals(TRUE_KEY)) {
                        System.out.println("| Generating Blockstate");
                        System.err.println("| Generating Blockstate");
                        if (Util.getChildValue(TYPE_KEY, section).equals(TILE_KEY)) {
                            Generators.generateBlockstate(section, modid);
                        }
                    }

                    if (section.getChild(LOCALE_KEY) != null
                            && Util.getChildValue(LOCALE_KEY, section).equals(TRUE_KEY)) {
                        System.out.println("| Generating Locale");
                        System.err.println("| Generating Locale");
                        Generators.generateLocalisation(section, modid);
                    }

                    if ((section.getChild(TEXTURE_KEY) != null
                            && Util.getChildValue(TEXTURE_KEY, section).equals(TRUE_KEY))) {
                        System.out.println("| Generating Textures");
                        System.err.println("| Generating Textures");
                        Generators.generateTexture(section, modid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
