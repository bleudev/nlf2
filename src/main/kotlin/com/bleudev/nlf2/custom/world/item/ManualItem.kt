package com.bleudev.nlf2.custom.world.item

import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import com.bleudev.nlf2.custom.packet.payload.OpenManual
import com.bleudev.nlf2.util.sendPackets
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class ManualItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        player.awardStat(Stats.ITEM_USED.get(this))
        val manualId = 0
        if (player is ServerPlayer) {
            player.sendPackets(OpenManual(manualId))
            return InteractionResult.SUCCESS
        } else if (player is LocalPlayer) {
            Minecraft.getInstance().setScreen(ManualScreen(manualId))
            return InteractionResult.SUCCESS
        }
        return super.use(level, player, hand)
    }
}