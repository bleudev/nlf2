package com.bleudev.nlf2.mixin.client.impl;

import com.bleudev.nlf2.i.mixin.client.IKeyBindsList$CategoryEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyBindsList.CategoryEntry.class)
public class KeyBindsList$CategoryEntryMixin implements IKeyBindsList$CategoryEntry {
    @Mutable
    @Unique
    @Final
    private KeyMapping.Category category;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void extractCategory(KeyBindsList this$0, KeyMapping.Category category, CallbackInfo ci) {
        this.category = category;
    }

    @Override
    public KeyMapping.Category nlf2$getCategory() {
        return category;
    }
}
