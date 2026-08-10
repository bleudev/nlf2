package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static com.bleudev.nlf2.client.ClientStorageKt.getShouldRenderStory;
import static com.bleudev.nlf2.client.custom.Nlf2Sounds.storySounds;
import static com.bleudev.nlf2.client.sys.anim.StoryAnimation.getOtherVolume;

@Environment(EnvType.CLIENT)
@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Shadow
    @Final
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Shadow
    protected abstract float calculateVolume(SoundInstance instance);

    @Inject(method = "tick", at = @At("TAIL"))
    private void modifyVolumesInStory(boolean paused, CallbackInfo ci) {
        if (getShouldRenderStory()) {
            this.instanceToChannel.forEach((soundInstance, channelHandle) -> {
                float newVolume = this.calculateVolume(soundInstance);
                channelHandle.execute((channel) -> channel.setVolume(newVolume));
            });
        }
    }

    @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
    private void modifyVolumeInStory(SoundInstance instance, CallbackInfoReturnable<Float> cir) {
        final Sound sound = instance.getSound();
        boolean bl = getShouldRenderStory();
        if (sound != null) {
            if (storySounds().stream().map(SoundEvent::location).toList().contains(sound.getLocation())) {
                bl = false;
            }
        }
        if (bl) {
            cir.setReturnValue(cir.getReturnValue() * getOtherVolume());
        }
    }
}
