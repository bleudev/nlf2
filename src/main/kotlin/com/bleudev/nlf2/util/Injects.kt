package com.bleudev.nlf2.util

import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.mojang.brigadier.builder.ArgumentBuilder
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions

fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) {
    for (payload in payloads) ServerPlayNetworking.send(this, payload)
}

fun MinecraftServer.sendToEveryone(generator: (ServerPlayer) -> Array<CustomPacketPayload>) {
    for (player in this.playerList.players) player.sendPackets(*generator(player))
}

fun <T : CustomPacketPayload> PayloadTypeRegistry<RegistryFriendlyByteBuf>.register(packet: PacketPayloadCompanion<T>) =
    register(packet.type, packet.codec)
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.requiresAdmin(): T =
    requires { it.permissions().hasPermission(Permissions.COMMANDS_ADMIN) }
