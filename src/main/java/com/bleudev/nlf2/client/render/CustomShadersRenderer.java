package com.bleudev.nlf2.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static net.minecraft.client.renderer.PostChain.MAIN_TARGET_ID;

public class CustomShadersRenderer {
    private final CrossFrameResourcePool resourcePool;
    private final Supplier<Minecraft> minecraftSupplier;

    public static final int ANAGLYPH_UBO_SIZE = new Std140SizeCalculator().putVec2().get();
    private final MappableRingBuffer anaglyph_ubo = new MappableRingBuffer(() -> "Anaglyph UBO", 130, ANAGLYPH_UBO_SIZE);

    public CustomShadersRenderer(CrossFrameResourcePool resourcePool, Supplier<Minecraft> minecraftSupplier) {
        this.resourcePool = resourcePool;
        this.minecraftSupplier = minecraftSupplier;
    }

    public void render() {
        if (shaderLostVesselControl) renderPostEffect(getIdentifier("lost_vessel_control"));
        if (shaderBorderLag) renderPostEffect(getIdentifier("border_lag"));
        if (shaderBorderEnd) renderPostEffect(getIdentifier("border_end"));
        if (shaderRedMaj) renderPostEffect(getIdentifier("redmaj"));

        if (shaderAnaglyph && shaderAnaglyphUniformX != 0 && shaderAnaglyphUniformY != 0) {
            try (GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(anaglyph_ubo.currentBuffer(), false, true)) {
                view.data().position(0);
                Std140Builder.intoBuffer(view.data()).putVec2(shaderAnaglyphUniformX, shaderAnaglyphUniformY);
            }
            CustomRenderSystem.setShaderAnaglyph(anaglyph_ubo.currentBuffer());
            renderPostEffect(getIdentifier("anaglyph"));
        }
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
