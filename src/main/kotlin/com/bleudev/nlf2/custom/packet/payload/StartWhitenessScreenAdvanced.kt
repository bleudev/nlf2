package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StartWhitenessScreenAdvanced(val duration: Int, val fadeIn: Int, val fadeOut: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<StartWhitenessScreenAdvanced> {
        override val identifier = Nlf2Packets.START_WHITENESS_SCREEN_ADVANCED
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StartWhitenessScreenAdvanced> = StreamCodec.composite(
            ByteBufCodecs.INT, StartWhitenessScreenAdvanced::duration,
            ByteBufCodecs.INT, StartWhitenessScreenAdvanced::fadeIn,
            ByteBufCodecs.INT, StartWhitenessScreenAdvanced::fadeOut,
            ::StartWhitenessScreenAdvanced
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}