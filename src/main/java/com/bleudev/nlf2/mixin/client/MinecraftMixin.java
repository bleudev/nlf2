package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import unknown.depths.r.man.ROHJEN67SW51;

import static com.bleudev.nlf2.client.ClientStorageKt.*;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "disconnectFromWorld", at = @At("HEAD"), cancellable = true)
    private void wrong(Component message, CallbackInfo ci) throws ROHJEN67SW51 {
        ci.cancel();
        throw new ROHJEN67SW51("...");
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onScreenClose(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            webcamEnabled = false;
            pause_screen_red_screen_effect = false;
            should_not_play_any_sound = false;
        }
    }
}
