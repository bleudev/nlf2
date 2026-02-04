package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {
    protected DisconnectedScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        Minecraft.getInstance().close();
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), 0xff000000);
    }
}
