package com.bleudev.nlf2.mixin;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PortalShape.class)
public class PortalShapeMixin {
    @Inject(method = "createPortalBlocks", at = @At("HEAD"), cancellable = true)
    private void doNOTcreatePortalBlocks(LevelAccessor level, CallbackInfo ci) {
        ci.cancel();
    }
}
