package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class BeforeMeetWaterFluid(val isIn: Boolean) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<BeforeMeetWaterFluid> {
        override val identifier = Nlf2Packets.BEFORE_MEET_WATER_FLUID
        override val codec: StreamCodec<RegistryFriendlyByteBuf, BeforeMeetWaterFluid> = StreamCodec.composite(
            ByteBufCodecs.BOOL, BeforeMeetWaterFluid::isIn,
            ::BeforeMeetWaterFluid
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}