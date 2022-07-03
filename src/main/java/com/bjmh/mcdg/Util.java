package com.bjmh.mcdg;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Util {
    public static ConfigSection getChildAsConfigSection(String name, ConfigSection from) {
        return (ConfigSection) from.getChild(name);
    }

    public static ConfigOption getChildAsConfigOption(String name, ConfigSection from) {
        return (ConfigOption) from.getChild(name);
    }

    public static String getChildValue(String name, ConfigSection from) {
        return ((ConfigOption) from.getChild(name)).getValue();
    }

    public static String createSystemPath(String name, String... path) {
        StringBuilder builder = new StringBuilder();
        for (String part : path) {
            builder.append(part);
            builder.append(Main.FILE_SEPARATOR);
        }
        return builder.append(name).toString();
    }

    public static String getNameFromRegistryName(String registry) {
        char[] chars = registry.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0)
                chars[i] = Character.toUpperCase(chars[i]);

            if (chars[i] == '_') {
                chars[i] = ' ';
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    public static String addPathCorrection(String path) {
        return (path.startsWith(Main.FILE_SEPARATOR) ? "" : Main.FILE_SEPARATOR) + path
                + (path.endsWith(Main.FILE_SEPARATOR) ? "" : Main.FILE_SEPARATOR);
    }

    public static String removePathCorrection(String path) {
        if (path.startsWith(Main.FILE_SEPARATOR)) {
            path = trimFromStart(path);
        } else if (path.endsWith(Main.FILE_SEPARATOR)) {
            path = trimFromEnd(path);
        }
        return path;
    }

    public static String trimFromStart(String line) {
        char[] chars = line.toCharArray();
        chars[0] = ' ';
        return new String(chars).trim();
    }

    public static String trimFromEnd(String line) {
        char[] chars = line.toCharArray();
        chars[chars.length - 1] = ' ';
        return new String(chars).trim();
    }

    public static BufferedImage writeLayer(BufferedImage base, BufferedImage layer, String transparent) {
        for (int x = 0; x < base.getWidth(); x++) { // Iterate through x-axis
            for (int y = 0; y < base.getHeight(); y++) { // Iterate throught the y-axis
                Color lc = new Color(layer.getRGB(x, y), true); // The colour at x,y on the layer
                if (lc.getRGB() >= 0) // If the value is more then 0 it is transparent and will not work
                    continue;

                if (transparent.equals(Main.FALSE_KEY)) { // If this layer is opaque
                    base.setRGB(x, y, lc.getRGB()); // Write the exact layer colour
                } else if (transparent.equals(Main.TRUE_KEY)) { // If this layer is transparent
                    Color bc = new Color(base.getRGB(x, y), true); // The colour at x,y ob the base
                    base.setRGB(x, y, new Color( // Take the average of the RGBA values
                            (lc.getRed() + bc.getRed()) / 2,
                            (lc.getGreen() + bc.getGreen()) / 2,
                            (lc.getBlue() + bc.getBlue()) / 2,
                            (lc.getAlpha() + bc.getAlpha()) / 2).getRGB());
                }
            }
        }

        layer.flush();
        base.flush();

        return base;
    }

    public static BufferedImage loadImage(String path, String type, String name, String modid) throws IOException {
        path = removePathCorrection(path);

        File file = new File(createSystemPath(name + ".png", Main.USER_DIR, "assets", modid, "textures", type, path));

        System.err.println("| Loading Image: " + file);

        return ImageIO.read(file);
    }

    public static void saveImage(BufferedImage image, String path, String type, String name, String modid)
            throws IOException {
        path = removePathCorrection(path);

        new File(createSystemPath("", Main.USER_DIR, "assets", modid, "textures", type, path)).mkdirs();

        File file = new File(createSystemPath(name + ".png", Main.USER_DIR, "assets", modid, "textures", type, path));

        System.err.println("| Saving Image: " + file);

        ImageIO.write(image, "png", file);
    }

    public static String getPathFromType(String type) {
        switch (type) {
            case "tile":
                return "blocks";
            case "item":
                return "items";
            default:
                return type;
        }
    }
}
