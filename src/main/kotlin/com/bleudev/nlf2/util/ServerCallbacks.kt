package com.bleudev.nlf2.util

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.data.saved.nlf2data
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

var clearVertItemsCludge: Boolean = false

fun areMobsAvoid(entity: LivingEntity): Boolean =
    !(entity !is ServerPlayer || entity.isActor) && entity.level().server.nlf2data.loreData.mobAvoidPlayers

val VERT_ITEMS = arrayOf<ItemLike>(
    Nlf2Items.MANUAL,
    Nlf2Items.WD_PUZZLE,
    Nlf2Items.CARD,
    Nlf2Items.CHARGED_MANUAL,
    Nlf2Blocks.STRANGE_JUKEBOX,
    Nlf2Blocks.AMETHYST_OXEYE_DAISY
)

fun modifyContainerItemStackIfNeed(itemStack: ItemStack): ItemStack {
    if (clearVertItemsCludge) {
        if (VERT_ITEMS.any { itemStack.`is`(it.asItem()) }) {
            return ItemStack.EMPTY
        }
    }
    return itemStack
}

fun clearVertItemsIfNeed(server: MinecraftServer) {
    if (server.nlf2data.loreData.noVert) {
        for (player in server.playerList.players) {
            player.advancedClear(*VERT_ITEMS)
        }
    }
}
