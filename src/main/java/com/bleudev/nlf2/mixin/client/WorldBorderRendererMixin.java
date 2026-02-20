package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.WorldBorderRenderer;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(WorldBorderRenderer.class)
public class WorldBorderRendererMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
    private Vector4fc modifyWorldBorderColor(Vector4fc colorModulator) {
        return new Vector4f(1f, 0f, 0f, colorModulator.w());
    }
}
