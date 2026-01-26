package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.IntroHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void renderIntro(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        IntroHelper.INSTANCE.render(guiGraphics);
    }
}
