package com.bleudev.nlf2.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(KeyBindsList.KeyEntry.class)
public interface KeyBindsList$KeyEntryAccessor {
    @Accessor("key")
    KeyMapping nlf2$key();
}
