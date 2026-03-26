package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MusicManager.class)
public class MusicManagerMixin {
    @Inject(method = "startPlaying", at = @At("HEAD"), cancellable = true)
    private void cancelPlay(Music music, CallbackInfo ci) {
        ci.cancel();
    }
}
