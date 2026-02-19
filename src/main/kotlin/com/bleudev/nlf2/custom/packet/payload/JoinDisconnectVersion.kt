package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class JoinDisconnectVersion(val client: String, val server: String) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<JoinDisconnectVersion> {
        override val identifier = Nlf2Packets.JOIN_DISCONNECT_VERSION
        override val codec: StreamCodec<RegistryFriendlyByteBuf, JoinDisconnectVersion> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, JoinDisconnectVersion::client,
            ByteBufCodecs.STRING_UTF8, JoinDisconnectVersion::server,
            ::JoinDisconnectVersion
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}