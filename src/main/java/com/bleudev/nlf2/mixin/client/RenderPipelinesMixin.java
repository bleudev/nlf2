package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.custom.Nlf2RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import kotlin.Unit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {
    @Shadow
    @Final
    private static Map<Identifier, RenderPipeline> PIPELINES_BY_LOCATION;

    @Inject(method = "register", at = @At("HEAD"))
    private static void registerCustomPipelines(RenderPipeline pipeline, CallbackInfoReturnable<RenderPipeline> cir) {
        Nlf2RenderPipelines.INSTANCE.initialize(custom -> {
            PIPELINES_BY_LOCATION.put(custom.getLocation(), custom);
            return Unit.INSTANCE;
        });
    }
}
