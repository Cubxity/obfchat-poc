# obfchat-poc

This PoC demonstrates how the chat can be obfuscated on the server, while remaining signed.
This targets the Minecraft version 1.19.1-pre4.

https://user-images.githubusercontent.com/27609129/178434755-2ff5efbe-9427-4a76-ba8a-13eb02ad29af.mp4

*Do note that there will be a warning screen and a resource pack prompt, which were not shown in this video.*

The idea was brought up to me by [Machine-Maker](https://github.com/Machine-Maker).

## Setting Up

1. Run [ObfchatGenerator](src/test/java/dev/cubxity/mods/obfchat/gen/ObfchatGenerator.java)
2. Set up a server with `previews-chat` set to true in serrver.properties and the mod installed
3. Ensure that `glyphs.json` is in the server's root directory (default: `run`)
4. Archive the resource pack in `run/resourcepacks/glyph`
5. Force the resource pack by setting `require-resource-pack` to true and by setting `resource-pack` to a URL with the
   resource pack.
6. Start the server and join the server

## Why?

This shows how the server can obfuscate the chat using the decoration/preview feature.
With help of a custom resource pack, the chat is shown normally to the users.

However, in reality, the actual message (which is signed) is gibberish.
Meaning, it will be nearly impossible to report those.

Furthermore, a malicious server can potentially use this to make it look like a player is typing a legitimate message.
For example, turning "love" into "hate" by using character mapping and resource pack.

## Potential Pitfalls

* Any client that tries to get around the required resource pack will have unreadable chat
* The server log is obfuscated, however this can be mitigated with a mod/plugin
* This can potentially be mitigated by Mojang by figuring out the character mapping. A workaround for this would be to
  rotate the mapping periodically. Furthermore, the server can also assign each player to a specific unicode range.
* The server can potentially be banned from the session server by Mojang