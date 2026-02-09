package com.bleudev.nlf2.custom.packet.payload.interfaces

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

interface PacketPayloadCompanion<T : CustomPacketPayload> {
    val identifier: Identifier
    val codec: StreamCodec<RegistryFriendlyByteBuf, T>

    val type: CustomPacketPayload.Type<T>
        get() = CustomPacketPayload.Type(identifier)
}