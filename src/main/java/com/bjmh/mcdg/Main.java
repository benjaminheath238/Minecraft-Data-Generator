package com.bjmh.mcdg;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import com.bjmh.lib.io.config.ConfigConsumer;
import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;
import com.bjmh.lib.io.config.Configuration;
import com.bjmh.lib.io.config.ParserMethod;
import com.bjmh.lib.io.config.ParserMethods;

public class Main {
    public static final String REGISTRY_KEY = "registry";

    public static final String MODEL_KEY = "model";
    public static final String LOCALE_KEY = "locale";
    public static final String BLOCKSTATE_KEY = "blockstate";
    public static final String TEXTURE_KEY = "texture";

    public static final String TYPE_KEY = "type";
    public static final String NAME_KEY = "name";
    public static final String PATH_KEY = "path";
    public static final String LAYER_KEY = "layer";
    public static final String TRANSPARENT_KEY = "transparent";
    public static final String SIZE_KEY = "size";

    public static final String TRUE_VAL = "true";
    public static final String FALSE_VAL = "false";
    public static final String TILE_VAL = "tile";
    public static final String ITEM_VAL = "item";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String USER_DIR = System.getProperty("user.dir");

    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Configuration GLOBAL_CONFIG = new Configuration("global");
    public static final Configuration MOD_CONFIG = new Configuration("mod");

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.setErr(new PrintStream(new File(Util.createSystemPath("latest.log", USER_DIR))));

        Util.createConfigIfAbsent();

        GLOBAL_CONFIG.parse(Util.createSystemPath("mcdg.ini", USER_DIR), ParserMethods.INI_PARSER_SIMPLE);

        String modid = Util.getChildValue("modid", GLOBAL_CONFIG);
        if (modid == null) {
            System.out.println("Enter The Mod ID");
            modid = SCANNER.nextLine();
        }

        String path = Util.getChildValue("path", GLOBAL_CONFIG);
        if (path == null) {
            System.out.println("Enter Config File Path");
            path = SCANNER.nextLine();
        }

        MOD_CONFIG.parse(path, new ParserMethod() {
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

        MOD_CONFIG.foreach(new ModConfConsumer(modid));
    }

    private static class ModConfConsumer implements ConfigConsumer {
        private final String modid;

        public ModConfConsumer(String modid) {
            this.modid = modid;
        }

        @Override
        public void accept(ConfigNode node) {

            if (!(node instanceof ConfigSection && node.getType().equals(ConfigNode.Type.COMPLEX_OPTION)))
                return;

            ConfigSection section = (ConfigSection) node;

            if (section.getChild(TYPE_KEY) == null) {
                return;
            } else if (Util.doesChildValueEqual(Main.TRUE_VAL, Main.LAYER_KEY, section)) {
                return;
            }

            System.out.println("+- Parsing: " + section.getName() + ", From: " + section.getParent().getName());

            try {
                if (Util.doesChildValueEqual(Main.TRUE_VAL, Main.MODEL_KEY, section)) {
                    System.out.println("| Generating Model");
                    if (Util.doesChildValueEqual(Main.TILE_VAL, Main.TYPE_KEY, section)) {
                        Generators.generateBlockModel(section, modid);
                    } else if (Util.doesChildValueEqual(Main.ITEM_VAL, Main.TYPE_KEY, section)) {
                        Generators.generateItemModel(section, modid);
                    }
                }

                if (Util.doesChildValueEqual(Main.TRUE_VAL, Main.BLOCKSTATE_KEY, section)) {
                    System.out.println("| Generating Blockstate");
                    if (Util.doesChildValueEqual(Main.TILE_VAL, Main.TYPE_KEY, section)) {
                        Generators.generateBlockstate(section, modid);
                    }
                }

                if (Util.doesChildValueEqual(Main.TRUE_VAL, Main.LOCALE_KEY, section)) {
                    System.out.println("| Generating Locale");
                    Generators.generateLocalisation(section, modid);
                }

                if (Util.doesChildValueEqual(Main.TRUE_VAL, Main.TEXTURE_KEY, section)) {
                    System.out.println("| Generating Textures");
                    Generators.generateTexture(section, modid);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };
}
