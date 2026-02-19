package com.bleudev.nlf2.custom.packet.payload.serverbound

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class JoinSendVersion(val version: String) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<JoinSendVersion> {
        override val identifier = Nlf2Packets.SB_JOIN_SEND_VERSION
        override val codec: StreamCodec<RegistryFriendlyByteBuf, JoinSendVersion> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, JoinSendVersion::version,
            ::JoinSendVersion
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}