package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static com.bleudev.nlf2.client.ClientStorageKt.dataSyncedActor;
import static com.bleudev.nlf2.client.ClientStorageKt.dataSyncedMeetState;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void cancelExtraction(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective, CallbackInfo ci) {
        if (dataSyncedMeetState % 2 == 0) ci.cancel();
    }

    @ModifyReturnValue(method = "getPlayerInfos", at = @At("RETURN"))
    private List<PlayerInfo> removeActorFromTabOverlay(List<PlayerInfo> original) {
        final ArrayList<PlayerInfo> nw = new ArrayList<>(original);
        nw.removeIf(playerInfo -> playerInfo.getProfile().name().equalsIgnoreCase(dataSyncedActor));
        return nw.stream().toList();
    }
}
