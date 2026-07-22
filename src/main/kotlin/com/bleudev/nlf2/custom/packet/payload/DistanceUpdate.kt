package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DistanceUpdate(val borderDistance: Double, val event101Distance: Float) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<DistanceUpdate> {
        override val identifier = Nlf2Packets.DISTANCE_UPDATE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, DistanceUpdate> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, DistanceUpdate::borderDistance,
            ByteBufCodecs.FLOAT, DistanceUpdate::event101Distance,
            ::DistanceUpdate
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}