package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class OpenTextBox(val text: String, val color: Int, val speed: Int, val wait: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<OpenTextBox> {
        override val identifier = Nlf2Packets.OPEN_TEXT_BOX
        override val codec: StreamCodec<RegistryFriendlyByteBuf, OpenTextBox> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, OpenTextBox::text,
            ByteBufCodecs.INT, OpenTextBox::color,
            ByteBufCodecs.INT, OpenTextBox::speed,
            ByteBufCodecs.INT, OpenTextBox::wait,
            ::OpenTextBox
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}