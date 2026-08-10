package com.bleudev.nlf2.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nlf2.util.ServerCallbacksKt.modifyContainerItemStackIfNeed;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @WrapMethod(method = "addSlot")
    private Slot clearVertItems(Slot slot, Operation<Slot> original) {
        Slot var10001 = original.call(slot);
        ItemStack var10002 = var10001.getItem();
        ItemStack var10003 = modifyContainerItemStackIfNeed(var10002);
        try {
            var10001.set(var10003);
        } catch (NullPointerException ignored) {}
        return var10001;
    }
}
