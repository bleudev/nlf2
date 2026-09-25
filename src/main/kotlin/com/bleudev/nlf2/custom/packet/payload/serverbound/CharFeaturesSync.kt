package com.bleudev.nlf2.custom.packet.payload.serverbound

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CharFeaturesSync(val isForceVisible: Boolean) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<CharFeaturesSync> {
        override val identifier = Nlf2Packets.SB_CHAR_FEATURES_SYNC
        override val codec: StreamCodec<RegistryFriendlyByteBuf, CharFeaturesSync> = StreamCodec.composite(
            ByteBufCodecs.BOOL, CharFeaturesSync::isForceVisible,
            ::CharFeaturesSync
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}