package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.renderPlayerInTab;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @ModifyReturnValue(method = "getPlayerInfos", at = @At("RETURN"))
    private List<PlayerInfo> removeActorFromTabOverlay(List<PlayerInfo> original) {
        final ArrayList<PlayerInfo> nw = new ArrayList<>(original);
        nw.removeIf(playerInfo -> !renderPlayerInTab(playerInfo.getProfile().name()));
        return nw.stream().toList();
    }
}
