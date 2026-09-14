package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.contextualbar.LocatorBar;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.modifyWaypointColor;

@Environment(EnvType.CLIENT)
@Mixin(LocatorBar.class)
public class LocatorBarMixin {
    @WrapMethod(method = "lambda$extractRenderState$4")
    private static Integer modifyColorWithUUID(UUID uuid, Operation<Integer> original) {
        int col = original.call(uuid);
        return modifyWaypointColor(Either.left(uuid), col);
    }
    @WrapMethod(method = "lambda$extractRenderState$5")
    private static Integer modifyColorWithName(String name, Operation<Integer> original) {
        int col = original.call(name);
        return modifyWaypointColor(Either.right(name), col);
    }
}
