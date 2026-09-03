package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.GpuBackend;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nlf2.client.ClientStorageKt.*;
import static com.bleudev.nlf2.client.util.TimeTickerKt.TICKER_RIP_SANYA;

@Environment(EnvType.CLIENT)
@Mixin(Window.class)
public abstract class WindowMixin {
    @Shadow
    private boolean fullscreen;

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/DisplayData;isFullscreen()Z"))
    private boolean setInitFullscreen(boolean original) {
        if (getWindowBlockFullscreen()) {
            return false;
        } else {
            return original;
        }
    }
    @Inject(method = "createGlfwWindow", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuBackend;setWindowHints()V", shift = At.Shift.AFTER))
    private static void setAdditionalWindowHits(int width, int height, String title, long monitor, GpuBackend backend, CallbackInfoReturnable<Long> cir) {
        GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, GLFW.GLFW_FALSE);
    }
    @WrapMethod(method = "setTitle")
    private void modifyTitle(String title, Operation<Void> original) {
        if (getWindowBlockedClose()) {
            original.call("YOU CANNOT");
            return;
        }

        String var10001 = title.replace("Minecraft*", "Minecraft");
        var10001 = var10001.replace("Minecraft", "\"Minecraft\"");
        if (getRipSanyaBlockKeys()) {
            int var10002 = ((int) TICKER_RIP_SANYA.getTicks()) % 80;
            if (var10002 < 40) {
                var10001 = "ATTENTION";
            }
            else {
                var10001 = "ERASING IS IN PROCESS";
            }
        }
        if (webcamEnabled) {
            var10001 = "EYES ARE WATCHING";
        }
        original.call(var10001);
    }
    @Inject(method = "toggleFullScreen", at = @At("HEAD"), cancellable = true)
    private void cancelFullscreen(CallbackInfo ci) {
        if (getWindowBlockFullscreen()) {
            this.fullscreen = false;
            ci.cancel();
        }
    }
}
