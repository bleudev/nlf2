@file:Suppress("ClassName")

package com.bleudev.nlf2.sys.event

import com.bleudev.nine_lifes.custom.NineLifesEntities
import com.bleudev.nine_lifes.util.lerp
import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.*
import com.bleudev.nlf2.Nlf2.Companion.battleTicks
import com.bleudev.nlf2.client.custom.gui.screens.ManualId
import com.bleudev.nlf2.client.util.sound
import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.unit.*
import com.bleudev.nlf2.custom.packet.payload.unit.StoryStart
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import com.bleudev.nlf2.sys.util.TextBoxSequence
import com.bleudev.nlf2.sys.util.TextColorConstants
import com.bleudev.nlf2.util.*
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.BossEvent
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.LevelData

abstract class Nlf2Event(protected val server: MinecraftServer, val name: String) {
    protected var ticks: Int = -1

    open fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        ticks = 0
    }
    operator fun invoke(causedBy: ServerPlayer?, vararg args: Any?) = cause(causedBy, args)
    open fun tick() {
        if (ticks >= 0) ticks++
    }
}

class TryTeleportOnFirstJoin(server: MinecraftServer) : Nlf2Event(server, "try_teleport_on_first_join_internal") {
    override fun cause(
        causedBy: ServerPlayer?,
        vararg args: Any?
    ) {
        super.cause(causedBy, *args)
        for (name in server.nlf2data.players.entries.filter { !it.value.teleported }.map { it.key }) {
            val lc = name.lowercase()
            if (lc in TELEPORT_LOCATIONS) {
                server.playerList.getPlayer(name)?.let { pl ->
                    val loc = TELEPORT_LOCATIONS[lc] ?: return@let
                    pl.teleportTo(loc.x, loc.y, loc.z)
                    pl.setRespawnPosition(ServerPlayer.RespawnConfig(LevelData.RespawnData
                        .of(Level.OVERWORLD, loc.toBlockPos(), pl.yRot, pl.xRot), true
                    ), false)
                    server.nlf2data.changePlayerData(name) {
                        it.teleported = true
                        it
                    }
                }
            }
        }
    }
}

class ResetTeleportsOnFirstJoin(server: MinecraftServer) : Nlf2Event(server, "reset_teleports_on_first_join") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        for (name in server.nlf2data.players.keys) {
            server.nlf2data.changePlayerData(name) {
                it.teleported = false
                it
            }
        }
    }
}

open class FI_MeetStateChange(server: MinecraftServer, val meetState: Int, name: String) : Nlf2Event(server, name) {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.meetState = meetState
            it
        }
    }
}

class MeetBeforeCrocko(server: MinecraftServer) : FI_MeetStateChange(server, 0, "meet_before_crocko")
class MeetAfterCrocko(server: MinecraftServer) : FI_MeetStateChange(server, 1, "meet_after_crocko")
class MeetBeforeSam(server: MinecraftServer) : FI_MeetStateChange(server, 2, "meet_before_sam") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.event101 = false
            it
        }
    }
}
class MeetAfterSam(server: MinecraftServer) : FI_MeetStateChange(server, 3, "meet_after_sam")
class MeetBeforeMilkich(server: MinecraftServer) : FI_MeetStateChange(server, 0, "meet_before_milkich") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.forceJukeboxMus = StrangeJukeboxBlock.SelectedMus.STRANGE_NOISE
            it.mobAvoidPlayers = true
            it
        }
    }
}
class MeetAfterMilkich(server: MinecraftServer) : FI_MeetStateChange(server, 1, "meet_after_milkich") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.forceJukeboxMus = StrangeJukeboxBlock.SelectedMus.MILKICH_APPEARS
            it.mobAvoidPlayers = false
            it
        }
    }
}
class MeetBeforeUnknown(server: MinecraftServer) : FI_MeetStateChange(server, 0, "meet_before_unknown") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.playerList.broadcastSystemMessage(
            Component.translatable("multiplayer.player.joined.unknown").withStyle(ChatFormatting.YELLOW),
            false
        )
        server.nlf2data::loreData.transform {
            it.actorChar = "unknown"
            it.visibleActor = true
            it
        }
        server.sendToEveryone { arrayOf(UnknownMeet(UnknownMeet.State.BEFORE_MEET)) }
    }
}
class MeetAfterUnknown(server: MinecraftServer) : FI_MeetStateChange(server, 1, "meet_after_unknown_wip") {
    override fun tick() {
        super.tick()
        var message: Component? = null
        when (ticks) {
            20 -> {
                server.sendToEveryone { arrayOf(UnknownMeet(UnknownMeet.State.AFTER_MEET)) }
                message = Component.literal("<???> Привет ребята")
            }
            100 -> {
                message = Component.literal("<???> Вы меня запомнили?")
            }
            260 -> {
                message = Component.literal("<???> Что-то я вас не слышу")
            }
            290 -> {
                message = Component.literal("<???> Ща")
            }
            350 -> {
                message = Component.literal("<???> Что-то с Войс Чатом")
            }
            380 -> {
                for (player in server.playerList.players) {
                    player.addEffect(MobEffectInstance(MobEffects.DARKNESS, 300, 1, false, false))
                }
            }
            420 -> {
                message = Component.literal("<???> ?")
            }
            480 -> {
                message = Component.literal("<???> Почему потемнело")
            }
            560 -> {
                message = Component.literal("<???> Ой")
            }
            580 -> {
                FI_RedSpectacularEffect(server, "").cause(null)
                for (player in server.playerList.players) {
                    player.sound(Nlf2Sounds.LAUGH, SoundSource.AMBIENT, 1f, .9f)
                }
                server.nlf2data::loreData.transform {
                    it.actorChar = "inv"
                    it.visibleActor = false
                    it
                }
                message = Component.translatable("multiplayer.player.left.unknown").withStyle(ChatFormatting.YELLOW)
                server.sendToEveryone { arrayOf(UnknownMeet(UnknownMeet.State.AFTER_ERASE)) }
            }
            590 -> {
                replaceWithStand()
                ticks = -1
            }
        }
        if (message != null) {
            server.playerList.broadcastSystemMessage(message, false)
        }
    }

    private fun replaceWithStand() {
        val actor = server.actor ?: return
        val stand = NineLifesEntities.WANDERING_ARMOR_STAND.create(actor.level(), EntitySpawnReason.EVENT) ?: return
        stand.copyPosition(actor)
        actor.level().addFreshEntity(stand)
    }
}
class MeetBeforeYnc(server: MinecraftServer) : FI_MeetStateChange(server, 0, "meet_before_ync") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.actorChar = PART4_LAST_MEET_MASK
            it.visibleActor = true
            it
        }
    }
}
class MeetAfterYnc(server: MinecraftServer) : FI_MeetStateChange(server, 1, "meet_after_ync")

open class FI_ManualUpdate(server: MinecraftServer, val manualId: Int, name: String) : Nlf2Event(server, name) {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.manualId = manualId
            it
        }
        if (manualId > 0) for (player in server.playerList.players) {
            player.sendPacket(ManualUpdateEffect(player.inventory.any { it.`is`(Nlf2Items.MANUAL) }))
        }
    }
}

class Manual0(server: MinecraftServer) : FI_ManualUpdate(server, ManualId.DEFAULT.id, "manual_0_reset")
class Manual1(server: MinecraftServer) : FI_ManualUpdate(server, ManualId.AFTER_DEBUG_STICK.id, "manual_1_internal")
class Manual2(server: MinecraftServer) : FI_ManualUpdate(server, ManualId.AFTER_AMETHYST_STICK.id, "manual_2_internal")
class Manual3(server: MinecraftServer) : FI_ManualUpdate(server, ManualId.AFTER_AMETHYST_OXEYE_DAISY.id, "manual_3_internal")
class Manual4(server: MinecraftServer) : FI_ManualUpdate(server, ManualId.BEFORE_CHARGED_MANUAL.id, "manual_4") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        val seq = TextBoxSequence()
        seq.color(TextColorConstants.VERT)
        seq.speed(6)
        seq.add("Руководство...", 40)
        val packet = OpenTextBox(seq, 40)
        server.sendToEveryone { arrayOf(packet) }
        super.cause(causedBy, *args)
    }
}

class OrangeSkinOverlayRemove(server: MinecraftServer) : Nlf2Event(server, "orange_skin_overlay_remove_wip") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        val nickname = if (DEV_EVENT_101) server.actorNickname else EVENT101_NICKNAME
        server.playerList.getPlayer(nickname)?.sendPacket(Event101ScreenEffectClear.INSTANCE)
        server.nlf2data::loreData.transform {
            it.event101 = true
            it
        }
    }
}

open class FI_LabOverlayChange(server: MinecraftServer, val labOverlayState: Int) : Nlf2Event(server, "lab_overlay_$labOverlayState") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.labOverlayState = labOverlayState
            it.compassCrazy = labOverlayState == 1
            it
        }
    }
}

class LabOverlay0(server: MinecraftServer) : FI_LabOverlayChange(server, 0)
class LabOverlay1(server: MinecraftServer) : FI_LabOverlayChange(server, 1)

class EndSerieCrash(server: MinecraftServer) : Nlf2Event(server, "end_serie_crash") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.sendToEveryone { player ->
            if (player.isActor) return@sendToEveryone arrayOf()
            return@sendToEveryone arrayOf(CrashClient.INSTANCE)
        }
    }
}

open class FI_CompassCrazy(server: MinecraftServer, val crazy: Boolean) : Nlf2Event(server, "compass_crazy_$crazy") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.compassCrazy = crazy
            it
        }
    }
}

class CompassCrazyFalse(server: MinecraftServer) : FI_CompassCrazy(server, false)
class CompassCrazyTrue(server: MinecraftServer) : FI_CompassCrazy(server, true)

open class FI_ForceJukeboxMus(server: MinecraftServer, val mus: StrangeJukeboxBlock.SelectedMus) : Nlf2Event(server, "force_jukebox_mus_${mus.mus_name}") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.forceJukeboxMus = mus
            it.jukeboxSleep = mus == StrangeJukeboxBlock.SelectedMus.JUKEBOX_SLEEP
            it
        }
    }
}

class ForceJukeboxNull(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.NULL)
class ForceJukeboxStrangeNoise(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.STRANGE_NOISE)
class ForceJukeboxMilkichAppears(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.MILKICH_APPEARS)
class ForceJukeboxSleep(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.JUKEBOX_SLEEP)

open class FI_RedSpectacularEffect(server: MinecraftServer, appearsName: String) : Nlf2Event(server, "red_${appearsName}_spectacularly") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        val actor = server.actor ?: return
        val level = actor.level()
        level.sendParticles(ParticleTypes.LARGE_SMOKE, actor.x, actor.y, actor.z, 200, 0.0, 0.5, 0.0, 0.03)
        level.sendParticles(ParticleTypes.LARGE_SMOKE, actor.x, actor.y+1, actor.z, 200, 0.0, 0.5, 0.0, 0.03)
        EventEntrypoint.cause(causedBy, 65)
    }
}

class RedAppearsSpectacularly(server: MinecraftServer) : FI_RedSpectacularEffect(server, "appears") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.labOverlayState = 1
            it.mobAvoidPlayers = true
            it
        }
        val stack = ItemStack(Items.DEBUG_STICK)
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("HAVE FUN"))
        server.actor?.inventory?.add(stack)
        server.actor?.inventory?.add(ItemStack(Nlf2Items.WD_PUZZLE))
    }

    override fun tick() {
        super.tick()
        if (ticks == 10) {
            server.nlf2data::loreData.transform {
                it.actorChar = "rouge"
                it
            }
            ticks = -1
        }
    }
}

class RedDisappearsSpectacularly(server: MinecraftServer) : FI_RedSpectacularEffect(server, "disappears") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.actorChar = "inv"
            it.labOverlayState = 0
            it.mobAvoidPlayers = false
            it
        }
    }
}

open class FI_SkyColorEffect(server: MinecraftServer, val color: Int, name: String) : Nlf2Event(server, "sky_color_$name") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        for (player in server.playerList.players) {
            player.sendPacket(SkyColorEffect(5, 100, color))

            player.sound(SoundEvents.WEATHER_END_FLASH, SoundSource.AMBIENT)
        }
    }
}

class RedAppearSky(server: MinecraftServer) : FI_SkyColorEffect(server, 0xbbff0000.toInt(), "red_appear_internal")

open class FI_MobAvoidPlayers(server: MinecraftServer, val avoid: Boolean) : Nlf2Event(server, "mob_avoid_players_$avoid") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.mobAvoidPlayers = avoid
            it
        }
    }
}

class MobAvoidPlayersFalse(server: MinecraftServer) : FI_MobAvoidPlayers(server, false)
class MobAvoidPlayersTrue(server: MinecraftServer) : FI_MobAvoidPlayers(server, true)

private fun tryPickItem(player: ServerPlayer, itemStack: ItemStack) {
    if (itemStack.isItemEnabled(player.level().enabledFeatures())) {
        val inventory = player.inventory
        val slotWithExistingItem = inventory.findSlotMatchingItem(itemStack)
        if (slotWithExistingItem != -1) {
            if (Inventory.isHotbarSlot(slotWithExistingItem)) {
                inventory.selectedSlot = slotWithExistingItem
            } else {
                inventory.pickSlot(slotWithExistingItem)
            }
        } else if (player.hasInfiniteMaterials()) {
            inventory.addAndPickItem(itemStack)
        }
        ServerPlayNetworking.getSender(player).sendPacket(ClientboundSetHeldSlotPacket(inventory.selectedSlot))
        player.inventoryMenu.broadcastChanges()
    }
}

class ForcePickManual(server: MinecraftServer) : Nlf2Event(server, "force_pick_manual") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)

        for (player in server.playerList.players) {
            if (player.isActor) continue
            val itemStack = player.inventory.find { it.`is`(Nlf2Items.MANUAL) } ?: continue
            tryPickItem(player, itemStack)
        }
    }
}

class StealStrangeJukebox(server: MinecraftServer) : Nlf2Event(server, "steal_strange_jukebox") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        var count = 0
        for (player in server.playerList.players) {
            if (player.isActor) continue
            count += player.inventory.clearOrCountMatchingItems({ it.`is`(Nlf2Blocks.STRANGE_JUKEBOX.asItem()) }, -1, player.inventoryMenu.craftSlots)
        }
        if (causedBy == null || count <= 0) return
        while (count > 0) {
            val var1 = count.coerceAtMost(64)
            causedBy.inventory.add(ItemStack(Nlf2Blocks.STRANGE_JUKEBOX, var1))
            count -= var1
        }
        val itemStack = causedBy.inventory.find { it.`is`(Nlf2Blocks.STRANGE_JUKEBOX.asItem()) } ?: return
        tryPickItem(causedBy, itemStack)
    }
}

class StoryStart(server: MinecraftServer) : Nlf2Event(server, "story_start_wip") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        server.playerList.players.forEach {
            if (it.wantsToListenStory) {
                it.sendPacket(StoryStart.INSTANCE)
            }
        }
        Nlf2.storyStarted(server)
    }
}

// Debug Like
class `RIP Vert(`(server: MinecraftServer) : Nlf2Event(server, "RIP VERT( (internal)") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        val seq = TextBoxSequence()
        seq.color(TextColorConstants.VERT)
        seq.speed(4)
        seq.add("Я бы хотел&вам помочь...", 20)
        seq.add("Правда...&Очень...", 20)
        seq.add("Но я, увы,&не могу...", 20)
        seq.add("Физически...", 20)
        seq.add("Прощайте...", 20)
        seq.speed(6)
        seq.add("ОН ЗДЕСЬ", 40)
        val shakeStart = seq.start - 30
        seq.full(true)
        val lagStart = seq.start
        seq.add("ОN ZДЕSЬ", 10)
        seq.add("OН ЗDES'", 10)
        seq.add("ON ZDES'", 10)
        repeat(10) {
            seq.add("ОN ZДЕSЬ", 2)
            seq.add("OН ЗDES'", 2)
            seq.add("ON ZDES'", 2)
        }
        val packets = arrayOf(
            OpenTextBox(seq, 0),
            RIPVertEvent(shakeStart, lagStart)
        )
        server.sendToEveryone { pl -> packets.takeIf { pl.wantsToListenStory } ?: arrayOf() }
    }
}

class `RIP Sanya(`(server: MinecraftServer) : Nlf2Event(server, "RIP SANYA( (internal)") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        val seq = TextBoxSequence()
        seq.color(TextColorConstants.ROUGE)
        seq.speed(7)
        seq.add("YOU", 50)
        seq.wait(40)
        seq.speed(5)
        seq.add("I CAN ADD YOU&I CAN REMOVE YOU", 50)
        seq.speed(9)
        seq.add("SAY GOODBYE", 90)
        val bye = seq.start - 30
        val packets = arrayOf(
            OpenTextBox(seq, 20),
            RIPSanyaEvent(bye)
        )
        server.sendToEveryone { pl -> packets.takeIf { pl.wantsToListenStory } ?: arrayOf() }
        Nlf2.ripSanyaBeforeTakeTicks = bye + RIP_SANYA_EVENT_WHITE_FADE
    }
}

class RougeExposure(server: MinecraftServer) : Nlf2Event(server, "rouge_exposure") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.sendToEveryone { arrayOf(StartWhitenessScreenAdvanced(50, 20, 20)) }
    }

    override fun tick() {
        super.tick()
        val overworld = server.overworld()
        when (ticks) {
            25 -> {
                server.nlf2data::loreData.transform {
                    it.actorChar = "rouge"
                    it.visibleActor = false
                    it
                }
                val team = server.scoreboard.addPlayerTeam("bleuers")
                team.isAllowFriendlyFire = false
                for (player in server.playerList.players) {
                    fun rd(): Double {
                        if (player.isActor) return 0.0
                        var d = (player.random.nextFloat() - .5f) * 2
                        if (d >= -.25f && d <= .25f) d = if (d < 0) -.25f else .25f
                        return d.lerp(-3f, 3f).toDouble()
                    }
                    player.teleportTo(
                        BATTLE_ARENA_LOCATION.x + rd(),
                        BATTLE_ARENA_LOCATION.y,
                        BATTLE_ARENA_LOCATION.z + rd()
                    )
                    if (!player.isActor) {
                        server.scoreboard.addPlayerToTeam(player.scoreboardName, team)
                    }
                }
                for (entity in overworld.allEntities.filterNotNull()) {
                    if (entity !is Player) {
                        entity.kill(overworld)
                    }
                }
            }
            70 -> {
                val seq = TextBoxSequence()
                seq.color(TextColorConstants.ROUGE)
                seq.speed(3)
                seq.add("ROUGE!", 60)
                server.sendToEveryone { arrayOf(OpenTextBox(seq, 0)) }
            }
            150 -> {
                battleTicks = 0
                server.nlf2data::loreData.transform {
                    it.inBattle = true
                    it
                }
                server.actor?.addEffect(MobEffectInstance(MobEffects.RESISTANCE, -1, 3, false, false))
                val bossBar = server.customBossEvents.create(
                    overworld.random,
                    BATTLE_BOSS_BAR,
                    Component.literal("ROUGE").withStyle(ChatFormatting.RED)
                )
                for (player in server.playerList.players) {
                    bossBar.addPlayer(player)
                }
                bossBar.color = BossEvent.BossBarColor.RED
                bossBar.overlay = BossEvent.BossBarOverlay.PROGRESS
                bossBar.progress = 0f

                server.sendToEveryone { arrayOf(StartBattle.INSTANCE) }
                ticks = -1
            }
        }
    }
}

class RougeGoingCrazy(server: MinecraftServer) : Nlf2Event(server, "rouge_going_crazy_internal") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.battleSecond = true
            it
        }
        val seq = TextBoxSequence()
        seq.color(TextColorConstants.ROUGE)
        seq.speed(2)
        seq.add("IT IS MY WORLD", 40)
        seq.add("YOU WILL NEVER", 10)
        seq.add("DEFEAT ME", 40)
        seq.wait(40)
        seq.add("I WILL PUNISH YOU", 40)
        server.sendToEveryone { arrayOf(
            OpenTextBox(seq, 0),
            BattleSecondStart.INSTANCE
        ) }
    }
}
