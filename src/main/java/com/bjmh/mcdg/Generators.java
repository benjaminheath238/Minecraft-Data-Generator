package com.bjmh.mcdg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigSection;

public class Generators {
    private static boolean lfe = false;

    public static void generateBlockstate(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "blockstates")).mkdirs();

        System.err.println("| Creating Blockstate File");

        File file = new File(Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                Main.USER_DIR, "assets", modid, "blockstates"));

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");
            writer.write("  \"forge_marker\": 1,\n");
            writer.write("  \"defaults\": {\n");
            writer.write("    \"model\": \"" + modid + ":"
                    + (Util.getChildValue(Main.PATH_KEY, section).startsWith(Main.FILE_SEPARATOR)
                            ? Util.trimFromStart(Util.getChildValue(Main.PATH_KEY, section))
                            : Util.getChildValue(Main.PATH_KEY, section))
                    + (Util.getChildValue(Main.PATH_KEY, section).endsWith(Main.FILE_SEPARATOR) ? ""
                            : Main.FILE_SEPARATOR)
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
            writer.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating blockstate for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }

    public static void generateBlockModel(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "models",
                "block", Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section)))).mkdirs();

        System.err.println("| Creating Block Model File");

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                        Main.USER_DIR,
                        "assets", modid, "models", "block",
                        Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))));

        try (FileWriter writer = new FileWriter(file)) {
            String SIDE = modid + ":blocks" + Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))
                    + Util.getChildValue(Main.REGISTRY_KEY, section) + "\"";

            writer.write("{\n");
            writer.write("  \"parent\": \"block/cube\",\n");
            writer.write("  \"textures\": {\n");
            writer.write("    \"particle\": \"" + SIDE + ",\n");
            writer.write("    \"down\": \"" + SIDE + ",\n");
            writer.write("    \"up\": \"" + SIDE + ",\n");
            writer.write("    \"east\": \"" + SIDE + ",\n");
            writer.write("    \"west\": \"" + SIDE + ",\n");
            writer.write("    \"north\": \"" + SIDE + ",\n");
            writer.write("    \"south\": \"" + SIDE + "\n");
            writer.write("  }\n");
            writer.write("}");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating block model for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }

    public static void generateItemModel(ConfigSection section, String modid) {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "models", "item",
                Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section)))).mkdirs();

        System.err.println("| Creating Item Model File");

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                        Main.USER_DIR,
                        "assets", modid, "models", "item",
                        Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))));

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\n");
            writer.write("  \"parent\": \"item/generated\",\n");
            writer.write("  \"textures\": {\n");
            writer.write(
                    "    \"layer0\": \"" + modid + ":items"
                            + Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))
                            + Util.getChildValue(Main.REGISTRY_KEY, section) + "\"\n");
            writer.write("  }\n");
            writer.write("}");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating item model for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }

    public static void generateLocalisation(ConfigSection section, String modid) {

        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "lang")).mkdirs();

        System.err.println("| Added Locale Key-Value Pair");

        File file = new File(Util.createSystemPath("en_us.lang", Main.USER_DIR, "assets", modid, "lang"));

        if (lfe == false)
            file.delete();

        try (FileWriter writer = new FileWriter(file, true)) {
            if (section.getChild(Main.NAME_KEY) == null) {
                writer.write(Util.getChildValue(Main.TYPE_KEY, section) + "." + modid + "."
                        + Util.getChildValue(Main.REGISTRY_KEY, section) + ".name="
                        + Util.getNameFromRegistryName(Util.getChildValue(Main.REGISTRY_KEY, section))
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

            lfe = true;

            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while generating localisation for: "
                    + Util.getChildValue(Main.REGISTRY_KEY, section));
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
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

        System.err.println("| Base Image Created: Size = " + base.getWidth() + "x" + base.getHeight() + ", Type = "
                + base.getType());

        for (int i = 0; true; i++) {
            if (section.getChild(Main.LAYER_KEY + "_" + i) == null)
                break;

            ConfigNode layerNode = Main.MOD_CONFIG
                    .getChild(Main.MOD_CONFIG.newConfigPath(
                            Util.getChildValue(Main.LAYER_KEY + "_" + i, section)));

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
                System.err.println("{");
                e.printStackTrace(System.err);
                System.err.println("}");
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
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }
}
