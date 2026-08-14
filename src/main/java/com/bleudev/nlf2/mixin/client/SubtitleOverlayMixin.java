package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(SubtitleOverlay.class)
public class SubtitleOverlayMixin {
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SubtitleOverlay$Subtitle;isAudibleFrom(Lnet/minecraft/world/phys/Vec3;)Z"))
    private boolean makeEmptySubtitlesNotAudible(SubtitleOverlay.Subtitle instance, Vec3 camera, Operation<Boolean> original) {
        if (instance.getText().getString().isBlank()) {
            return false;
        }
        else {
            return instance.isAudibleFrom(camera);
        }
    }
}
