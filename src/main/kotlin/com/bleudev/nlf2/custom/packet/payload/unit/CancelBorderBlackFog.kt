package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CancelBorderBlackFog : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<CancelBorderBlackFog> {
        override val identifier = Nlf2Packets.UNIT_CANCEL_BORDER_BLACK_FOG
        val INSTANCE = CancelBorderBlackFog()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, CancelBorderBlackFog> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}