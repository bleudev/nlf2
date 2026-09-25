package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.i.mixin.client.IHumanoidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<S extends HumanoidRenderState> {
    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void cancelRenderArmor(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, final S state, CallbackInfo ci) {
        if (!((IHumanoidRenderState) state).nlf2$shouldRenderArmor()) ci.cancel();
    }
}
