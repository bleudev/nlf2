package com.bleudev.nlf2.custom.world.item

import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class ManualItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        player.openItemGui(itemStack, hand)
        player.awardStat(Stats.ITEM_USED.get(this))
        return InteractionResult.SUCCESS
    }
}