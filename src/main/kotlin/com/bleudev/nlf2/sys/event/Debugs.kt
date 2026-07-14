package com.bleudev.nlf2.sys.event

import com.bleudev.nlf2.custom.Nlf2Commands.causeEvent
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.util.transform
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

class OrangeSkinOverlay(server: MinecraftServer) : Nlf2Event(server, "orange_skin_overlay") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        server.nlf2data::loreData.transform {
            it.event101 = false
            it
        }
    }
}

class ReTeleportOnFirstJoin(server: MinecraftServer) : Nlf2Event(server, "re_teleport_on_first_join") {
    override fun cause(causedBy: ServerPlayer?, vararg args: Any?) {
        causeEvent(35)
        causeEvent(34)
    }
}