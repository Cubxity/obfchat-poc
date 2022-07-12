# obfchat-poc

This PoC demonstrates how the chat can be obfuscated on the server, while remaining signed.
This targets the Minecraft version 1.19.1-pre4.

https://user-images.githubusercontent.com/27609129/178434755-2ff5efbe-9427-4a76-ba8a-13eb02ad29af.mp4

## Setting up

1. Run [ObfchatGenerator](src/test/java/dev/cubxity/mods/obfchat/gen/ObfchatGenerator.java)
2. Set up a server with `previews-chat` set to true in serrver.properties and the mod installed
3. Ensure that `glyphs.json` is in the server's root directory (default: `run`)
4. Archive the resource pack in `run/resourcepacks/glyph`
5. Force the resource pack by setting `require-resource-pack` to true and by setting `resource-pack` to a URL with the
   resource pack.
6. Start the server and join the server
