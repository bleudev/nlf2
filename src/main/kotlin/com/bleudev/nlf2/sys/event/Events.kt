@file:Suppress("ClassName")

package com.bleudev.nlf2.sys.event

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.TELEPORT_LOCATIONS
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.ManualUpdateEffect
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.Event101ScreenEffectClear
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.util.*
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

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
        for (name in server.nlf2data.getPlayers().filter { !it.teleported }.map { it.name }) {
            val lc = name.lowercase()
            if (lc in TELEPORT_LOCATIONS) {
                server.playerList.getPlayer(name)?.let { pl ->
                    val loc = TELEPORT_LOCATIONS[lc] ?: return@let
                    pl.teleportTo(loc.x, loc.y, loc.z)
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
        for (name in server.nlf2data.getPlayers().map { it.name }) {
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
            if (meetState == 2) it.event101 = false
            it
        }
    }
}

class MeetBeforeCrocko(server: MinecraftServer) : FI_MeetStateChange(server, 0, "meet_before_crocko")
class MeetAfterCrocko(server: MinecraftServer) : FI_MeetStateChange(server, 1, "meet_after_crocko")
class MeetBeforeWaflla(server: MinecraftServer) : FI_MeetStateChange(server, 2, "meet_before_waflla")
class MeetAfterWaflla(server: MinecraftServer) : FI_MeetStateChange(server, 3, "meet_after_waflla")

open class FI_ManualUpdate(server: MinecraftServer, val manualId: Int, name: String) : Nlf2Event(server, name) {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.manualId = manualId
            it
        }
        if (manualId == 0) return
        for (player in server.playerList.players) {
            player.sendPacket(ManualUpdateEffect(player.inventory.any { it.`is`(Nlf2Items.MANUAL) }))
        }
    }
}

class Manual0(server: MinecraftServer) : FI_ManualUpdate(server, 0, "manual_0_reset")
class Manual1(server: MinecraftServer) : FI_ManualUpdate(server, 1, "manual_1_internal")
class Manual2(server: MinecraftServer) : FI_ManualUpdate(server, 2, "manual_2_internal")

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
            return@sendToEveryone arrayOf(CrashClientPayload.INSTANCE)
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

class CompassCrazyTrue(server: MinecraftServer) : FI_CompassCrazy(server, true)
class CompassCrazyFalse(server: MinecraftServer) : FI_CompassCrazy(server, false)

open class FI_ForceJukeboxMus(server: MinecraftServer, val mus: StrangeJukeboxBlock.SelectedMus) : Nlf2Event(server, "force_jukebox_mus_${mus.mus_name}") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.forceJukeboxMus = mus
            it
        }
    }
}

class ForceJukeboxNull(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.NULL)
class ForceJukeboxStrangeNoise(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.STRANGE_NOISE)
class ForceJukeboxMilkichAppears(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.MILKICH_APPEARS)

open class FI_RedSpectacularEffect(server: MinecraftServer, appearsName: String) : Nlf2Event(server, "red_${appearsName}_spectacularly") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        val actor = server.actor ?: return
        val level = actor.level()
        level.sendParticles(ParticleTypes.LARGE_SMOKE, actor.x, actor.y, actor.z, 200, 0.0, 0.5, 0.0, 0.03)
        level.sendParticles(ParticleTypes.LARGE_SMOKE, actor.x, actor.y+1, actor.z, 200, 0.0, 0.5, 0.0, 0.03)
    }
}

class RedAppearsSpectacularly(server: MinecraftServer) : FI_RedSpectacularEffect(server, "appears") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.labOverlayState = 1
            it
        }
        val stack = ItemStack(Items.DEBUG_STICK)
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("HAVE FUN"))
        server.actor?.inventory?.add(stack)
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
            it
        }
    }
}
