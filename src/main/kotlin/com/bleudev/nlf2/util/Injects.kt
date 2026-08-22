package com.bleudev.nlf2.util

import com.bleudev.nine_lifes.api.Transformer
import com.bleudev.nine_lifes.custom.NineLifesDamageTypeTags
import com.bleudev.nine_lifes.custom.NineLifesDamageTypes
import com.bleudev.nlf2.DEV_STORY_ACTOR
import com.bleudev.nlf2.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import com.mojang.brigadier.builder.ArgumentBuilder
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import net.minecraft.util.ARGB
import net.minecraft.world.Container
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.item.component.DamageResistant
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.reflect.KMutableProperty

fun Container.advancedClear(vararg prohibitedItems: Item) {
    for (i in 0..<this.containerSize) {
        val slotAccess = getSlot(i) ?: continue
        val itemStack = slotAccess.get()
        var bl = false
        if (prohibitedItems.any { itemStack.`is`(it) }) {
            itemStack.count = 0
        }
        // Bundles
        if (itemStack.has(DataComponents.BUNDLE_CONTENTS)) {
            val bundleContents = itemStack.get(DataComponents.BUNDLE_CONTENTS)!!
            val new = BundleContents(bundleContents.items().toList().filter { itemStackTemplate ->
                !prohibitedItems.any { itemStackTemplate.`is`(it) }
            })
            itemStack.set(DataComponents.BUNDLE_CONTENTS, new)
            bl = true
        }
        // Containers
        if (itemStack.has(DataComponents.CONTAINER)) {
            val container = itemStack.get(DataComponents.CONTAINER)!!
            val new = ItemContainerContents.fromItems(container.allItemsCopyStream()
                .filter {vanya ->
                    !prohibitedItems.any { vanya.`is`(it) }
                }.toList()
            )
            itemStack.set(DataComponents.CONTAINER, new)
            bl = true
        }
        if (bl) slotAccess.set(itemStack)
    }
}
fun Container.advancedClear(vararg prohibitedItems: ItemLike) = advancedClear(*prohibitedItems.map { it.asItem() }.toTypedArray())

fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) {
    for (payload in payloads) ServerPlayNetworking.send(this, payload)
}
fun ServerPlayer.hurtServer(source: (Level) -> DamageSource, damage: Float) = level().also {
    hurtServer(it, source(it), damage)
}
fun ServerPlayer.hurtUnknown(damage: Float) = hurtServer(NineLifesDamageTypes::unknown, damage)
fun ServerPlayer.advancedClear(vararg prohibitedItems: ItemLike) {
    inventory.advancedClear(*prohibitedItems)
    enderChestInventory.advancedClear(*prohibitedItems)
}

fun Level.explode(pos: Vec3, strength: Float, damageSourceSupplier: (Level) -> DamageSource, explosionInteraction: Level.ExplosionInteraction, source: Entity? = null, fire: Boolean = true) = explode(
    source, damageSourceSupplier(this), ExplosionDamageCalculator(),
    pos.x, pos.y, pos.z, strength, fire, explosionInteraction
)

fun MinecraftServer.sendToEveryone(generator: (ServerPlayer) -> Array<CustomPacketPayload>) {
    for (player in this.playerList.players) player.sendPackets(*generator(player))
}
fun MinecraftServer.sendToEveryone(vararg packets: CustomPacketPayload) = sendToEveryone { arrayOf(*packets) } // Cludge

fun Item.Properties.lightningAndFireResistant(): Item.Properties = delayedComponent(DataComponents.DAMAGE_RESISTANT) {
    DamageResistant(it.getOrThrow(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE))
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
    get() = level().server.let { gameProfile.name.equals(it.actorNickname, true) && !it.nlf2data.loreData.visibleActor }
@Suppress("SimplifyBooleanWithConstants")
val ServerPlayer.wantsToListenStory: Boolean
    get() = DEV_STORY_ACTOR || !isActor

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
fun Float.hueToTriple(): Triple<Float, Float, Float> {
    var h = this % 1f
    if (h < 0) h += 1f

    val rgb = FloatArray(3)
    val offsets = floatArrayOf(0f, 4f, 2f)

    for (i in offsets.indices) {
        val value = (h * 6f + offsets[i]) % 6f
        val result = kotlin.math.abs(value - 3f) - 1f
        rgb[i] = result.coerceIn(0f, 1f)
    }

    return Triple(rgb[0], rgb[1], rgb[2])
}
