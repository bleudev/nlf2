@file:Suppress("ClassName")

package com.bleudev.nlf2.sys.event

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.TELEPORT_LOCATIONS
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.Event101ScreenEffectClear
import com.bleudev.nlf2.custom.packet.payload.unit.ManualUpdateEffect
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.util.actorNickname
import com.bleudev.nlf2.util.isActor
import com.bleudev.nlf2.util.sendToEveryone
import com.bleudev.nlf2.util.transform
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

abstract class Nlf2Event(protected val server: MinecraftServer, val name: String) {
    abstract fun cause(causedBy: ServerPlayer?, vararg args: Any?)
    operator fun invoke(causedBy: ServerPlayer?, vararg args: Any?) = cause(causedBy, args)
}

class TryTeleportOnFirstJoin(server: MinecraftServer) : Nlf2Event(server, "try_teleport_on_first_join_internal") {
    override fun cause(
        causedBy: ServerPlayer?,
        vararg args: Any?
    ) {
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
        server.nlf2data::loreData.transform {
            it.manualId = manualId
            it
        }
        if (manualId == 0) return
        val owner = (args[0] as? ServerPlayer?) ?: return
        owner.sendPacket(ManualUpdateEffect.INSTANCE)
    }
}

class Manual0(server: MinecraftServer) : FI_ManualUpdate(server, 0, "manual_0_reset")
class Manual1(server: MinecraftServer) : FI_ManualUpdate(server, 1, "manual_1_internal")
class Manual2(server: MinecraftServer) : FI_ManualUpdate(server, 2, "manual_2_internal")

class OrangeSkinOverlayRemove(server: MinecraftServer) : Nlf2Event(server, "orange_skin_overlay_remove_wip") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
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
        server.sendToEveryone { player ->
            if (player.isActor) return@sendToEveryone arrayOf()
            return@sendToEveryone arrayOf(CrashClientPayload.INSTANCE)
        }
    }
}

open class FI_CompassCrazy(server: MinecraftServer, val crazy: Boolean) : Nlf2Event(server, "compass_crazy_$crazy") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
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
        server.nlf2data::loreData.transform {
            it.forceJukeboxMus = mus
            it
        }
    }
}

class ForceJukeboxNull(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.NULL)
class ForceJukeboxStrangeNoise(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.STRANGE_NOISE)
class ForceJukeboxMilkichAppears(server: MinecraftServer) : FI_ForceJukeboxMus(server, StrangeJukeboxBlock.SelectedMus.MILKICH_APPEARS)

