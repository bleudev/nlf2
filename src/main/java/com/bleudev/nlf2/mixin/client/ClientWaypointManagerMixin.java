package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.waypoints.ClientWaypointManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.isHoldingCompass;

@Environment(EnvType.CLIENT)
@Mixin(ClientWaypointManager.class)
public class ClientWaypointManagerMixin {
    @ModifyReturnValue(method = "hasWaypoints", at = @At("RETURN"))
    private boolean disableLocatorBar(boolean original) {
        return original && isHoldingCompass();
    }
}
