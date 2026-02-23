package com.bleudev.nlf2.mixin.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static net.minecraft.client.renderer.PostChain.MAIN_TARGET_ID;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private CrossFrameResourcePool resourcePool;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void renderAdditionalShader(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (shaderLostVesselControl) renderPostEffect(getIdentifier("lost_vessel_control"));
        if (shaderBorderLag) renderPostEffect(getIdentifier("border_lag"));
        if (shaderBorderEnd) renderPostEffect(getIdentifier("border_end"));
        if (shaderAnaglyph) renderPostEffect(getIdentifier("anaglyph"));
        if (shaderRedMaj) renderPostEffect(getIdentifier("redmaj"));
    }

    @Unique
    private void renderPostEffect(Identifier postEffectId) {
        var this$GameRenderer = (GameRenderer) (Object) this;
        PostChain postChain = this$GameRenderer.getMinecraft().getShaderManager().getPostChain(postEffectId, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            var mainTarget = this$GameRenderer.getMinecraft().getMainRenderTarget();
            FrameGraphBuilder frame = new FrameGraphBuilder();
            PostChain.TargetBundle targets = PostChain.TargetBundle.of(MAIN_TARGET_ID, frame.importExternal("main", mainTarget));
            postChain.addToFrame(frame, mainTarget.width, mainTarget.height, targets);
            frame.execute(resourcePool);
        }
    }
}
