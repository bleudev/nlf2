package com.bleudev.nlf2.mixin.client;

import com.bleudev.nlf2.client.custom.key.mapping.Nlf2KeyMapping;
import com.bleudev.nlf2.interfaces.mixin.client.IKeyBindsList$CategoryEntry;
import com.bleudev.nlf2.mixin.client.accessor.KeyBindsList$KeyEntryAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(KeyBindsList.class)
public class KeyBindsListMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/options/controls/KeyBindsList;addEntry(Lnet/minecraft/client/gui/components/AbstractSelectionList$Entry;)I"))
    private int modifyInit(KeyBindsList instance, AbstractSelectionList.Entry<?> entry, Operation<Integer> original) {
        boolean render = true;
        if (entry instanceof KeyBindsList.KeyEntry) {
            render = Nlf2KeyMapping.shouldRender$nlf2(((KeyBindsList$KeyEntryAccessor) entry).nlf2$key());
        }
        if (entry instanceof KeyBindsList.CategoryEntry) {
            render = Nlf2KeyMapping.shouldRenderCategory$nlf2(((IKeyBindsList$CategoryEntry) entry).nlf2$getCategory());
        }
        if (render) return original.call(instance, entry);
        else return 0;
    }
}
