@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_COMPASS_CRAZY_DISTANCE
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.UNKNOWN_RAINBOW_NAME_TAG_DURATION
import com.bleudev.nlf2.client.*
import com.bleudev.nlf2.client.sys.anim.StoryAnimation
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.util.hueToTriple
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.ARGB
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

val isCompassCrazy: Boolean get() = noise_amount > .3f || dataSyncedCompassCrazy || event101_distance <= EVENT101_COMPASS_CRAZY_DISTANCE
val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return (!(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS)) && !isCompassCrazy
}
val canOpenChat: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.gameProfile.name.equals(dataSyncedActor, true)
}
fun renderHitboxes(entity: Entity): Boolean =
    !(entity is Player && !dataSyncedVisibleActor && dataSyncedActor.equals(entity.gameProfile.name, true))
fun renderPlayerInTab(name: String): Boolean =
    if (dataSyncedMeetState % 2 == 0) name.equals(Minecraft.getInstance().gameProfile.name, true)
    else !(!dataSyncedVisibleActor && name.equals(dataSyncedActor, true))
fun renderPlayerInVoiceChat(name: String): Boolean = renderPlayerInTab(name)
fun modifyPlayerNameTag(original: Component?): Component? {
    val name = original?.string ?: return original
    val t = TICKER_UNKNOWN_RAINBOW_NAME_TAG.tick()
    if (dataSyncedActor.equals(name, true) && dataSyncedActorLoreChar == "unknown") {
        val (r, g, b) = ((t % UNKNOWN_RAINBOW_NAME_TAG_DURATION).toFloat() / UNKNOWN_RAINBOW_NAME_TAG_DURATION).coerceIn(0f, 1f).hueToTriple()
        return Component.literal("???").withColor(ARGB.colorFromFloat(1f, r, g, b))
    }
    return original
}
fun renderPlayerNameTag(name: String): Boolean {
    return dataSyncedActor.equals(name, true) && dataSyncedActorLoreChar == "unknown"
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
