package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.sys.util.TextBoxSequence
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class OpenTextBox(val sequence: TextBoxSequence, val fadeOut: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<OpenTextBox> {
        override val identifier = Nlf2Packets.OPEN_TEXT_BOX
        override val codec: StreamCodec<RegistryFriendlyByteBuf, OpenTextBox> = StreamCodec.composite(
            TextBoxSequence.STREAM_CODEC, OpenTextBox::sequence,
            ByteBufCodecs.INT, OpenTextBox::fadeOut,
            ::OpenTextBox
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}