package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

import static com.bleudev.nlf2.client.ClientStorageKt.canUseMouse;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    private void cancelMove(long l, double d, double e, CallbackInfo ci) {
        if (!canUseMouse) ci.cancel();
    }
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void cancelScroll(long l, double d, double e, CallbackInfo ci) {
        if (!canUseMouse) ci.cancel();
    }
    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void cancelButton(long l, MouseButtonInfo mouseButtonInfo, int i, CallbackInfo ci) {
        if (!canUseMouse) ci.cancel();
    }
    @Inject(method = "onDrop", at = @At("HEAD"), cancellable = true)
    private void cancelDrop(long l, List<Path> list, int i, CallbackInfo ci) {
        if (!canUseMouse) ci.cancel();
    }
}
