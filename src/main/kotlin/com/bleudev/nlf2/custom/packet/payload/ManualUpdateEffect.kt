package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ManualUpdateEffect(val owner: Boolean) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<ManualUpdateEffect> {
        override val identifier = Nlf2Packets.MANUAL_UPDATE_EFFECT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, ManualUpdateEffect> = StreamCodec.composite(
            ByteBufCodecs.BOOL, ManualUpdateEffect::owner,
            ::ManualUpdateEffect
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}