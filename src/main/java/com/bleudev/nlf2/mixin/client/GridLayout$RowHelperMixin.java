package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.bleudev.nlf2.client.ClientStorageKt.getDisconnectState;
import static net.minecraft.network.chat.CommonComponents.GUI_DISCONNECT;
import static net.minecraft.network.chat.CommonComponents.GUI_RETURN_TO_MENU;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.layouts.GridLayout$RowHelper")
public class GridLayout$RowHelperMixin {
    @Inject(method = "addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;ILnet/minecraft/client/gui/layouts/LayoutSettings;)Lnet/minecraft/client/gui/layouts/LayoutElement;", at = @At("HEAD"), cancellable = true)
    private <T extends LayoutElement> void cancelDisconnectButtonInitialize(T widget, int columnWidth, LayoutSettings layoutSettings, CallbackInfoReturnable<T> cir) {
        if (widget instanceof Button) {
            var label = ((Button) widget).getMessage();
            if (List.of(GUI_RETURN_TO_MENU, GUI_DISCONNECT).contains(label) && getDisconnectState() == 1)
                cir.setReturnValue(widget);
        }
    }
}
