package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.modifySoundSubtitle;

@Environment(EnvType.CLIENT)
@Mixin(SubtitleOverlay.class)
public class SubtitleOverlayMixin {
    @ModifyVariable(method = "onPlaySound", at = @At(value = "STORE", ordinal = 0), name = "text")
    private Component modifySubtitleColor(Component text, @Local(name = "sound", argsOnly = true) SoundInstance sound) {
        Sound var10001 = sound.getSound();
        if (var10001 != null) {
            return modifySoundSubtitle(var10001, text);
        }
        Identifier var10002 = sound.getIdentifier();
        return modifySoundSubtitle(var10002, text);
    }
}
