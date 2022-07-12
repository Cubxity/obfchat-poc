package dev.cubxity.mods.obfchat.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class ObfchatGenerator {
    // This is the unicode code point to start at. This will occupy 256 proceeding codepoints.
    private static final int START_CODE_POINT = 0x4c * 256;

    public static void main(String[] args) throws IOException {
        var source = ImageIO.read(ObfchatGenerator.class.getResourceAsStream("/font/ascii.png"));
        var dest = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        var graphics = dest.getGraphics();

        // Shuffle the glyphs
        var map = new ArrayList<>(IntStream.range(0, 256).boxed().toList());
        Collections.shuffle(map);

        // Map the glyphs to random places as defined above
        for (var sourceIndex = 0; sourceIndex < 256; sourceIndex++) {
            var sourceX = (sourceIndex % 16) * 8;
            var sourceY = (Math.floorDiv(sourceIndex, 16)) * 8;

            var destIndex = map.get(sourceIndex);
            var destX = (destIndex % 16) * 8;
            var destY = (Math.floorDiv(destIndex, 16)) * 8;

            var data = source.getSubimage(sourceX, sourceY, 8, 8);
            graphics.drawImage(data, destX, destY, null);
        }

        // Export glyphs and data
        var fontTextureDir = new File("run/resourcepacks/glyphs/assets/minecraft/textures/font/");
        fontTextureDir.mkdirs();
        ImageIO.write(dest, "PNG", new File(fontTextureDir, "glyphs.png"));

        var fontProvider = new JsonObject();
        fontProvider.addProperty("type", "bitmap");
        fontProvider.addProperty("file", "minecraft:font/glyphs.png");
        fontProvider.addProperty("ascent", 7);

        var fontProviderChars = new JsonArray();
        for (var row = 0; row < 16; row++) {
            var chars = new StringBuilder();
            for (var col = 0; col < 16; col++) {
                var index = row * 16 + col;
                chars.append(String.format("\\u%04X", START_CODE_POINT + index));
            }
            fontProviderChars.add(chars.toString());
        }
        fontProvider.add("chars", fontProviderChars);

        var fontProviders = new JsonArray();
        fontProviders.add(fontProvider);

        var fontJson = new JsonObject();
        fontJson.add("providers", fontProviders);

        var fontDir = Path.of("run/resourcepacks/glyphs/assets/minecraft/font/");
        Files.createDirectories(fontDir);

        // FIXME: is there a better way to do this?
        Files.writeString(fontDir.resolve("default.json"), fontJson.toString().replace("\\\\", "\\"));


        var glyphs = new JsonObject();
        glyphs.addProperty("offset", START_CODE_POINT);

        var glyphsMap = new JsonArray();
        for (int i : map) {
            glyphsMap.add(i);
        }
        glyphs.add("glyphs", glyphsMap);

        var runDir = Path.of("run");
        Files.createDirectories(runDir);
        Files.writeString(runDir.resolve("glyphs.json"), glyphs.toString());
    }
}
