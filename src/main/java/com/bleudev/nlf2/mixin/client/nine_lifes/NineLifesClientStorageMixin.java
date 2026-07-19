package com.bleudev.nlf2.mixin.client.nine_lifes;

import com.bleudev.nine_lifes.client.NineLifesClientStorageKt;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.ClientStorageKt.*;

@Environment(EnvType.CLIENT)
@Mixin(NineLifesClientStorageKt.class)
public class NineLifesClientStorageMixin {
    @ModifyReturnValue(method = "getShaderAnaglyphX", at = @At("RETURN"))
    private static float modifyShaderAnaglyphX(float original) {
        return Math.max(original, getShaderAnaglyphUniformX());
    }
    @ModifyReturnValue(method = "getShaderAnaglyphY", at = @At("RETURN"))
    private static float modifyShaderAnaglyphY(float original) {
        return Math.max(original, getShaderAnaglyphUniformY());
    }
    @ModifyReturnValue(method = "getShaderRedMajStrength", at = @At("RETURN"))
    private static float modifyShaderRedMajStrength(float original) {
        return Math.max(original, getShaderRedMajUniformStrength());
    }
    // Shake
    @ModifyReturnValue(method = "getShakeStrength", at = @At("RETURN"))
    private static float modifyShakeStrength(float original) {
        return Math.max(original, getEarthquakeStrength());
    }
    @ModifyReturnValue(method = "getShakeSpeed", at = @At("RETURN"))
    private static float modifyShakeSpeed(float original) {
        return Math.max(original, getEarthquakeSpeed());
    }
}
