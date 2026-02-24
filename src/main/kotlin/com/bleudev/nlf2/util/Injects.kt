package com.bleudev.nlf2.util

import com.bleudev.nine_lifes.client.Transformer
import com.bleudev.nine_lifes.custom.NineLifesDamageSources
import com.bleudev.nlf2.custom.data.saved.nlf2data
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
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.Level
import kotlin.reflect.KMutableProperty

fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) {
    for (payload in payloads) ServerPlayNetworking.send(this, payload)
}
fun ServerPlayer.hurtServer(source: (Level) -> DamageSource, damage: Float) = level().also {
    hurtServer(it, source(it), damage)
}
fun ServerPlayer.hurtUnknown(damage: Float) = hurtServer(NineLifesDamageSources::unknown, damage)

fun MinecraftServer.sendToEveryone(generator: (ServerPlayer) -> Array<CustomPacketPayload>) {
    for (player in this.playerList.players) player.sendPackets(*generator(player))
}

fun <T : CustomPacketPayload> PayloadTypeRegistry<RegistryFriendlyByteBuf>.register(packet: PacketPayloadCompanion<T>): CustomPacketPayload.TypeAndCodec<in RegistryFriendlyByteBuf, T> =
    register(packet.type, packet.codec)
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.requiresAdmin(): T =
    requires { it.permissions().hasPermission(Permissions.COMMANDS_ADMIN) }

// Data
val MinecraftServer.actorNickname: String
    get() = nlf2data.loreData.actor
val MinecraftServer.actor: ServerPlayer?
    get() = playerList.getPlayer(actorNickname)
val ServerPlayer.isActor: Boolean
    get() = gameProfile.name == level().server.actorNickname

// Lang
fun <T : Any> KMutableProperty<T>.transform(func: Transformer<T>) {
    val value = this.getter.call()
    this.setter.call(func(value))
}
