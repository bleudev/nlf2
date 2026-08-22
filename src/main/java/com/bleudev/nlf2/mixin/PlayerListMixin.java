package com.bleudev.nlf2.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
    private void modifyBroadcastSystemMessage(Component message, Function<ServerPlayer, Component> playerMessages, boolean overlay, CallbackInfo ci) {
        if (message.getContents() instanceof TranslatableContents contents) {
            String key = contents.getKey();
            if (List.of("multiplayer.player.joined", "multiplayer.player.left").contains(key)) ci.cancel();
            else if (key.startsWith("death.")) ci.cancel();
        }
    }
}
