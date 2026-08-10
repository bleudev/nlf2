package com.bleudev.nlf2.client.custom.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import static com.bleudev.nlf2.client.ClientStorageKt.*;

public class AdvancedDisconnectButton extends Button.Plain {
    public AdvancedDisconnectButton(Button original) {
        super(original.getX(), original.getY(), original.getWidth(), original.getHeight(), original.getMessage(), (ignored) -> {}, Button.DEFAULT_NARRATION);
    }

    @Override
    protected void handleCursor(@NonNull GuiGraphicsExtractor graphics) {
        super.handleCursor(graphics);
        if (isHovered()) {
            this.setMessage(Component.literal("I'M WATCHING FOR YOU"));
            this.active = false;
            webcamEnabled = true;
            Minecraft.getInstance().updateTitle();
            pause_screen_ticks = 0;
            pause_screen_red_screen_effect = true;
            should_not_play_any_sound = true;
            Minecraft.getInstance().getSoundManager().stop();
            setDisconnectState(1);
        }
    }
}
