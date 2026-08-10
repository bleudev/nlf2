package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.ClientStorageKt.webcamEnabled;

@Environment(EnvType.CLIENT)
@Mixin(Window.class)
public class WindowMixin {
    @WrapMethod(method = "setTitle")
    private void modifyTitle(String title, Operation<Void> original) {
        String var10001 = title.replace("Minecraft*", "Minecraft");
        var10001 = var10001.replace("Minecraft", "\"Minecraft\"");
        if (webcamEnabled) {
            var10001 = "EYES ARE WATCHING";
        }
        original.call(var10001);
    }
}
