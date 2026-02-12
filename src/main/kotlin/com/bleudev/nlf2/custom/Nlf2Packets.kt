package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.ClientNlf2DataSync
import com.bleudev.nlf2.custom.packet.payload.OpenPuzzle
import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val CLIENT_NLF2_DATA_SYNC = getIdentifier("packet/client_nlf2_data_sync")
    val OPEN_PUZZLE = getIdentifier("packet/open_puzzle")
    val WORLD_BORDER_NOISE = getIdentifier("packet/world_border_noise")

    val UNIT_CRASH_CLIENT = getIdentifier("packet/unit/crash_client")

    fun initialize() {
        val s2c = PayloadTypeRegistry.clientboundPlay()
        // Normal
        s2c.register(ClientNlf2DataSync)
        s2c.register(OpenPuzzle)
        s2c.register(WorldBorderNoisePayload)
        // Unit
        s2c.register(CrashClientPayload)
    }
}