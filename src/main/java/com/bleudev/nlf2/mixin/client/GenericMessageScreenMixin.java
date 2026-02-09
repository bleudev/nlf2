package com.bleudev.nlf2.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.util.InjectsKt.black;

@Environment(EnvType.CLIENT)
@Mixin(GenericMessageScreen.class)
public class GenericMessageScreenMixin extends Screen {
    protected GenericMessageScreenMixin(Component title) {
        super(title);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        var saving = Component.translatable("menu.savingLevel");
        if (title.equals(saving)) black(graphics);
    }
}
