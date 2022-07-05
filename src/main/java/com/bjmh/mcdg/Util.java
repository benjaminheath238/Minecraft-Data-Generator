package com.bjmh.mcdg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.imageio.ImageIO;

import com.bjmh.lib.io.config.ConfigOption;
import com.bjmh.lib.io.config.ConfigSection;

public class Util {
    public static ConfigSection getChildAsConfigSection(String name, ConfigSection section) {
        return (ConfigSection) section.getChild(name);
    }

    public static ConfigOption getChildAsConfigOption(String name, ConfigSection section) {
        return (ConfigOption) section.getChild(name);
    }

    public static String getChildValue(String name, ConfigSection section) {
        if (section.getChild(name) == null) {
            return null;
        } else {
            return ((ConfigOption) section.getChild(name)).getValue();
        }
    }

    public static boolean doesChildValueEqual(String value, String name, ConfigSection section) {
        return value.equals(Util.getChildValue(name, section));
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
        for (int x = 0; x < base.getWidth(); x++) {
            for (int y = 0; y < base.getHeight(); y++) {
                Color lc = new Color(layer.getRGB(x, y), true);
                if (lc.getRGB() >= 0)
                    continue;

                if ((Main.TRUE_VAL).equals(transparent)) {
                    Color bc = new Color(base.getRGB(x, y), true);
                    base.setRGB(x, y, new Color(
                            (lc.getRed() + bc.getRed()) / 2,
                            (lc.getGreen() + bc.getGreen()) / 2,
                            (lc.getBlue() + bc.getBlue()) / 2,
                            (lc.getAlpha() + bc.getAlpha()) / 2).getRGB());
                } else {
                    base.setRGB(x, y, lc.getRGB());
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

        return ImageIO.read(file);
    }

    public static void saveImage(BufferedImage image, String path, String type, String name, String modid)
            throws IOException {
        path = removePathCorrection(path);

        new File(createSystemPath("", Main.USER_DIR, "assets", modid, "textures", type, path)).mkdirs();

        File file = new File(createSystemPath(name + ".png", Main.USER_DIR, "assets", modid, "textures", type, path));

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

    public static void writeToFile(List<String> data, String path) {
        try (FileWriter writer = new FileWriter(new File(path))) {
            for (String line : data) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while writing to file: " + path);
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }

    public static void copyFile(File old, File copy) {
        try (BufferedReader oldReader = new BufferedReader(new FileReader(old)); FileWriter copyWriter = new FileWriter(copy)) {
            while (oldReader.ready()) {
                copyWriter.write(oldReader.readLine());
            }

            copyWriter.flush();
            copyWriter.close();
            oldReader.close();
        } catch (IOException e) {
            System.err.println("| A Exception occured while copying file: " + old + ", to: " + copy);
            System.err.println("{");
            e.printStackTrace(System.err);
            System.err.println("}");
        }
    }

    public static void createConfigIfAbsent() throws IOException, URISyntaxException {
        File location = new File(createSystemPath("mcdg.ini", Main.USER_DIR));

        if (location.exists()) return;

        copyFile(new File(Main.class.getClassLoader().getResource("mcdg.ini").toURI()), location);
    }

}
