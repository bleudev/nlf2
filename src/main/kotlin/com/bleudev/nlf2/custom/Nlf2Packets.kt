package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.BorderBlackFogStart
import com.bleudev.nlf2.custom.packet.payload.unit.CancelBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.custom.packet.payload.unit.LostVesselControlActivate
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val CLIENT_NLF2_DATA_SYNC = getIdentifier("packet/client_nlf2_data_sync")
    val OPEN_MANUAL = getIdentifier("packet/open_manual")
    val OPEN_PUZZLE = getIdentifier("packet/open_puzzle")
    val WORLD_BORDER_NOISE = getIdentifier("packet/world_border_noise")
    val JOIN_DISCONNECT_VERSION = getIdentifier("packet/join_disconnect_version")

    val UNIT_CRASH_CLIENT = getIdentifier("packet/unit/crash_client")
    val UNIT_BORDER_BLACK_FOG_START = getIdentifier("packet/unit/border_black_fog_start")
    val UNIT_CANCEL_BORDER_BLACK_FOG = getIdentifier("packet/unit/cancel_border_black_fog")
    val UNIT_LOST_VESSEL_CONTROL_ACTIVATE = getIdentifier("packet/unit/lost_vessel_control_activate")

    val SB_JOIN_SEND_VERSION = getIdentifier("packet/sb/join_send_version")
    val SB_KILL_BORDER_BLACK_FOG = getIdentifier("packet/sb/kill_border_black_fog")

    fun initialize() {
        // Client bound
        val s2c = PayloadTypeRegistry.clientboundPlay()
        // Normal
        s2c.register(ClientNlf2DataSync)
        s2c.register(OpenManual)
        s2c.register(OpenPuzzle)
        s2c.register(WorldBorderNoisePayload)
        s2c.register(JoinDisconnectVersion)
        // Unit
        s2c.register(CrashClientPayload)
        s2c.register(BorderBlackFogStart)
        s2c.register(CancelBorderBlackFog)
        s2c.register(LostVesselControlActivate)

        // Server bound
        val c2s = PayloadTypeRegistry.serverboundPlay()
        // Normal
        c2s.register(JoinSendVersion)
        // Unit
        c2s.register(KillBorderBlackFog)
    }
}