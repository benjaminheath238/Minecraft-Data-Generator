package com.bjmh.mcdg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.bjmh.lib.io.config.ConfigSection;

public class Generators {
    public static void generateBlockstate(ConfigSection section, String modid) {
        File file = new File((System.getProperty("user.dir") + "/assets/" + modid + "/blockstates/" + name + ".json").replace("/", FILE_SEPARATOR));
        FileWriter writer = new FileWriter(file);

        writer.write("{\n");
        writer.write("  \"forge_marker\": 1,\n");
        writer.write("  \"defaults\": {\n");
        writer.write("    \"model\": \"" + modid + ":" + (path.startsWith("/") ? removeCharacter(path, 0) : path) + (path.endsWith("/") ? "" : "/") + name + "\"\n");
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

        writer.close();
    }

    public static void generateBlockModel(ConfigSection section, String modid) {
        
    }

    public static void generateItemModel(ConfigSection section, String modid) {
        
    }

    public static void generateLocalisation(ConfigSection section, String modid) {
        
    }

    public static void generateTexture(ConfigSection section, String modid) {
        
    }
}
