package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StrangeJukeboxMusicStop : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<StrangeJukeboxMusicStop> {
        override val identifier = Nlf2Packets.UNIT_STRANGE_JUKEBOX_MUSIC_STOP
        val INSTANCE = StrangeJukeboxMusicStop()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StrangeJukeboxMusicStop> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}