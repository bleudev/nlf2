package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.ClientStorageKt.getCloudAlphaDelta;

@Environment(EnvType.CLIENT)
@Mixin(CloudRenderer.class)
public class CloudRendererMixin {
    @WrapMethod(method = "render")
    private void modifyColor(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks, Operation<Void> original) {
        var newColor = ARGB.color((int) (ARGB.alpha(color) * getCloudAlphaDelta()), ARGB.red(color), ARGB.green(color), ARGB.blue(color));
        original.call(newColor, cloudStatus, bottomY, range, cameraPosition, gameTime, partialTicks);
    }
}
