package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.gui.components.AdvancedDisconnectButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanUseKeyboard;

@Environment(EnvType.CLIENT)
@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {
    @Shadow
    public abstract void onClose();

    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void doNotSoftLock(CallbackInfo ci) {
        if (!getCanUseKeyboard()) {
            this.onClose();
        }
    }

    @SuppressWarnings("unchecked")
    @ModifyArg(method = "createPauseMenu()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;I)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 1))
    private <T extends LayoutElement> T modifyDisconnectButton(T original) {
        if (original instanceof Button button && getDisconnectState() == 0) {
            return (T) new AdvancedDisconnectButton(button);
        }
        return original;
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    public void onCloseMixin(CallbackInfo ci) {
        webcamEnabled = false;
        Minecraft.getInstance().updateTitle();
        pause_screen_red_screen_effect = false;
        should_not_play_any_sound = false;
    }
}
