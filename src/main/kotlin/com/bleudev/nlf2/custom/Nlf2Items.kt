package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.item.WDPuzzleItem
import com.bleudev.nlf2.util.registerCreativeModeTab
import com.bleudev.nlf2.util.registerItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object Nlf2Items {
    object Groups {
        val GENERAL = registerCreativeModeTab("general") {
            icon { ItemStack(WD_PUZZLE) }
            .title(Component.translatable("creativeModeTab.nlf2.general"))
            .displayItems { _, output ->
                output.accept(WD_PUZZLE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY)
            }
        }

        fun initialize() {}
    }

    val WD_PUZZLE = registerItem("wd_puzzle", ::WDPuzzleItem)

    fun initialize() {
        Groups.initialize()
    }
}