package com.bjmh.mcdg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigPath;
import com.bjmh.lib.io.config.ConfigSection;

public class Generators {
    private Generators() {
    }

    private static boolean lfe = false;

    public static void generateBlockstate(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, Main.ASSETS_PATH, modid, "blockstates")).mkdirs();

        System.err.println("| Creating Blockstate File");

        File file = new File(Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + Main.JSON_PATH,
                Main.USER_DIR, Main.ASSETS_PATH, modid, "blockstates"));

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");
            writer.write("  \"forge_marker\": 1,\n");
            writer.write("  \"defaults\": {\n");
            writer.write("    \"model\": \"" + modid + ":"
                    + (Util.getChildValue(Main.PATH_KEY, section).startsWith("/")
                            ? Util.trimFromStart(Util.getChildValue(Main.PATH_KEY, section))
                            : Util.getChildValue(Main.PATH_KEY, section))
                    + (Util.getChildValue(Main.PATH_KEY, section).endsWith("/") ? ""
                            : "/")
                    + Util.getChildValue(Main.REGISTRY_KEY, section) + "\"\n");
            writer.write("  },\n");
            writer.write("  \"variants\": {\n");
            writer.write("    \"normal\": [{}],\n");
            writer.write("    \"inventory\": [{}],\n");
            writer.write("    \"facing\": {\n");
            writer.write("      \"north\": {},\n");
            writer.write("      \"south\": {\"y\": 180},\n");
            writer.write("      \"west\": {\"y\": 270},\n");
            writer.write("      \"east\": {\"y\": 90},\n");
            writer.write("      \"up\": {\"x\": -90},\n");
            writer.write("      \"down\": {\"x\": 90}\n");
            writer.write("    }\n");
            writer.write("  }\n");
            writer.write("}");

            writer.flush();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating blockstate for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));

            e.printStackTrace(System.err);
        }
    }

    public static void generateBlockModel(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, Main.ASSETS_PATH, modid, Main.MODELS_PATH,
                "block", Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section)))).mkdirs();

        System.err.println("| Creating Block Model File");

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + Main.JSON_PATH,
                        Main.USER_DIR,
                        Main.ASSETS_PATH, modid, Main.MODELS_PATH, "block",
                        Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))));

        try (FileWriter writer = new FileWriter(file)) {
            String side = modid + ":blocks"
                    + Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))
                    + Util.getChildValue(Main.REGISTRY_KEY, section) + "\"";

            writer.write("{\n");
            writer.write("  \"parent\": \"block/cube\",\n");
            writer.write("  \"textures\": {\n");
            writer.write("    \"particle\": \"" + side + ",\n");
            writer.write("    \"down\": \"" + side + ",\n");
            writer.write("    \"up\": \"" + side + ",\n");
            writer.write("    \"east\": \"" + side + ",\n");
            writer.write("    \"west\": \"" + side + ",\n");
            writer.write("    \"north\": \"" + side + ",\n");
            writer.write("    \"south\": \"" + side + "\n");
            writer.write("  }\n");
            writer.write("}");

            writer.flush();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating block model for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));

            e.printStackTrace(System.err);
        }
    }

    public static void generateItemModel(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, Main.ASSETS_PATH, modid, Main.MODELS_PATH, "item")).mkdirs();

        System.err.println("| Creating Item Model File");

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + Main.JSON_PATH,
                        Main.USER_DIR,
                        Main.ASSETS_PATH, modid, Main.MODELS_PATH, "item"));

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");
            writer.write("  \"parent\": \"item/generated\",\n");
            writer.write("  \"textures\": {\n");
            writer.write(
                    "    \"layer0\": \"" + modid + ":items"
                            + Util.addPathCorrection(
                                    Util.getChildValue(Main.PATH_KEY, section))
                            + Util.getChildValue(Main.REGISTRY_KEY, section) + "\"\n");
            writer.write("  }\n");
            writer.write("}");

            writer.flush();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating item model for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));

            e.printStackTrace(System.err);
        }
    }

    public static void generateLocalisation(ConfigSection section, String modid) {

        new File(Util.createSystemPath("", Main.USER_DIR, Main.ASSETS_PATH, modid, "lang")).mkdirs();

        System.err.println("| Added Locale Key-Value Pair");

        File file = new File(Util.createSystemPath("en_us.lang", Main.USER_DIR, Main.ASSETS_PATH, modid, "lang"));

        if (!lfe){
            try {
                java.nio.file.Files.delete(file.toPath());
            } catch (IOException e) {
                System.err.println("| A Exception occured while removing old localisation for");
            e.printStackTrace(System.err);
            }
            lfe = true;
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            if (section.getChild(Main.NAME_KEY) == null) {
                writer.write(Util.getChildValue(Main.TYPE_KEY, section) + "." + modid + "."
                        + Util.getChildValue(Main.REGISTRY_KEY, section) + ".name="
                        + Util.getNameFromRegistryName(
                                Util.getChildValue(Main.REGISTRY_KEY, section))
                        + "\n");
            } else {
                if (Util.doesChildValueEqual("itemGroup", Main.TYPE_KEY, section)) {
                    writer.write(Util.getChildValue(Main.TYPE_KEY, section) + "."
                            + Util.getChildValue(Main.REGISTRY_KEY, section) + "="
                            + Util.getChildValue(Main.NAME_KEY, section) + "\n");
                } else {
                    writer.write(Util.getChildValue(Main.TYPE_KEY, section) + "." + modid + "."
                            + Util.getChildValue(Main.REGISTRY_KEY, section) + ".name="
                            + Util.getChildValue(Main.NAME_KEY, section) + "\n");
                }
            }

            writer.flush();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating localisation for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));

            e.printStackTrace(System.err);
        }
    }

    public static void generateTexture(ConfigSection section, String modid) {
        BufferedImage base = new BufferedImage(
                Util.getChildValue(Main.SIZE_KEY, section) != null
                        ? Integer.valueOf(Util.getChildValue(Main.SIZE_KEY, section)
                                .split("x")[0])
                        : 16,
                Util.getChildValue(Main.SIZE_KEY, section) != null
                        ? Integer.valueOf(Util.getChildValue(Main.SIZE_KEY, section)
                                .split("x")[1])
                        : 16,
                6);

        System.err.println(
                "| Base Image Created: Size = " + base.getWidth() + "x" + base.getHeight() + ", Type = "
                        + base.getType());

        for (int i = 0; true; i++) {
            if (section.getChild(Main.LAYER_KEY + "_" + i) == null)
                break;

            ConfigNode layerNode = Main.MOD_CONFIG
                    .getChild(new ConfigPath(Util.getChildValue(Main.LAYER_KEY + "_" + i, section)));

            if (!(layerNode instanceof ConfigSection))
                break;

            ConfigSection layer = (ConfigSection) layerNode;

            BufferedImage image = null;
            try {
                System.err.println("| Loading Layer From File: " + layer.getName());

                image = Util.loadImage(
                        Util.getChildValue(Main.PATH_KEY, layer),
                        Util.getPathFromType(Util.getChildValue(Main.TYPE_KEY, layer)),
                        Util.getChildValue(Main.REGISTRY_KEY, layer),
                        modid);
            } catch (IOException e) {
                System.err.println("| A Exception occured while loading layer: "
                        + Util.getChildValue(Main.REGISTRY_KEY, layer));

                e.printStackTrace(System.err);
            }

            System.out.println("| Writting layer: " + layer.getName() + ", To: " + section.getName());

            Util.writeLayer(base, image, Util.getChildValue(Main.TRANSPARENT_KEY, layer));
        }

        System.err.println("| Saving Image To File");

        try {
            Util.saveImage(base, Util.getChildValue(Main.PATH_KEY, section),
                    Util.getPathFromType(Util.getChildValue(Main.TYPE_KEY, section)),
                    Util.getChildValue(Main.REGISTRY_KEY, section),
                    modid);
        } catch (IOException e) {
            System.err.println("| A Exception occured while saving texture: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));

            e.printStackTrace(System.err);
        }
    }
}
