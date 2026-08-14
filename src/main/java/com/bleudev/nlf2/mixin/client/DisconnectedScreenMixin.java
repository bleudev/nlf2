package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.util.ClientInjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {
    protected DisconnectedScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        black(graphics);
        Minecraft.getInstance().close();
    }
}
