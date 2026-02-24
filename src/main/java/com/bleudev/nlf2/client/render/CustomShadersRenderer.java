package com.bleudev.nlf2.client.render;

import com.bleudev.nlf2.client.api.UniformEditorRegistryImpl;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static net.minecraft.client.renderer.PostChain.MAIN_TARGET_ID;

public class CustomShadersRenderer {
    private final CrossFrameResourcePool resourcePool;
    private final Supplier<Minecraft> minecraftSupplier;

    public CustomShadersRenderer(CrossFrameResourcePool resourcePool, Supplier<Minecraft> minecraftSupplier) {
        this.resourcePool = resourcePool;
        this.minecraftSupplier = minecraftSupplier;

        UniformEditorRegistryImpl.internalInitBuffers();
    }

    public void render() {
        UniformEditorRegistryImpl.internalUpdateBuffers();

        if (shaderLostVesselControl) renderCustomPostEffect("lost_vessel_control");
        if (shaderBorderLag) renderCustomPostEffect("border_lag");
        if (shaderBorderEnd) renderCustomPostEffect("border_end");

        if (shaderRedMaj && shaderRedMajUniformStrength > 0)
            renderCustomPostEffect("redmaj");
        if (shaderAnaglyph && shaderAnaglyphUniformX != 0 && shaderAnaglyphUniformY != 0)
            renderCustomPostEffect("anaglyph");
    }

    private void renderCustomPostEffect(String name) {
        renderPostEffect(getIdentifier(name));
    }

    private void renderPostEffect(Identifier postEffectId) {
        PostChain postChain = minecraftSupplier.get().getShaderManager().getPostChain(postEffectId, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            var mainTarget = minecraftSupplier.get().getMainRenderTarget();
            FrameGraphBuilder frame = new FrameGraphBuilder();
            PostChain.TargetBundle targets = PostChain.TargetBundle.of(MAIN_TARGET_ID, frame.importExternal("main", mainTarget));
            postChain.addToFrame(frame, mainTarget.width, mainTarget.height, targets);
            frame.execute(resourcePool);
        }
    }
}
