@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_COMPASS_CRAZY_DISTANCE
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.UNKNOWN_GAME_PROFILE
import com.bleudev.nlf2.client.*
import com.bleudev.nlf2.client.sys.anim.StoryAnimation
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.sys.actor.ActorCharEntrypoint
import com.bleudev.nlf2.sys.actor.client.ActorCharEntrypointClient.current
import com.mojang.datafixers.util.Either
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.client.resources.sounds.Sound
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import java.util.*

val isCompassCrazy: Boolean get() = noise_amount > .3f || dataSyncedCompassCrazy || event101_distance <= EVENT101_COMPASS_CRAZY_DISTANCE
val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return (!(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS)) && !isCompassCrazy
}
val canOpenChat: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.gameProfile.name.equals(dataSyncedActor, true)
}
fun renderHitboxes(entity: Entity): Boolean {
    if (entity is Player) {
        val ch = current(entity.gameProfile.name)
        return ch?.renderHitboxes ?: true
    }
    return true
}
private fun renderPlayerInTab(name: String): Boolean {
    if (dataSyncedMeetState % 2 == 0) return name.equals(Minecraft.getInstance().gameProfile.name, true)
    if (name.equals(dataSyncedActor, true)) {
        return dataSyncedVisibleActor && current(name)?.isMask == true
    }
    return true
}
fun modifyPlayerTabEntries(original: List<PlayerInfo>): List<PlayerInfo> {
    val nw = ArrayList(original)
    nw.removeIf { playerInfo -> !renderPlayerInTab(playerInfo.profile.name) }
    if (dataSyncedActorLoreChar == "unknown") {
        nw.add(PlayerInfo(UNKNOWN_GAME_PROFILE, false))
    }
    return nw.toList()
}

fun renderPlayerInVoiceChat(name: String): Boolean = renderPlayerInTab(name)
fun modifyPlayerNameTag(original: Component?): Component? {
    val name = original?.string ?: return original
    if (dataSyncedActor.equals(name, true) && dataSyncedActorLoreChar == "unknown") {
        return Component.literal("???").withColor(unknownRainbowEffectColor)
    }
    return original
}
fun renderPlayerNameTag(name: String): Boolean {
    return dataSyncedActor.equals(name, true) && dataSyncedActorLoreChar == "unknown"
}
fun modifyChatMessage(message: Component): Component {
    val text = message.string
    if (text.startsWith("<???>")) {
        return message.copy().withColor(unknownRainbowEffectColor)
    }
    return message
}

fun modifyWaypointColor(id: Either<UUID, String>, original: Int): Int {
    val isActor = id.map(
        { it == dataSyncedActorId },
        { it.equals(dataSyncedActor, true) }
    )
    if (isActor) {
        val c = ActorCharEntrypoint.fetchChar(dataSyncedActorLoreChar)
        return c?.customWaypointColor ?: original
    }
    return original
}

// Event 101
val CURRENT_EVENT101_NICKNAME: String get() = if (DEV_EVENT_101) dataSyncedActor else EVENT101_NICKNAME
fun isEvent101Char(nickname: String): Boolean = nickname.equals(CURRENT_EVENT101_NICKNAME, true)
fun isEvent101Char(): Boolean = isEvent101Char(Minecraft.getInstance().gameProfile.name)

// Keyboard / mouse
private val event85var: Boolean get() =
    beforeMeetWaterFluidAmount < 1f || Minecraft.getInstance().player?.let { it.isDeadOrDying || !(it.gameMode()?.isSurvival ?: false) } ?: true
var canUseMouse: Boolean = true
    get() = listOf(
        event85var,
        !ripSanyaBlockKeys,
        field,
    ).all{it}
    set(v) {
        field = v
        KeyMapping.releaseAll()
    }
val canMoveMouseAnyway: Boolean get() = listOf(
    beforeMeetWaterFluidAmount >= 1f,
    ripSanyaBlockKeys,
).any{it}
var canUseKeyboard: Boolean = true
    get() = listOf(
        event85var,
        !ripSanyaBlockKeys,
        field,
    ).all{it}
    set(v) {
        field = v
        KeyMapping.releaseAll()
    }

private var prevCanUseKeyboard: Boolean? = null
private var prevCanUseMouse: Boolean? = null
fun keyTick() {
    // Cludge?
    if (prevCanUseKeyboard == null) prevCanUseKeyboard = canUseKeyboard
    if (prevCanUseMouse == null) prevCanUseMouse = canUseMouse
    if (prevCanUseKeyboard != canUseKeyboard || prevCanUseMouse != canUseMouse){
        KeyMapping.releaseAll()
        prevCanUseKeyboard = canUseKeyboard
        prevCanUseMouse = canUseMouse
    }
}

// Sounds
private val shouldModifyFinalVolume: Boolean get() = shouldRenderStory
private var prevShouldModifyFinalVolume: Boolean? = null
fun modifySoundSubtitle(sound: Sound, current: Component): Component = modifySoundSubtitle(sound.location, current)
fun modifySoundSubtitle(soundId: Identifier, current: Component): Component {
    if (soundId in Nlf2Sounds.scaryVoiceSounds().map { it.location }) {
        return current.copy().withColor(0xffff0000.toInt())
    }
    return current
}
fun getSoundFinalVolume(instance: SoundInstance, original: Float): Float {
    val identifier = instance.identifier
    if (shouldRenderStory) {
        if (identifier !in Nlf2Sounds.storySounds().map(SoundEvent::location)) {
            return original * StoryAnimation.otherVolume
        }
    }
    return original
}
fun volumeTick() {
    val var10001 = shouldModifyFinalVolume
    if (prevShouldModifyFinalVolume == null) {
        prevShouldModifyFinalVolume = var10001
    }
    if (var10001 || prevShouldModifyFinalVolume != false) {
        Minecraft.getInstance().soundManager.soundEngine.refreshAll()
    }
    prevShouldModifyFinalVolume = var10001
}
