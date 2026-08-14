package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nlf2.client.ClientStorageKt.deathScreenBlackFogEffect;
import static com.bleudev.nlf2.client.util.ClientInjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> @NonNull T addRenderableWidget(@NonNull T widget) {
        if (widget instanceof Button button) {
            if (button.getMessage().equals(Component.translatable("deathScreen.titleScreen"))) return widget;
        }
        return super.addRenderableWidget(widget);
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void blackBackgroundIfJoinBlackness(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (deathScreenBlackFogEffect) black(graphics);
    }
}
