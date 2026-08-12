package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class RIPSanyaEvent(val byeTick: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<RIPSanyaEvent> {
        override val identifier = Nlf2Packets.RIP_SANYA_EVENT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, RIPSanyaEvent> = StreamCodec.composite(
            ByteBufCodecs.INT, RIPSanyaEvent::byeTick,
            ::RIPSanyaEvent
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}