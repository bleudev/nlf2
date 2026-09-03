package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.Nlf2ConstKt.RAINBOW_ITEMS_CHECKER;
import static com.bleudev.nlf2.client.ClientStorageKt.getUnknownRainbowEffectColor;

@Environment(EnvType.CLIENT)
@Mixin(Hud.class)
public class HudMixin {
    @Shadow
    private ItemStack lastToolHighlight;

    @WrapOperation(method = "extractSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;textWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"))
    private void modifyColor(GuiGraphicsExtractor instance, Font font, Component str, int textX, int textY, int textWidth, int textColor, Operation<Void> original, @Local(name = "alpha") int alpha) {
        int newColor = textColor;
        if (RAINBOW_ITEMS_CHECKER.invoke(this.lastToolHighlight.getItem())) {
            newColor = getUnknownRainbowEffectColor();
        }
        newColor = ARGB.color(alpha, newColor);

        original.call(instance, font, str.copy().withColor(newColor), textX, textY, textWidth, textColor);
    }
}
