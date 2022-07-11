package dev.cubxity.mods.obfchat.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ObfchatServer implements DedicatedServerModInitializer {
    // This is the destination "unicode page".
    // The page number (represented in hex) can be found in the file name:
    // For example: "unicode_page_4c.png"
    private static final int START_CODE_POINT = 0x4c * 256;
    private final Gson gson = new Gson();

    @Override
    public void onInitializeServer() {
        try {
            var json = Files.readString(Path.of("glyphs.json"));
            var array = gson.fromJson(json, JsonArray.class);
            var map = new int[256];
            for (int i = 0; i < 255; i++) {
                map[i] = array.get(i).getAsInt() + START_CODE_POINT;
            }

            ServerMessageDecoratorEvent.EVENT.register((serverPlayer, component) ->
                CompletableFuture.completedFuture(modify(component, map)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MutableComponent modify(Component component, int[] map) {
        var contents = component.getContents();
        if (contents instanceof LiteralContents literal) {
            var builder = new StringBuilder();
            literal.text().codePoints().forEach(codePoint -> {
                if (codePoint > 255) {
                    builder.appendCodePoint(codePoint);
                } else {
                    builder.appendCodePoint(map[codePoint]);
                }
            });
            contents = new LiteralContents(builder.toString());
        }

        var newComponent = MutableComponent.create(contents);
        newComponent.setStyle(component.getStyle());

        for (var sibling : component.getSiblings()) {
            newComponent.append(modify(sibling, map));
        }

        return newComponent;
    }
}
