@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_COMPASS_CRAZY_DISTANCE
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.client.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
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

val CURRENT_EVENT101_NICKNAME: String get() = if (DEV_EVENT_101) dataSyncedActor else EVENT101_NICKNAME
fun isEvent101Char(nickname: String): Boolean = nickname.equals(CURRENT_EVENT101_NICKNAME, true)
fun isEvent101Char(): Boolean = isEvent101Char(Minecraft.getInstance().gameProfile.name)

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

