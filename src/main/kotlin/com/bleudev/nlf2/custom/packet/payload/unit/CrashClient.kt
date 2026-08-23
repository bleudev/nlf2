package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CrashClient : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<CrashClient> {
        override val identifier = Nlf2Packets.UNIT_CRASH_CLIENT
        val INSTANCE = CrashClient()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, CrashClient> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}