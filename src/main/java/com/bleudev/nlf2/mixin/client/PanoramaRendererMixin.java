package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PanoramaRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.util.InjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void doNOTrenderPanorama(GuiGraphics graphics, int width, int height, boolean shouldSpin, CallbackInfo ci) {
        black(graphics);
    }
}
