package com.bleudev.nlf2.mixin.nine_lifes;

import com.bleudev.nine_lifes.NineLifes;
import com.bleudev.nlf2.util.InjectsKt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NineLifes.class)
public class NineLifesMixin {
    @WrapOperation(method = "onInitialize$lambda$4", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameType;isSurvival()Z"))
    private static boolean doNotTakeActorLifes(GameType instance, Operation<Boolean> original, @Local(name = "entity", argsOnly = true) LivingEntity entity) {
        return !((entity instanceof ServerPlayer player) && InjectsKt.isActor(player)) && original.call(instance);
    }
}
