package com.bleudev.nlf2.mixin;

import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
    @ModifyVariable(method = "useOn", at = @At("STORE"), ordinal = 0)
    private BlockPattern.BlockPatternMatch doNOTcreatePortal(BlockPattern.BlockPatternMatch original) {
        return null;
    }
}
