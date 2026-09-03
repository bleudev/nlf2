package com.bleudev.nlf2.mixin.client;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import unknown.depths.r.man.ROHJEN67SW51;

import static com.bleudev.nlf2.client.ClientStorageKt.getWindowBlockFullscreen;
import static com.bleudev.nlf2.client.ClientStorageKt.setWindowBlockedClose;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private Window window;

    @Inject(method = "disconnectFromWorld", at = @At("HEAD"), cancellable = true)
    private void wrong(Component message, CallbackInfo ci) throws ROHJEN67SW51 {
        ci.cancel();
        throw new ROHJEN67SW51("...");
    }

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    private void cancelClose(CallbackInfo ci) {
        if (getWindowBlockFullscreen()) {
            setWindowBlockedClose(true);
            this.window.setTitle("!!!"); // Sync
            ci.cancel();
        }
    }
}
