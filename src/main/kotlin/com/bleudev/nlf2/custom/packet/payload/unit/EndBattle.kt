package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class EndBattle : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<EndBattle> {
        override val identifier = Nlf2Packets.UNIT_END_BATTLE
        val INSTANCE = EndBattle()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, EndBattle> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}