package com.bleudev.nlf2.sys.event

import com.bleudev.nlf2.LOGGER
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object DebugEntrypoint {
    private var initialized = false
    private val debugs = HashMap<Int, Nlf2Event>()
    val names = HashMap<Int, String>()

    fun initialize(server: MinecraftServer) {
        if (initialized) return
        initialized = true
        fun add(id: Int, constr: (MinecraftServer) -> Nlf2Event) {
            val ev = constr(server)
            debugs[id] = ev
            names[id] = ev.name
        }

        add(34, ::ReTeleportOnFirstJoin)
        add(55, ::RedSkyTest)
        add(56, ::GreenSkyTest)
        add(57, ::BlueSkyTest)
        add(58, ::WhiteSkyTest)
        add(101, ::OrangeSkinOverlay)
    }

    fun cause(causedBy: ServerPlayer?, id: Int, vararg args: Any?): Boolean {
        LOGGER.info("Caused nlf2:{} debug", names[id])
        val ev = debugs[id] ?: return false
        ev.cause(causedBy, args)
        return true
    }
}