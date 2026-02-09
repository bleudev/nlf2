package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val WORLD_BORDER_NOISE = getIdentifier("packet/world_border_noise")

    fun initialize() {
        PayloadTypeRegistry.clientboundPlay().register(WorldBorderNoisePayload)
    }
}