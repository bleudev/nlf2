package com.bleudev.nlf2.custom.packet.payload.serverbound.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StoryEnd : CustomPacketPayload {
    private constructor()
    companion object : PacketPayloadCompanion<StoryEnd> {
        override val identifier = Nlf2Packets.SB_UNIT_STORY_END
        val INSTANCE = StoryEnd()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StoryEnd> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}