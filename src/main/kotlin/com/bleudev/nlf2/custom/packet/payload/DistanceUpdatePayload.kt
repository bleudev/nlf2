package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DistanceUpdatePayload(val borderDistance: Double, val event101Distance: Float) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<DistanceUpdatePayload> {
        override val identifier = Nlf2Packets.DISTANCE_UPDATE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, DistanceUpdatePayload> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, DistanceUpdatePayload::borderDistance,
            ByteBufCodecs.FLOAT, DistanceUpdatePayload::event101Distance,
            ::DistanceUpdatePayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}