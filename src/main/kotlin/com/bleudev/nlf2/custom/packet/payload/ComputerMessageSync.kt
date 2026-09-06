package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.util.list
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ComputerMessageSync(val pos: BlockPos, val lines: List<Component>) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<ComputerMessageSync> {
        override val identifier = Nlf2Packets.COMPUTER_MESSAGE_SYNC
        override val codec: StreamCodec<RegistryFriendlyByteBuf, ComputerMessageSync> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ComputerMessageSync::pos,
            ComponentSerialization.STREAM_CODEC.list(), ComputerMessageSync::lines,
            ::ComputerMessageSync
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}