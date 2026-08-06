package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.Nlf2Sounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

import static com.bleudev.nlf2.client.ClientStorageKt.getShouldRenderStory;
import static com.bleudev.nlf2.client.sys.anim.StoryAnimation.getOtherVolume;

@Environment(EnvType.CLIENT)
@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
    private void modifyVolumeInStory(SoundInstance instance, CallbackInfoReturnable<Float> cir) {
        final Sound sound = instance.getSound();
        boolean bl = getShouldRenderStory();
        if (sound != null) {
            if (Stream.of(Nlf2Sounds.MUS_STORY, Nlf2Sounds.BROKE)
                    .map(SoundEvent::location).toList().contains(sound.getLocation())) {
                bl = false;
            }
        }
        if (bl) {
            cir.setReturnValue(getOtherVolume());
        }
    }
}
