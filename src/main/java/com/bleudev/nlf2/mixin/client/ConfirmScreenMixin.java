package com.bleudev.nlf2.mixin.client;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.Nlf2ConstKt.INTRO_ENABLED;
import static com.bleudev.nlf2.client.util.ClientInjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(ConfirmScreen.class)
public class ConfirmScreenMixin extends Screen {
    @Shadow
    @Final
    protected BooleanConsumer callback;

    protected ConfirmScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (INTRO_ENABLED) {
            black(graphics);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void autoConfirmModSyncUpdate(CallbackInfo ci) {
        if (title.equals(Component.translatable("modsync.update.confirm.title")) || title.equals(Component.translatable("modsync.update.end.restart"))) {
            ci.cancel();
            callback.accept(true);
        }
    }
}
