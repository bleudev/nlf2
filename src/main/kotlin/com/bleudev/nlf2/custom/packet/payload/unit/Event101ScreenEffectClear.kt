package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class Event101ScreenEffectClear : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<Event101ScreenEffectClear> {
        override val identifier = Nlf2Packets.UNIT_EVENT101_SCREEN_EFFECT_CLEAR
        val INSTANCE = Event101ScreenEffectClear()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, Event101ScreenEffectClear> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}