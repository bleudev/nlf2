package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.custom.packet.payload.unit.CrashClientPayload
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val WORLD_BORDER_NOISE = getIdentifier("packet/world_border_noise")

    val UNIT_CRASH_CLIENT = getIdentifier("packet/unit/crash_client")

    fun initialize() {
        val s2c = PayloadTypeRegistry.clientboundPlay()
        // Normal
        s2c.register(WorldBorderNoisePayload)
        // Unit
        s2c.register(CrashClientPayload)
    }
}