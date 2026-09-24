package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen;
import com.bleudev.nlf2.client.custom.gui.screens.PuzzleScreen;
import com.bleudev.nlf2.custom.Nlf2Items;
import com.bleudev.nlf2.custom.packet.payload.PacketPuzzle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.dataSyncedManualId;
import static com.bleudev.nlf2.client.ClientStorageKt.deathScreenBlackFogEffect;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "respawn", at = @At("RETURN"))
    private void resetDeathScreenBlackFogEffect(CallbackInfo ci) {
        deathScreenBlackFogEffect = false;
    }

    @Inject(method = "openItemGui", at = @At("TAIL"))
    private void openCustomGuis(ItemStack itemStack, InteractionHand hand, CallbackInfo ci) {
        if (itemStack.is(Nlf2Items.WD_PUZZLE)) {
            this.minecraft.gui.setScreen(new PuzzleScreen(PacketPuzzle.WD));
        }
        else if (itemStack.is(Nlf2Items.MANUAL)) {
            this.minecraft.gui.setScreen(new ManualScreen(dataSyncedManualId, true));
        }
    }
}
