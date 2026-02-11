package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CrashClientPayload : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<CrashClientPayload> {
        override val identifier = Nlf2Packets.UNIT_CRASH_CLIENT
        val INSTANCE = CrashClientPayload()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, CrashClientPayload> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}