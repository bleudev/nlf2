package com.bleudev.nlf2.custom.packet.payload.serverbound

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CauseEventWithKey(val eventId: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<CauseEventWithKey> {
        override val identifier = Nlf2Packets.SB_CAUSE_EVENT_WITH_KEY
        override val codec: StreamCodec<RegistryFriendlyByteBuf, CauseEventWithKey> = StreamCodec.composite(
            ByteBufCodecs.INT, CauseEventWithKey::eventId,
            ::CauseEventWithKey
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}