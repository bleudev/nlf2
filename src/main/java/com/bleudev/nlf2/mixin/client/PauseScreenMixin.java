package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.gui.components.AdvancedDisconnectButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.bleudev.nlf2.client.ClientStorageKt.getDisconnectState;
import static com.bleudev.nlf2.client.ClientStorageKt.webcamEnabled;

@Environment(EnvType.CLIENT)
@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @SuppressWarnings("unchecked")
    @ModifyArg(method = "createPauseMenu()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;I)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 0))
    private <T extends LayoutElement> T modifyDisconnectButton(T original) {
        if (original instanceof Button button && getDisconnectState() == 0) {
            return (T) new AdvancedDisconnectButton(button);
        }
        return original;
    }

    @Override
    public void onClose() {
        webcamEnabled = false;
        super.onClose();
    }
}
