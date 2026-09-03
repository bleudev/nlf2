package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.Nlf2ConstKt.RAINBOW_ITEMS_CHECKER;
import static com.bleudev.nlf2.client.ClientStorageKt.getUnknownRainbowEffectColor;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @ModifyReturnValue(method = "getStyledHoverName", at = @At("RETURN"))
    private Component modifyColor(Component original) {
        return RAINBOW_ITEMS_CHECKER.invoke(getItem()) ? original.copy().withColor(getUnknownRainbowEffectColor()) : original;
    }
}
