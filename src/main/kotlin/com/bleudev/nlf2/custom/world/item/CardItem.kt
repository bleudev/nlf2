package com.bleudev.nlf2.custom.world.item

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class CardItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        player.sendOverlayMessage(Component.translatable("nlf2.message.item.card.check"))
        return InteractionResult.PASS
    }
}