package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.ERROR;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "disconnectFromWorld", at = @At("HEAD"), cancellable = true)
    private void wrong(Component message, CallbackInfo ci) throws ERROR {
        ci.cancel();
        throw new ERROR("WRONG");
    }
}
