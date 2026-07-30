package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanUseKeyboard;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void cancelKeyPress(final long handle, final @KeyEvent.Action int action, final KeyEvent event, CallbackInfo ci) {
        if (!getCanUseKeyboard()) ci.cancel();
    }
}
