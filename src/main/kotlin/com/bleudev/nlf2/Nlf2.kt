package com.bleudev.nlf2

import com.bleudev.nine_lifes.custom.NineLifesItems
import com.bleudev.nine_lifes.util.lifes
import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.custom.Nlf2Commands
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.JoinDisconnectVersion
import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.CauseEventWithKey
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.BorderBlackFogStart
import com.bleudev.nlf2.custom.packet.payload.unit.CancelBorderBlackFog
import com.bleudev.nlf2.util.hurtUnknown
import com.bleudev.nlf2.util.sendToEveryone
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val MODID = "nlf2"
fun getIdentifier(path: String): Identifier = Identifier.fromNamespaceAndPath(MODID, path)
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    private var ticks = 0
    private lateinit var serverVersion: String

    override fun onInitialize() {
        Nlf2Commands.initialize()
        Nlf2Items.initialize()
        Nlf2Packets.initialize()
        serverVersion = FabricLoader.getInstance().getModContainer(MODID).get().metadata.version.friendlyString

        ServerPlayerEvents.JOIN.register { player ->
            player.level().server.nlf2data.onJoin(player)

            if (player.isFirstJoined() && TELEPORT_ON_FIRST_JOIN) player.teleportOnFirstJoin()
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.sendToEveryone { player -> arrayOf(
                WorldBorderNoisePayload(player.level().worldBorder.getDistanceToBorder(player))
            ) }
            ticks++
            if (ticks % 20 == 0) {
                server.playerList.players.forEach { player ->
                    if (server.nlf2data.loreData.borderState != 2) return@forEach
                    if (!player.gameMode().isSurvival) return@forEach
                    if (!player.isAlive) return@forEach
                    val level = player.level()
                    val dist = level.worldBorder.getDistanceToBorder(player)
                    if (dist <= 25) {
                        player.sendPacket(BorderBlackFogStart.INSTANCE)
                    } else player.sendPacket(CancelBorderBlackFog.INSTANCE)
                }
            }
            // Manuals
            fun shouldUpdate(item: Item): Boolean =
                server.playerList.players.any { player -> player.inventory.any { (!it.isEmpty) && it.`is`(item) } }
            val owner = server.playerList.players.firstOrNull { player -> player.inventory.any { it.`is`(Nlf2Items.MANUAL) } }

            if (shouldUpdate(Items.DEBUG_STICK) && server.nlf2data.loreData.manualId == 0)
                server.causeEvent(93, owner)
            if (shouldUpdate(NineLifesItems.AMETHYST_STICK) && server.nlf2data.loreData.manualId == 1)
                server.causeEvent(94, owner)
        }

        registerPacketReceiver(JoinSendVersion) { payload, ctx ->
            val clientVersion = payload.version
            val player = ctx.player()
            if (serverVersion != clientVersion) {
                player.setGameMode(GameType.SPECTATOR)
                player.sendPacket(JoinDisconnectVersion(clientVersion, serverVersion))
            } else {
                if (player.gameMode() == GameType.SPECTATOR && player.lifes >= 1) {
                    player.setGameMode(GameType.SURVIVAL)
                }
            }
        }

        registerPacketReceiver(KillBorderBlackFog) { _, ctx ->
            if (ctx.server().nlf2data.loreData.borderState != 2) return@registerPacketReceiver
            ctx.player().hurtUnknown(9999f)
        }

        registerPacketReceiver(CauseEventWithKey) { p, ctx ->
            ctx.server().causeEvent<Any>(p.eventId)
        }
    }

    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T, ctx: ServerPlayNetworking.Context) -> Unit) =
        ServerPlayNetworking.registerGlobalReceiver(packet.type, handler)
    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T) -> Unit) =
        registerPacketReceiver(packet) {payload, _ -> handler(payload)}

    private fun ServerPlayer.isFirstJoined(): Boolean = stats.getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) <= 10

    private fun ServerPlayer.teleportOnFirstJoin() {
        LOGGER.info("Try to teleport on first join")

        val level = level()
        val border = level.worldBorder
        val borderIndent = 25
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
