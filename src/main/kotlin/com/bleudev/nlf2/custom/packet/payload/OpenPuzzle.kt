package com.bleudev.nlf2.custom.packet.payload

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.util.StringRepresentable

class OpenPuzzle(val puzzle: String) : CustomPacketPayload {
    constructor(puzzle: PacketPuzzle) : this(puzzle.serializedName)

    companion object : PacketPayloadCompanion<OpenPuzzle> {
        override val identifier = Nlf2Packets.OPEN_PUZZLE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, OpenPuzzle> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, OpenPuzzle::puzzle,
            ::OpenPuzzle
        )
    }
    val packetPuzzle: PacketPuzzle?
        get() = PacketPuzzle.fromString(puzzle)

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}

enum class PacketPuzzle(private val x: String): StringRepresentable {
    WD("wd");

    override fun getSerializedName(): String = x

    companion object {
        fun fromString(x: String): PacketPuzzle? = when(x) {
            "wd" -> WD
            else -> null
        }
    }
}