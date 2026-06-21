package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.border.BorderStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BorderStatus.class)
public class BorderStatusMixin {
    @ModifyReturnValue(method = "getColor", at = @At("RETURN"))
    private int modifyWorldBorderColor(int original) {
        return 16724016;
    }
}
