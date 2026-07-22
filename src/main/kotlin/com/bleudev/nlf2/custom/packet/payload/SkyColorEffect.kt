package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class SkyColorEffect(val startDuration: Int, val endDuration: Int, val color: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<SkyColorEffect> {
        override val identifier = Nlf2Packets.SKY_COLOR_EFFECT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, SkyColorEffect> = StreamCodec.composite(
            ByteBufCodecs.INT, SkyColorEffect::startDuration,
            ByteBufCodecs.INT, SkyColorEffect::endDuration,
            ByteBufCodecs.INT, SkyColorEffect::color,
            ::SkyColorEffect
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}