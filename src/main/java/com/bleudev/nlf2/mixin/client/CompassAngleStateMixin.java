package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.ClientStorageKt.compassForceSpin;

@Environment(EnvType.CLIENT)
@Mixin(CompassAngleState.class)
public abstract class CompassAngleStateMixin {
    @Shadow
    protected abstract float getRandomlySpinningRotation(final int seed, final long gameTime);

    @ModifyReturnValue(method = "calculate", at = @At("RETURN"))
    private float modifySpin(float original, ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner) {
        if (compassForceSpin) return getRandomlySpinningRotation(seed, level.getGameTime());
        return original;
    }
}
