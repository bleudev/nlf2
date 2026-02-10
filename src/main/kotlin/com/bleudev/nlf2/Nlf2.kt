package com.bleudev.nlf2

import com.bleudev.nlf2.custom.Nlf2Commands
import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData
import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.util.sendToEveryone
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.entity.player.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val MODID = "nlf2"
fun getIdentifier(path: String) = Identifier.fromNamespaceAndPath(MODID, path)
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    override fun onInitialize() {
        Nlf2Packets.initialize()
        Nlf2Commands.initialize()

        ServerPlayerEvents.JOIN.register { player ->
            Nlf2PlayersData.get(player.level().server).trySetDefault(player.gameProfile.name)

            if (player.isFirstJoined()) player.teleportOnFirstJoin()
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.sendToEveryone { player -> arrayOf(
                WorldBorderNoisePayload(player.level().worldBorder.getDistanceToBorder(player))
            ) }
        }
    }

    private fun ServerPlayer.isFirstJoined(): Boolean = stats.getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) <= 10

    private fun ServerPlayer.teleportOnFirstJoin() {
        LOGGER.info("Try to teleport on first join")

        val level = level()
        val border = level.worldBorder
        val borderIndent = 20
        val minX = (border.minX + borderIndent).toInt()
        val maxX = (border.maxX - borderIndent).toInt()
        val minZ = (border.minZ + borderIndent).toInt()
        val maxZ = (border.maxZ - borderIndent).toInt()

        val x = level.random.nextInt(minX, maxX)
        val z = level.random.nextInt(minZ, maxZ)
        val pos = findSolidGround(level, x, z)
        if (pos == null) {
            LOGGER.error("Failed to find solid ground at [{}, {}]", x, z)
            LOGGER.info("Retrying find solid ground")
            teleportOnFirstJoin()
        } else {
            // Check that there aren't players nearby
            val playerRange = 30.0
            val x = pos.x.toDouble(); val y = pos.y.toDouble(); val z = pos.z.toDouble()
            if (level.getNearestPlayer(x, y, z, playerRange) { entity ->
                if (entity is Player) return@getNearestPlayer entity.gameProfile.name != this.gameProfile.name
                return@getNearestPlayer false
            } != null) return teleportOnFirstJoin()
            teleportTo(x, y, z)
        }
    }

    private fun findSolidGround(level: ServerLevel, x: Int, z: Int): BlockPos? {
        for (y in level.maxY - 1 downTo 1) {
            val pos = BlockPos(x, y, z)
            if (level.getBlockState(pos).isSolidRender && level.getFluidState(pos.above(1)).isEmpty)
                return pos.above(1)
        }
        return null
    }
}
