package com.bleudev.nlf2.custom.world.item

import com.bleudev.nlf2.client.custom.gui.screens.PuzzleScreen
import com.bleudev.nlf2.custom.packet.payload.OpenPuzzle
import com.bleudev.nlf2.custom.packet.payload.PacketPuzzle
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

class WDPuzzleItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        player.awardStat(Stats.ITEM_USED.get(this))
        if (player is ServerPlayer) {
            player.sendPackets(OpenPuzzle(PacketPuzzle.WD))
            return InteractionResult.SUCCESS
        } else if (player is LocalPlayer) {
            Minecraft.getInstance().gui.setScreen(PuzzleScreen(PacketPuzzle.WD))
            return InteractionResult.SUCCESS
        }
        return super.use(level, player, hand)
    }
}