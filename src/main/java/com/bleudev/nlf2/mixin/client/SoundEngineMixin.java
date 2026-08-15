package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getSoundFinalVolume;

@Environment(EnvType.CLIENT)
@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
    private void modifyVolumeInStory(SoundInstance instance, CallbackInfoReturnable<Float> cir) {
        float var10001 = cir.getReturnValue();
        float var10002 = getSoundFinalVolume(instance, var10001);
        if (var10001 != var10002) {
            cir.setReturnValue(var10002);
        }
    }
}
