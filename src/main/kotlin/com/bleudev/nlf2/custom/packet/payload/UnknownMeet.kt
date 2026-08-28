package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.intellij.lang.annotations.MagicConstant

class UnknownMeet(@MagicConstant(valuesFromClass = State::class) val state: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<UnknownMeet> {
        override val identifier = Nlf2Packets.UNKNOWN_MEET
        override val codec: StreamCodec<RegistryFriendlyByteBuf, UnknownMeet> = StreamCodec.composite(
            ByteBufCodecs.INT, UnknownMeet::state,
            ::UnknownMeet
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type

    object State {
        const val BEFORE_MEET = 0
        const val AFTER_MEET = 1
        const val AFTER_ERASE = 2
    }
}