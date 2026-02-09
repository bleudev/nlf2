package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.util.InjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderBlack(GuiGraphics graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        black(graphics);
    }
}
