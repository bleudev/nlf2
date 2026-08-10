package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.sys.anim.StoryAnimation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanOpenChat;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.isHoldingCompass;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    public abstract @Nullable Screen screen();

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickXaeroMap(CallbackInfo ci) {
        if (!isHoldingCompass()) {
            Screen screen = screen();
            if (screen != null && screen.getTitle().equals(Component.translatable("gui.xaero_world_map_screen"))) setScreen(null);
        }
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void blockSpecificScreens(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            webcamEnabled = false;
            Minecraft.getInstance().updateTitle();
            pause_screen_red_screen_effect = false;
            should_not_play_any_sound = false;
        } else if (screen.getTitle().equals(Component.translatable("gui.xaero_world_map_screen"))) { // Block Xaero's world map
            if (isHoldingCompass()) return;
            ci.cancel();
        } else if (screen instanceof ChatScreen && !(screen instanceof InBedChatScreen)) { // Block chat opening
            if (getCanOpenChat()) return;
            ci.cancel();
        }
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractExtras(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci, @Local(name = "graphics") GuiGraphicsExtractor graphics) {
        StoryAnimation.INSTANCE.extractRenderState(graphics);
    }
}
