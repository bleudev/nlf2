package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

import static com.bleudev.nlf2.Nlf2ConstKt.UNKNOWN_GAME_PROFILE;
import static com.bleudev.nlf2.Nlf2Kt.getIdentifier;
import static com.bleudev.nlf2.client.ClientStorageKt.getUnknownRainbowEffectColor;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.modifyPlayerTabEntries;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @ModifyReturnValue(method = "getPlayerInfos", at = @At("RETURN"))
    private List<PlayerInfo> removeActorFromTabOverlay(List<PlayerInfo> original) {
        return modifyPlayerTabEntries(original);
    }
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;onlineMode()Z"))
    private boolean alwaysShowHead(ClientPacketListener instance, Operation<Boolean> original) {
        return true;
    }
    @ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceExtractor;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;IIIZZI)V"), index = 1)
    private Identifier modifyPlayerFace(Identifier texture, @Local(name = "info") PlayerInfo info) {
        return info.getProfile().equals(UNKNOWN_GAME_PROFILE) ? getIdentifier("textures/entity/player/black.png") : texture;
    }
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void modifyPlayerTabColor(GuiGraphicsExtractor instance, Font font, Component str, int x, int y, int color, Operation<Void> original) {
        if (str.getString().equals("???")) {
            color = getUnknownRainbowEffectColor();
        }
        original.call(instance, font, str, x, y, color);
    }
}
