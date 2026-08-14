package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Panorama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.util.ClientInjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(Panorama.class)
public class PanoramaMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void doNotExtractPanorama(GuiGraphicsExtractor graphics, int width, int height, CallbackInfo ci) {
        black(graphics);
    }
}
