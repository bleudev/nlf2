package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StartBattle : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<StartBattle> {
        override val identifier = Nlf2Packets.UNIT_START_BATTLE
        val INSTANCE = StartBattle()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StartBattle> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}