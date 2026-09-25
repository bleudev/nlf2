package com.bleudev.nlf2.custom.world.item

import com.bleudev.nlf2.CARD_NEW_DIALOGUE
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class CardItem(properties: Properties) : Item(properties) {
    private var newDialogueTicks = -1

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (CARD_NEW_DIALOGUE) {
            newDialogueTicks = 0
            player.sendOverlayMessage(Component.translatable("nlf2.message.item.card.check.new.0"))
        } else {
            player.sendOverlayMessage(Component.translatable("nlf2.message.item.card.check"))
        }

        return InteractionResult.PASS
    }

    override fun inventoryTick(itemStack: ItemStack, level: ServerLevel, owner: Entity, slot: EquipmentSlot?) {
        if (newDialogueTicks >= 0 && owner is Player) {
            newDialogueTicks++

            val isBleu = owner.gameProfile.name == "bleugame"

            var message: Component? = null

            when (newDialogueTicks) {
                40 -> message = Component.translatable("nlf2.message.item.card.check.new.1")
                100 -> message = Component.translatable("nlf2.message.item.card.check.new.2")
                160 -> message =
                    if (isBleu) Component.translatable("nlf2.message.item.card.check.new.3.bleu")
                    else Component.translatable("nlf2.message.item.card.check.new.3")
                220 -> {
                    message =
                        if (isBleu) Component.translatable("nlf2.message.item.card.check.new.4.bleu")
                        else Component.translatable("nlf2.message.item.card.check.new.4")
                    newDialogueTicks = -1
                }
            }
            message?.let { owner.sendOverlayMessage(it) }
        }
    }
}