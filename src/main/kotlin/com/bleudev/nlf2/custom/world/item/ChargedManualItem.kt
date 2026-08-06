package com.bleudev.nlf2.custom.world.item

import com.bleudev.nlf2.custom.packet.payload.unit.StoryStart
import com.bleudev.nlf2.util.sendToEveryone
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class ChargedManualItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        player.awardStat(Stats.ITEM_USED.get(this))
        if (!level.isClientSide) {
            (level as? ServerLevel)?.server?.sendToEveryone {
                arrayOf(StoryStart.INSTANCE)
            }
            return InteractionResult.SUCCESS
        }
        return super.use(level, player, hand)
    }
}