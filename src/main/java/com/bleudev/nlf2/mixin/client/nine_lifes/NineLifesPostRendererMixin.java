package com.bleudev.nlf2.mixin.client.nine_lifes;

import com.bleudev.nine_lifes.client.render.NineLifesPostRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.getShaderEvent101;

@Environment(EnvType.CLIENT)
@Mixin(NineLifesPostRenderer.class)
public abstract class NineLifesPostRendererMixin {
    @Shadow
    protected abstract void renderPostEffect(Identifier postEffectId);

    @Inject(method = "render", at = @At("TAIL"))
    private void renderNlf2PostEffects(CallbackInfo ci) {
        if (getShaderEvent101())
            renderPostEffect(getIdentifier("event101"));
    }
}
