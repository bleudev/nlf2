package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanOpenChat;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.isHoldingCompass;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            webcamEnabled = false;
            pause_screen_red_screen_effect = false;
            should_not_play_any_sound = false;
        } else if (screen.getTitle().equals(Component.translatable("gui.xaero_world_map_screen"))) { // Block Xaero's world map
            if (isHoldingCompass()) return;
            ci.cancel();
        } else if (screen instanceof ChatScreen) { // Block chat opening
            if (getCanOpenChat()) return;
            ci.cancel();
        }
    }
}
