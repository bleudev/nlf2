package com.bleudev.nlf2.mixin.client.nine_lifes;

import com.bleudev.nine_lifes.client.FogProperitesKt;
import com.bleudev.nlf2.client.util.ClientFogRenderTransformEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nlf2.client.ClientStorageKt.getBlackFogEffect;

@Environment(EnvType.CLIENT)
@Mixin(FogProperitesKt.class)
public class FogPropertiesMixin {
    @Inject(method = "_get_skyColor_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifySkyColor(int original, CallbackInfoReturnable<Integer> cir) {
        int current = cir.getReturnValue();
        cir.setReturnValue(ARGB.srgbLerp(getBlackFogEffect(), current, ARGB.color(ARGB.alpha(current), 0)));
    }
    @Inject(method = "_get_fogColor_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogColor(Vector4f original, CallbackInfoReturnable<Vector4f> cir) {
        cir.setReturnValue(ClientFogRenderTransformEvents.FOG_COLOR.invoker().invoke(original, cir.getReturnValue()));
    }
    @Inject(method = "_get_fogStart_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogStart(float original, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ClientFogRenderTransformEvents.FOG_START.invoker().invoke(original, cir.getReturnValue()));
    }
    @Inject(method = "_get_fogEnd_$lambda$0", at = @At("RETURN"), cancellable = true)
    private static void modifyFogEnd(float original, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ClientFogRenderTransformEvents.FOG_END.invoker().invoke(original, cir.getReturnValue()));
    }
}
