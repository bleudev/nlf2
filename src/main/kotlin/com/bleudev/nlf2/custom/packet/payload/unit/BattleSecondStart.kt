package com.bleudev.nlf2.custom.packet.payload.unit

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class BattleSecondStart : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<BattleSecondStart> {
        override val identifier = Nlf2Packets.UNIT_BATTLE_SECOND_START
        val INSTANCE = BattleSecondStart()
        override val codec: StreamCodec<RegistryFriendlyByteBuf, BattleSecondStart> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}