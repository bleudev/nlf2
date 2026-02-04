package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.waypoints.ClientWaypointManager;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.bleudev.nlf2.client.util.InjectsKt.holdsInAnyHand;

@Environment(EnvType.CLIENT)
@Mixin(ClientWaypointManager.class)
public class ClientWaypointManagerMixin {
    @ModifyReturnValue(method = "hasWaypoints", at = @At("RETURN"))
    private boolean disableLocatorBar(boolean original) {
        var player = Minecraft.getInstance().player;
        if (player == null) return original;
        // Show locator bar only if player holds compass
        return original && holdsInAnyHand(player, Items.COMPASS);
    }
}
