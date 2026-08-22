package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.util.ClientInjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(ConfirmScreen.class)
public class ConfirmScreenMixin extends Screen {
    protected ConfirmScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        black(graphics);
    }
}
