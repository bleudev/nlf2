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

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanMoveMouseAnyway;
import static com.bleudev.nlf2.client.util.ClientCallbacksKt.getCanUseMouse;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    private void cancelMove(long handle, double xpos, double ypos, CallbackInfo ci) {
        if (!(getCanUseMouse() || getCanMoveMouseAnyway())) ci.cancel();
    }
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void cancelScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        if (!getCanUseMouse()) ci.cancel();
    }
    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void cancelButton(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo ci) {
        if (!getCanUseMouse()) ci.cancel();
    }
    @Inject(method = "onDrop", at = @At("HEAD"), cancellable = true)
    private void cancelDrop(long handle, List<Path> files, int failedCount, CallbackInfo ci) {
        if (!getCanUseMouse()) ci.cancel();
    }
}
