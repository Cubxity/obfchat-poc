package dev.cubxity.mods.obfchat.mixin.network.chat;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerChatMessage.class)
public abstract class MixinPlayerChatMessage {
    @Inject(
        method = "write",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeOptional(Ljava/util/Optional;Lnet/minecraft/network/FriendlyByteBuf$Writer;)V"),
        cancellable = true
    )
    private void write(FriendlyByteBuf friendlyByteBuf, CallbackInfo ci) {
        friendlyByteBuf.writeOptional(Optional.empty(), FriendlyByteBuf::writeComponent);
        ci.cancel();
    }
}
