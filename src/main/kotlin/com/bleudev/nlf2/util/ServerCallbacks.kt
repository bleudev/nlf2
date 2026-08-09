package com.bleudev.nlf2.util

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.data.saved.nlf2data
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity

fun areMobsAvoid(entity: LivingEntity): Boolean =
    !(entity !is ServerPlayer || entity.isActor) && entity.level().server.nlf2data.loreData.mobAvoidPlayers

fun clearVertItemsIfNeed(server: MinecraftServer) {
    if (server.nlf2data.loreData.noVert) {
        for (player in server.playerList.players) {
            player.advancedClear(
                Nlf2Items.MANUAL,
                Nlf2Items.WD_PUZZLE,
                Nlf2Items.CARD,
                Nlf2Items.CHARGED_MANUAL,
                Nlf2Blocks.STRANGE_JUKEBOX,
                Nlf2Blocks.AMETHYST_OXEYE_DAISY
            )
        }
    }
}
