package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.item.CardItem
import com.bleudev.nlf2.custom.world.item.ChargedManualItem
import com.bleudev.nlf2.custom.world.item.ManualItem
import com.bleudev.nlf2.custom.world.item.WDPuzzleItem
import com.bleudev.nlf2.util.lightningAndFireResistant
import com.bleudev.nlf2.util.registerCreativeModeTab
import com.bleudev.nlf2.util.registerItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

object Nlf2Items {
    object Groups {
        val GENERAL = registerCreativeModeTab("general") {
            icon { ItemStack(WD_PUZZLE) }
            .title(Component.translatable("creativeModeTab.nlf2.general"))
            .displayItems { _, output ->
                val var1 = listOf<ItemLike>(
                    WD_PUZZLE,
                    MANUAL,
                    CARD,
                    CHARGED_MANUAL,
                    Nlf2Blocks.STRANGE_JUKEBOX,
                    Nlf2Blocks.AMETHYST_OXEYE_DAISY,
                    Nlf2Blocks.ARENA_BLOCK,
                    Nlf2Blocks.COMPUTER,
                )

                for (var2 in var1) {
                    output.accept(var2, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
                }
            }
        }

        fun initialize() {}
    }

    val WD_PUZZLE = registerItem("wd_puzzle", ::WDPuzzleItem)
    val MANUAL = registerItem("manual", ::ManualItem, Item.Properties().lightningAndFireResistant())
    val CARD = registerItem("card", ::CardItem)
    val CHARGED_MANUAL = registerItem("charged_manual", ::ChargedManualItem, Item.Properties().lightningAndFireResistant())

    fun initialize() {
        Groups.initialize()
    }
}