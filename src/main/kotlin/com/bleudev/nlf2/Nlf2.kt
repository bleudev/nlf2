package com.bleudev.nlf2

import com.bleudev.nine_lifes.custom.NineLifesItems
import com.bleudev.nine_lifes.util.lifes
import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.custom.*
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.data.saved.BorderStates
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.DistanceUpdate
import com.bleudev.nlf2.custom.packet.payload.JoinDisconnectVersion
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.BorderBlackFogStart
import com.bleudev.nlf2.custom.packet.payload.unit.CancelBorderBlackFog
import com.bleudev.nlf2.sys.event.DebugEntrypoint
import com.bleudev.nlf2.sys.event.EventEntrypoint
import com.bleudev.nlf2.util.actor
import com.bleudev.nlf2.util.hurtUnknown
import com.bleudev.nlf2.util.isActor
import com.bleudev.nlf2.util.sendToEveryone
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val MODID = "nlf2"
fun getIdentifier(path: String): Identifier = Identifier.fromNamespaceAndPath(MODID, path)
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    data class BorderLoseHeartsInfo(var ticksBeforeLose: Int, var ticksAfterLose: Int)

    private var ticks = 0
    private var borderLoseHeartsInfos: HashMap<String, BorderLoseHeartsInfo> = HashMap()
    private lateinit var serverVersion: String

    private fun borderLoseHeartsTick(player: ServerPlayer) {
        if (player.level().server.nlf2data.loreData.borderState != BorderStates.STANDARD_LOSE_HEARTS) return

        val nickname = player.gameProfile.name.lowercase()
        val should = player.gameMode.isSurvival && !player.isActor &&
            player.level().worldBorder.getDistanceToBorder(player) <= BORDER_LOSE_HEARTS_DISTANCE
        if (!borderLoseHeartsInfos.containsKey(nickname)) borderLoseHeartsInfos[nickname] = BorderLoseHeartsInfo(-1, -1)
        val info = borderLoseHeartsInfos[nickname]!!
        if (should) {
            if (info.ticksAfterLose == -1) {
                if (info.ticksBeforeLose == -1) info.ticksBeforeLose = BORDER_LOSE_HEARTS_BEFORE_DURATION
                else if (info.ticksBeforeLose > 0) info.ticksBeforeLose--
                if (info.ticksBeforeLose == 0) {
                    info.ticksBeforeLose = -1
                    info.ticksAfterLose = BORDER_LOSE_HEARTS_AFTER_DURATION
                    player.hurtUnknown(2f)
                    player.getAttribute(Attributes.MAX_HEALTH)?.let {
                        val mdf = AttributeModifier(MOD_BORDER_LOSE_HEARTS, -2.0, AttributeModifier.Operation.ADD_VALUE)
                        it.removeModifier(mdf)
                        it.addPermanentModifier(mdf)
                    }
                }
            }
            else if (info.ticksAfterLose > 0) info.ticksAfterLose--
        } else if (info.ticksAfterLose > 0) info.ticksAfterLose--

        if (info.ticksAfterLose == 0) {
            info.ticksBeforeLose = -1
            info.ticksAfterLose = -1
            player.getAttribute(Attributes.MAX_HEALTH)?.removeModifier(MOD_BORDER_LOSE_HEARTS)
        }
        borderLoseHeartsInfos[nickname] = info
    }

    override fun onInitialize() {
        Nlf2Commands.initialize()
        Nlf2Items.initialize()
        Nlf2Packets.initialize()
        Nlf2Blocks.initialize()
        Nlf2BlockEntities.initialize()
        serverVersion = FabricLoader.getInstance().getModContainer(MODID).get().metadata.version.friendlyString

        ServerLifecycleEvents.SERVER_STARTED.register {
            EventEntrypoint.initialize(it)
            DebugEntrypoint.initialize(it)
        }
        ServerPlayerEvents.JOIN.register { player ->
            player.level().server.nlf2data.onJoin(player)
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.sendToEveryone { player ->
                var ev101d = 999f
                if (!server.nlf2data.loreData.event101) {
                    val ev101 = if (DEV_EVENT_101) server.actor else server.playerList.getPlayer(EVENT101_NICKNAME)
                    if (ev101 != null) ev101d = ev101.distanceTo(player)
                }
                arrayOf(DistanceUpdate(
                    player.level().worldBorder.getDistanceToBorder(player),
                    ev101d,
                )) }
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
                server.playerList.players.any { player -> !player.isActor && player.inventory.any { (!it.isEmpty) && it.`is`(item) } }
            if (shouldUpdate(Items.DEBUG_STICK) && server.nlf2data.loreData.manualId == 0) causeEvent(93)
            if (shouldUpdate(NineLifesItems.AMETHYST_STICK) && server.nlf2data.loreData.manualId == 1) causeEvent(94)

            server.playerList.players.forEach {
                val wpm = it.level().waypointManager
                if (it.isActor) wpm.untrackWaypoint(it)
                else wpm.trackWaypoint(it)
                borderLoseHeartsTick(it)
            }
            // Events
            EventEntrypoint.tick()
        }

        registerPacketReceiver(JoinSendVersion) { payload, ctx ->
            val clientVersion = payload.version
            val player = ctx.player()

            if (Version.parse(serverVersion) > Version.parse(clientVersion)) {
                player.setGameMode(GameType.SPECTATOR)
                player.sendPacket(JoinDisconnectVersion(clientVersion, serverVersion))
            } else {
                if (player.gameMode() == GameType.SPECTATOR && player.lifes >= 1 && !player.isActor) {
                    player.setGameMode(GameType.SURVIVAL)
                }
            }
        }

        registerPacketReceiver(KillBorderBlackFog) { _, ctx ->
            if (ctx.server().nlf2data.loreData.borderState != 2) return@registerPacketReceiver
            ctx.player().hurtUnknown(9999f)
        }
    }

    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T, ctx: ServerPlayNetworking.Context) -> Unit) =
        ServerPlayNetworking.registerGlobalReceiver(packet.type, handler)
    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T) -> Unit) =
        registerPacketReceiver(packet) {payload, _ -> handler(payload)}
}
