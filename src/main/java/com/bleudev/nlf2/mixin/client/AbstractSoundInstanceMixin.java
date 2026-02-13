package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.ClientStorageKt.should_not_play_any_sound;

@Environment(EnvType.CLIENT)
@Mixin(AbstractSoundInstance.class)
public class AbstractSoundInstanceMixin {
    @ModifyReturnValue(method = "getVolume", at = @At("RETURN"))
    private float noSound(float original) {
        if (should_not_play_any_sound) return 0f;
        return original;
    }
}
