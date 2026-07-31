package com.bleudev.nlf2.util

import com.bleudev.nine_lifes.api.Transformer
import com.bleudev.nine_lifes.custom.NineLifesDamageTypes
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.mojang.brigadier.builder.ArgumentBuilder
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import net.minecraft.util.ARGB
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.reflect.KMutableProperty

fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) {
    for (payload in payloads) ServerPlayNetworking.send(this, payload)
}
fun ServerPlayer.hurtServer(source: (Level) -> DamageSource, damage: Float) = level().also {
    hurtServer(it, source(it), damage)
}
fun ServerPlayer.hurtUnknown(damage: Float) = hurtServer(NineLifesDamageTypes::unknown, damage)

fun Level.explode(pos: Vec3, strength: Float, damageSourceSupplier: (Level) -> DamageSource, explosionInteraction: Level.ExplosionInteraction, source: Entity? = null, fire: Boolean = true) = explode(
    source, damageSourceSupplier(this), ExplosionDamageCalculator(),
    pos.x, pos.y, pos.z, strength, fire, explosionInteraction
)

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
    get() = gameProfile.name.equals(level().server.actorNickname, true)

// Color
fun Int.normalize(rgbTarget: Int = 0) =
    if (ARGB.alphaFloat(this) >= 1f) this else ARGB.srgbLerp(ARGB.alphaFloat(this), ARGB.color(1f, rgbTarget), this)

// Other
fun Vec3.toBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())

// Lang
fun <T : Any> KMutableProperty<T>.transform(func: Transformer<T>) {
    val value = this.getter.call()
    this.setter.call(func(value))
}
fun <T> List<T>.with(vararg values: T): List<T> {
    val cp = this.toMutableList()
    cp.addAll(values)
    return cp.toList()
}
fun Float.reverseDelta(max: Float = 1f): Float = max - this
fun Float.reverseDeltaIf(condition: Boolean, max: Float = 1f): Float = if (condition) reverseDelta(max) else this
fun Float.onlyIf(condition: Boolean, default: Float = 0f): Float = if (condition) this else default
