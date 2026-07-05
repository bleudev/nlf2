package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StrangeJukeboxRainbowControl(val start: Boolean) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<StrangeJukeboxRainbowControl> {
        override val identifier = Nlf2Packets.STRANGE_JUKEBOX_RAINBOW_CONTROL
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StrangeJukeboxRainbowControl> = StreamCodec.composite(
            ByteBufCodecs.BOOL, StrangeJukeboxRainbowControl::start,
            ::StrangeJukeboxRainbowControl
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}