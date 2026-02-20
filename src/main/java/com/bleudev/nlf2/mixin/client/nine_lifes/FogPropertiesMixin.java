package com.bleudev.nlf2.mixin.client.nine_lifes;

import com.bleudev.nine_lifes.client.FogProperitesKt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FogProperitesKt.class)
public class FogPropertiesMixin {
    @Inject(method = "_get_skyColor_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifySkyColor(int color, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0xff000000);
    }
    @Inject(method = "_get_fogColor_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogColor(Vector4f color, CallbackInfoReturnable<Vector4f> cir) {
        cir.setReturnValue(new Vector4f(0f, 0f, 0f, 1f));
    }
    @Inject(method = "_get_fogStart_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogStart(float start, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(start * .005f);
    }
    @Inject(method = "_get_fogEnd_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogEnd(float end, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(end * .005f);
    }
}
