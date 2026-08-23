package com.bleudev.nlf2.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.client.util.ClientCallbacksKt.modifyMessage;

@Environment(EnvType.CLIENT)
@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @WrapMethod(method = "addMessageToDisplayQueue")
    private void modifyMessages(GuiMessage message, Operation<Void> original) {
        // Unknown
        boolean bl = false;
        String string = message.content().getString();
        if (string.startsWith("<???>") || string.startsWith("???")) {
            bl = true;
        }

        GuiMessage newMessage = new GuiMessage(
            message.addedTime(),
            modifyMessage(message.content()),
            message.signature(),
            message.source(),
            bl ? null : message.tag()
        );
        original.call(newMessage);
    }
}
