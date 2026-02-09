package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class WorldBorderNoisePayload(val distance: Double) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<WorldBorderNoisePayload> {
        override val identifier = Nlf2Packets.WORLD_BORDER_NOISE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, WorldBorderNoisePayload> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, WorldBorderNoisePayload::distance,
            ::WorldBorderNoisePayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}