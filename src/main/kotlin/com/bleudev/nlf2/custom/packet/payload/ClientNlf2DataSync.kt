package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ClientNlf2DataSync(val actor: String, val actorChar: String) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<ClientNlf2DataSync> {
        override val identifier = Nlf2Packets.CLIENT_NLF2_DATA_SYNC
        override val codec: StreamCodec<RegistryFriendlyByteBuf, ClientNlf2DataSync> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ClientNlf2DataSync::actor,
            ByteBufCodecs.STRING_UTF8, ClientNlf2DataSync::actorChar,
            ::ClientNlf2DataSync
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}