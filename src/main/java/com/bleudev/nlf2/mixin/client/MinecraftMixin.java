package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.ERROR;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.webcamEnabled;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "disconnectFromWorld", at = @At("HEAD"), cancellable = true)
    private void wrong(Component message, CallbackInfo ci) throws ERROR {
        ci.cancel();
        throw new ERROR("...");
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onScreenClose(Screen screen, CallbackInfo ci) {
        if (screen == null) webcamEnabled = false;
    }
}
