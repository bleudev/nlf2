package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void doNOTrender(GuiGraphics graphics, int screenWidth, Scoreboard scoreboard, @Nullable Objective displayObjective, CallbackInfo ci) {
        ci.cancel();
    }
}
