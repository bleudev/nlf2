package com.bleudev.nlf2.sys.event

import com.bleudev.nlf2.LOGGER
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object EventEntrypoint {
    private var initialized = false
    private val events = HashMap<Int, Nlf2Event>()
    val names = HashMap<Int, String>()

    fun initialize(server: MinecraftServer) {
        if (initialized) return
        initialized = true
        fun add(id: Int, constr: (MinecraftServer) -> Nlf2Event) {
            val ev = constr(server)
            events[id] = ev
            names[id] = ev.name
        }

        add(34, ::TryTeleportOnFirstJoin)
        add(35, ::ResetTeleportsOnFirstJoin)
        add(65, ::RedAppearSky)
        add(66, ::RedAppearsSpectacularly)
        add(67, ::RedDisappearsSpectacularly)
        add(68, ::ForcePickManual)
        add(69, ::StealStrangeJukebox)
        // Meets
        add(80, ::MeetBeforeCrocko)
        add(81, ::MeetAfterCrocko)
        add(82, ::MeetBeforeSam)
        add(83, ::MeetAfterSam)
        add(84, ::MeetBeforeMilkich)
        add(85, ::MeetAfterMilkich)
        // Manual updating
        add(92, ::Manual0)
        add(93, ::Manual1)
        add(94, ::Manual2)
        add(95, ::Manual3)

        add(101, ::OrangeSkinOverlayRemove)
        add(140, ::LabOverlay0)
        add(141, ::LabOverlay1)
        add(189, ::EndSerieCrash)
        add(200, ::CompassCrazyFalse)
        add(201, ::CompassCrazyTrue)
        add(300, ::MobAvoidPlayersFalse)
        add(301, ::MobAvoidPlayersTrue)
        // Force jukebox
        add(340, ::ForceJukeboxNull)
        add(341, ::ForceJukeboxStrangeNoise)
        add(342, ::ForceJukeboxMilkichAppears)
    }

    fun cause(causedBy: ServerPlayer?, id: Int, vararg args: Any?): Boolean {
        LOGGER.info("Caused nlf2:{} event", names[id])
        val ev = events[id] ?: return false
        ev.cause(causedBy, args)
        return true
    }

    fun tick() {
        events.values.toList().forEach(Nlf2Event::tick)
    }
}