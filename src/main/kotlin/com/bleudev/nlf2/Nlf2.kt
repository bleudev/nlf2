package com.bleudev.nlf2

import com.bleudev.nine_lifes.*
import com.bleudev.nine_lifes.custom.NineLifesCriterions
import com.bleudev.nine_lifes.custom.NineLifesEntities
import com.bleudev.nine_lifes.custom.NineLifesItems
import com.bleudev.nine_lifes.custom.packet.payload.StartChargeScreen
import com.bleudev.nine_lifes.custom.packet.payload.unit.ArmorStandKillEvent
import com.bleudev.nine_lifes.util.alwaysTrue
import com.bleudev.nine_lifes.util.entityIn
import com.bleudev.nine_lifes.util.lifes
import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.client.sys.actor.ActorCharEntrypoint
import com.bleudev.nlf2.custom.*
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.packet.payload.BeforeMeetWaterFluid
import com.bleudev.nlf2.custom.packet.payload.DistanceUpdate
import com.bleudev.nlf2.custom.packet.payload.JoinDisconnectVersion
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipSanyaTakeStart
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipVertEnd
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.StoryEnd
import com.bleudev.nlf2.custom.packet.payload.unit.BorderBlackFogStart
import com.bleudev.nlf2.custom.packet.payload.unit.CancelBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.EndBattle
import com.bleudev.nlf2.custom.world.level.saveddata.BorderStates
import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import com.bleudev.nlf2.sys.event.DebugEntrypoint
import com.bleudev.nlf2.sys.event.EventEntrypoint
import com.bleudev.nlf2.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.players.NameAndId
import net.minecraft.server.players.UserBanListEntry
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import net.minecraft.world.level.gamerules.GameRule
import net.minecraft.world.level.gamerules.GameRules
import net.minecraft.world.phys.Vec3
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min


const val MODID = "nlf2"
fun getIdentifier(path: String): Identifier = Identifier.fromNamespaceAndPath(MODID, path)
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    companion object {
        var battleTicks: Int = -1

        private var isStorySpectator = false
        var ripSanyaBeforeTakeTicks = -1
        private var scheduledEvents: ArrayList<ScheduledEvent> = ArrayList()

        fun storyStarted(server: MinecraftServer) {
            scheduledEvents.add(ScheduledEvent(30) {
                isStorySpectator = true
                for (player in server.playerList.players) {
                    if (player.wantsToListenStory) {
                        player.setGameMode(GameType.SPECTATOR)
                        player.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 1640, 255, false, false))
                    }
                }
            })
        }

        fun MinecraftServer.clearPostBattleEffects() {
            actor?.removeAllEffects()
            sendToEveryone { arrayOf(EndBattle.INSTANCE) }
        }
    }

    data class ScheduledEvent(var remainingTicks: Int, val runnable: Runnable) {
        fun tick() {
            if (remainingTicks <= 0) return
            remainingTicks--
            if (remainingTicks <= 0) {
                runnable.run()
            }
        }
    }
    data class BorderLoseHeartsInfo(var ticksBeforeLose: Int, var ticksAfterLose: Int)

    private var ticks = 0
    private var sbSendRipVertTicks = 0
    private var sbSendRipSanyaTicks = 0
    private var sbSendRipSanyaTakeStartTicks = 0
    private var ripVertStartTicks = -1
    private var ripSanyaStartTicks = -1
    private var ripSanyaTakingTicks = -1
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
        Nlf2MobEffects.initialize()
        Nlf2Sounds.initialize()
        ActorCharEntrypoint.initialize()
        serverVersion = FabricLoader.getInstance().getModContainer(MODID).get().metadata.version.friendlyString

        ServerLifecycleEvents.SERVER_STARTED.register {
            EventEntrypoint.initialize(it)
            DebugEntrypoint.initialize(it)
        }
        ServerPlayerEvents.JOIN.register { player ->
            player.level().server.nlf2data.onJoin(player)
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            if (sbSendRipVertTicks > 0) sbSendRipVertTicks--
            if (sbSendRipSanyaTicks > 0) sbSendRipSanyaTicks--
            if (ripVertStartTicks > 0) ripVertStartTicks--
            if (ripVertStartTicks == 0) {
                causeEvent(111)
                ripVertStartTicks = -1
            }
            if (ripSanyaStartTicks > 0) ripSanyaStartTicks--
            if (ripSanyaStartTicks == 0) {
                causeEvent(112)
                ripSanyaStartTicks = -1
            }
            if (ripSanyaTakingTicks > 0) {
                val pl = if (DEV_STORY_ACTOR) server.actor else server.playerList.getPlayer(EVENT112_NICKNAME)
                if (pl != null) {
                    pl.setDeltaMovement(0.0, 0.05, 0.0)
                    pl.needsSync = true
                    pl.noPhysics = true
                    pl.setOnGround(false)
                    pl.isNoGravity = true
                    // TODO: Normal spectator like movement
                    pl.connection.send(ClientboundSetEntityMotionPacket(pl))
                }
                ripSanyaTakingTicks--
            }
            if (ripSanyaTakingTicks == 0) {
                val pl = if (DEV_STORY_ACTOR) server.actor else server.playerList.getPlayer(EVENT112_NICKNAME)
                if (pl != null) {
                    val armorStand = NineLifesEntities.WANDERING_ARMOR_STAND.create(pl.level(), EntitySpawnReason.EVENT)
                    armorStand?.copyPosition(pl)
                    pl.dropAllDeathLoot()
                    if (DEV_BAN_EVENT_112) {
                        // Ban event 112 player
                        server.playerList.bans.add(UserBanListEntry(NameAndId(pl.gameProfile)))
                        pl.connection.disconnect(Component.literal("GOODBYE"))
                    }
                    server.playerList.broadcastSystemMessage(Component
                            .translatable("nlf2.message.event.112.disconnect", pl.gameProfile.name)
                            .withStyle(ChatFormatting.YELLOW),
                        false
                    )
                    server.sendToEveryone(ArmorStandKillEvent.INSTANCE)
                    armorStand?.let { pl.level().addFreshEntity(it) }
                }
                ripSanyaTakingTicks = -1
            }

            clearVertItemsCludge = server.nlf2data.loreData.noVert
            clearVertItemsIfNeed(server)
            val pl = if (DEV_EVENT_85) server.actor else server.playerList.getPlayer(EVENT85_NICKNAME)
            pl?.run {
                sendPacket(BeforeMeetWaterFluid(
                    server.nlf2data.loreData.meetState % 2 == 0 && isInWater
                ))
            }
            server.sendToEveryone { player ->
                var ev101d = 999f
                if (!server.nlf2data.loreData.event101) {
                    val ev101 = if (DEV_EVENT_101) server.actor else server.playerList.getPlayer(EVENT101_NICKNAME)
                    if (ev101 != null) ev101d = ev101.distanceTo(player)
                }
                var daisyd = 999.0
                for (x in -3..3) for (y in -3..3) for (z in -3..3) {
                    val p = player.blockPosition().offset(x, y, z)
                    if (player.level().getBlockState(p).`is`(Nlf2Blocks.AMETHYST_OXEYE_DAISY)) {
                        daisyd = min(daisyd, player.position().distanceTo(Vec3(p)))
                    }
                }
                arrayOf(DistanceUpdate(
                    player.level().worldBorder.getDistanceToBorder(player),
                    ev101d,
                    daisyd,
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
            if (server.nlf2data.loreData.manualId < 1 && shouldUpdate(Items.DEBUG_STICK)) causeEvent(93)
            if (server.nlf2data.loreData.manualId < 2 && shouldUpdate(NineLifesItems.AMETHYST_STICK)) causeEvent(94)
            if (server.nlf2data.loreData.manualId < 3 && shouldUpdate(Nlf2Blocks.AMETHYST_OXEYE_DAISY.asItem())) causeEvent(95)

            server.playerList.players.forEach {
                val wpm = it.level().waypointManager
                if (it.isActor) wpm.untrackWaypoint(it)
                else wpm.trackWaypoint(it)
                borderLoseHeartsTick(it)
            }
            // Events
            EventEntrypoint.tick()
            // Scheduled
            scheduledEvents.forEach(ScheduledEvent::tick)
            scheduledEvents.removeAll { it.remainingTicks <= 0 }
            // Game rules
            gameRulesTick(server)
            // Charge manual
            val chargeScreenEffectRadiusDiff = CHARGE_SCREEN_EFFECT_RADIUS_MAX - CHARGE_SCREEN_EFFECT_RADIUS_MIN
            for (level in server.allLevels) {
                for (lightningBolt in level.getEntities(EntityTypes.LIGHTNING_BOLT, alwaysTrue())) {
                    for (itemEntity in level.getEntities(EntityTypes.ITEM, entityIn(lightningBolt.position(), LIGHTNING_CHARGING_RADIUS))) {
                        if (itemEntity.item.`is`(Nlf2Items.MANUAL)) {
                            itemEntity.item = itemEntity.item.transmuteCopy(Nlf2Items.CHARGED_MANUAL)
                        }
                        level.players().forEach { player ->
                            val distance = (player.position().distanceTo(itemEntity.position()) - CHARGE_SCREEN_EFFECT_RADIUS_MIN)
                                .coerceAtLeast(.0)
                            NineLifesCriterions.CHARGE_ITEM.trigger(player, itemEntity.item.item, distance)
                            val strength = CHARGE_SCREEN_MAX_STRENGTH * (chargeScreenEffectRadiusDiff - distance) / chargeScreenEffectRadiusDiff
                            player.sendPacket(StartChargeScreen(CHARGE_SCREEN_DURATION, strength.toFloat()))
                        }
                    }
                }
            }
            // Story
            if (isStorySpectator) {
                for (player in server.playerList.players) {
                    if (player.wantsToListenStory) {
                        player.deltaMovement = Vec3.ZERO
                        player.needsSync = true
                        player.connection.send(ClientboundSetEntityMotionPacket(player))
                    }
                }
            }
            // Battle
            if (battleTicks >= 0) battleTicks++
            if (server.nlf2data.loreData.inBattle) {
                server.customBossEvents.get(BATTLE_BOSS_BAR)?.let { bossBar ->
                    if (battleTicks in 0..BATTLE_BOSSBAR_FADE_IN_DURATION) {
                        bossBar.progress = (battleTicks.toFloat() / BATTLE_BOSSBAR_FADE_IN_DURATION).coerceIn(0f, 1f)
                        server.actor?.setGameMode(GameType.CREATIVE)
                    } else {
                        battleTicks = -1
                        server.actor?.setGameMode(GameType.SURVIVAL)
                        server.actor?.let {
                            bossBar.setValue(it.health.toInt())
                            bossBar.setMax(it.maxHealth.toInt())
                        }
                    }
                }
                if (server.actor?.isDeadOrDying == true) {
                    server.nlf2data::loreData.transform {
                        it.inBattle = false
                        it
                    }
                    // THE END!
                    server.clearPostBattleEffects()
                    server.halt(false)
                }
                server.actor?.foodData?.foodLevel = 10
            } else {
                server.customBossEvents.get(BATTLE_BOSS_BAR)?.let {
                    it.removeAllPlayers()
                    server.customBossEvents.remove(it)
                }
            }
        }
        EntityElytraEvents.CUSTOM.register { entity, _ ->
            entity is Player && entity.hasEffect(Nlf2MobEffects.GLIDING)
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
        registerPacketReceiver(RipSanyaTakeStart) {
            if (sbSendRipSanyaTakeStartTicks > 0) return@registerPacketReceiver
            sbSendRipSanyaTakeStartTicks = 200
            ripSanyaTakingTicks = RIP_SANYA_EVENT_SANYA_TAKE
        }
        registerPacketReceiver(RipVertEnd) { _, ctx ->
            if (sbSendRipVertTicks > 0) return@registerPacketReceiver
            sbSendRipVertTicks = 200
            ctx.server().nlf2data::loreData.transform {
                it.noVert = true
                it
            }
            clearVertItemsIfNeed(ctx.server())
            ripSanyaStartTicks = 200
        }
        registerPacketReceiver(StoryEnd) { _, ctx ->
            if (sbSendRipSanyaTicks > 0) return@registerPacketReceiver
            sbSendRipSanyaTicks = 200
            isStorySpectator = false
            for (player in ctx.server().playerList.players) {
                if (player.wantsToListenStory) {
                    player.setGameMode(GameType.SURVIVAL)
                }
            }
            ripVertStartTicks = 80
        }
    }

    private fun gameRulesTick(server: MinecraftServer) {
        val rules = server.gameRules
        operator fun <T : Any> GameRules.set(rule: GameRule<T>, value: T) {
            if (get(rule) != value) {
                set(rule, value, server)
            }
        }
        rules[GameRules.SHOW_ADVANCEMENT_MESSAGES] = false //
        rules[GameRules.PLAYERS_SLEEPING_PERCENTAGE] = 1

        val bl = server.nlf2data.loreData.inBattle
        rules[GameRules.SPAWN_MOBS] = !bl
        rules[GameRules.FALL_DAMAGE] = !bl
    }

    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T, ctx: ServerPlayNetworking.Context) -> Unit) =
        ServerPlayNetworking.registerGlobalReceiver(packet.type, handler)
    private fun <T : CustomPacketPayload> registerPacketReceiver(packet: PacketPayloadCompanion<T>, handler: (payload: T) -> Unit) =
        registerPacketReceiver(packet) {payload, _ -> handler(payload)}
}
