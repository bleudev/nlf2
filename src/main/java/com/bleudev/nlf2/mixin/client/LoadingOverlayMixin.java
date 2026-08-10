package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.sys.anim.IntroAnimation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void renderIntro(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        IntroAnimation.INSTANCE.render(graphics);
    }
}
