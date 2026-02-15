package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class OpenManual(val manualId: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<OpenManual> {
        override val identifier = Nlf2Packets.OPEN_MANUAL
        override val codec: StreamCodec<RegistryFriendlyByteBuf, OpenManual> = StreamCodec.composite(
            ByteBufCodecs.INT, OpenManual::manualId,
            ::OpenManual
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}