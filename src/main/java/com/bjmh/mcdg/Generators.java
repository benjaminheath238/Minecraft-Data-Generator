package com.bjmh.mcdg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigNode;
import com.bjmh.lib.io.config.ConfigSection;

public class Generators {
    private static boolean lfe = false;

    public static void generateBlockstate(ConfigSection section, String modid) throws IOException {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "blockstates")).mkdirs();

        File file = new File(Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                Main.USER_DIR, "assets", modid, "blockstates"));

        FileWriter writer = new FileWriter(file);

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
    }

    public static void generateBlockModel(ConfigSection section, String modid) throws IOException {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "models",
                "block", Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section)))).mkdirs();

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                        Main.USER_DIR,
                        "assets", modid, "models", "block",
                        Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))));

        FileWriter writer = new FileWriter(file);

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
    }

    public static void generateItemModel(ConfigSection section, String modid) throws IOException {
        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "models", "item",
                Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section)))).mkdirs();

        File file = new File(
                Util.createSystemPath(Util.getChildValue(Main.REGISTRY_KEY, section) + ".json",
                        Main.USER_DIR,
                        "assets", modid, "models", "item",
                        Util.addPathCorrection(Util.getChildValue(Main.PATH_KEY, section))));

        FileWriter writer = new FileWriter(file);

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
    }

    public static void generateLocalisation(ConfigSection section, String modid) throws IOException {

        new File(Util.createSystemPath("", Main.USER_DIR, "assets", modid, "lang")).mkdirs();

        File file = new File(Util.createSystemPath("en_us.lang", Main.USER_DIR, "assets", modid, "lang"));
        if (lfe == false)
            file.delete();
        FileWriter writer = new FileWriter(file, true);

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
    }

    public static void generateTexture(ConfigSection section, String modid) throws IOException {
        BufferedImage base = new BufferedImage(
                Util.getChildValue(Main.SIZE_KEY, section) != null
                        ? Integer.valueOf(Util.getChildValue(Main.SIZE_KEY, section).split("x")[0])
                        : 16,
                Util.getChildValue(Main.SIZE_KEY, section) != null
                        ? Integer.valueOf(Util.getChildValue(Main.SIZE_KEY, section).split("x")[1])
                        : 16,
                6);

        for (int i = 0; true; i++) {
            if (section.getChild(Main.LAYER_KEY + "_" + i) == null)
                break;

            ConfigNode layerNode = Main.CONFIG
                    .getChild(Main.CONFIG.newConfigPath(Util.getChildValue(Main.LAYER_KEY + "_" + i, section)));

            System.err.println("| Writting layer: " + layerNode);

            if (!(layerNode instanceof ConfigSection))
                break;

            ConfigSection layer = (ConfigSection) layerNode;

            BufferedImage image = Util.loadImage(
                    Util.getChildValue(Main.PATH_KEY, layer),
                    Util.getPathFromType(Util.getChildValue(Main.TYPE_KEY, layer)),
                    Util.getChildValue(Main.REGISTRY_KEY, layer),
                    modid);

            System.out.println("| Writting layer: " + layer.getName() + ", To: " + section.getName());

            Util.writeLayer(base, image, Util.getChildValue(Main.TRANSPARENT_KEY, layer));
        }

        Util.saveImage(base, Util.getChildValue(Main.PATH_KEY, section),
                Util.getPathFromType(Util.getChildValue(Main.TYPE_KEY, section)),
                Util.getChildValue(Main.REGISTRY_KEY, section),
                modid);
    }
}
