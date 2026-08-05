@file:Suppress("ClassName")

package com.bleudev.nlf2.sys.event

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.unit.StoryStart
import com.bleudev.nlf2.util.isActor
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

class StoryStart(server: MinecraftServer) : Nlf2Event(server, "story_start_wip") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        server.playerList.players.forEach {
            if (!it.isActor) {
                it.sendPacket(StoryStart.INSTANCE)
            }
        }
    }
}
