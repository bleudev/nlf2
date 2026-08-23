@file:Suppress("ClassName")

package com.bleudev.nlf2.sys.event

import com.bleudev.nlf2.Nlf2.Companion.clearPostBattleEffects
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import com.bleudev.nlf2.util.transform
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

class OrangeSkinOverlay(server: MinecraftServer) : Nlf2Event(server, "orange_skin_overlay") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.event101 = false
            it
        }
    }
}

class ReTeleportOnFirstJoin(server: MinecraftServer) : Nlf2Event(server, "re_teleport_on_first_join") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        causeEvent(35)
        causeEvent(34)
    }
}

class RedSkyTest(server: MinecraftServer) : FI_SkyColorEffect(server, 0xffff0000.toInt(), "red")
class GreenSkyTest(server: MinecraftServer) : FI_SkyColorEffect(server, 0xff00ff00.toInt(), "green")
class BlueSkyTest(server: MinecraftServer) : FI_SkyColorEffect(server, 0xff0000ff.toInt(), "blue")
class WhiteSkyTest(server: MinecraftServer) : FI_SkyColorEffect(server, 0xffffffff.toInt(), "white")

class FI_NoVert(server: MinecraftServer, val new: Boolean) : Nlf2Event(server, "no_vert_$new") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.noVert = new
            it
        }
    }
}

class ResetVisibleActor(server: MinecraftServer) : Nlf2Event(server, "reset_visible_actor") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.visibleActor = false
            it
        }
    }
}

class EndBattle(server: MinecraftServer) : Nlf2Event(server, "end_battle") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        super.cause(causedBy, *args)
        server.nlf2data::loreData.transform {
            it.inBattle = false
            it
        }
        server.clearPostBattleEffects()
    }
}
