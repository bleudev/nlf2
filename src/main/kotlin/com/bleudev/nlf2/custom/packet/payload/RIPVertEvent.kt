package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class RIPVertEvent(val shakeStartTick: Int, val lagStartTick: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<RIPVertEvent> {
        override val identifier = Nlf2Packets.RIP_VERT_EVENT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, RIPVertEvent> = StreamCodec.composite(
            ByteBufCodecs.INT, RIPVertEvent::shakeStartTick,
            ByteBufCodecs.INT, RIPVertEvent::lagStartTick,
            ::RIPVertEvent
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}