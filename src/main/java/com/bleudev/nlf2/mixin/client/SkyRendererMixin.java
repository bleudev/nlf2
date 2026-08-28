package com.bleudev.nlf2.mixin.client;

import com.bleudev.nine_lifes.util.InjectsKt;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import kotlin.random.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.getSunGlitchEffectEnabled;
import static com.bleudev.nlf2.client.ClientStorageKt.getSunShakeStrength;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public class SkyRendererMixin {
    @Shadow
    private static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite sprite) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private TextureAtlas celestialsAtlas;

    @Mutable
    @Unique
    @Final
    private GpuBuffer glitchSunBuffer;

    @Unique
    @Final
    private static final Identifier GLITCH_SUN_SPRITE = getIdentifier("glitch_sun");

    @Unique
    private static GpuBuffer buildSunQuad(final TextureAtlas atlas) {
        return buildCelestialQuad("Glitch sun quad", atlas.getSprite(GLITCH_SUN_SPRITE));
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initGlitchSunBuffer(TextureManager textureManager, AtlasManager atlasManager, RenderTarget renderTarget, CallbackInfo ci) {
        this.glitchSunBuffer = buildSunQuad(this.celestialsAtlas);
    }

    @Inject(method = "renderSun", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;scale(FFF)Lorg/joml/Matrix4f;", shift = At.Shift.AFTER))
    private void modifySunPosition(float rainBrightness, PoseStack poseStack, CallbackInfo ci, @Local(name = "modelViewStack") Matrix4fStack modelViewStack) {
        float shakeStrength = getSunShakeStrength() * .025f;

        if (shakeStrength > 0f) {
            float dx = InjectsKt.lerp(Random.Default.nextFloat(), -shakeStrength, shakeStrength);
            float dy = InjectsKt.lerp(Random.Default.nextFloat(), -shakeStrength, shakeStrength);
            modelViewStack.translate(dx, 0, dy);
        }
    }
    @ModifyArg(method = "renderSun", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"), index = 1)
    private @Nullable GpuBufferSlice modifySunBuffer(@Nullable GpuBufferSlice vertexBuffer) {
        return getSunGlitchEffectEnabled() ? this.glitchSunBuffer.slice() : vertexBuffer;
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void glitchSunClose(CallbackInfo ci) {
        this.glitchSunBuffer.close();
    }
}
