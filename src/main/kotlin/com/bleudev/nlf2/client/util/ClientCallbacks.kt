package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.DEV_EVENT_101
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.client.*
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items

val isCompassCrazy: Boolean get() = noise_amount > .3f || dataSyncedCompassCrazy

val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS) || !isCompassCrazy
}

val canOpenChat: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.gameProfile.name.equals(dataSyncedActor, true)
}

fun renderHitboxes(entity: Entity): Boolean =
    !(entity is Player && dataSyncedActor.equals(entity.gameProfile.name, true))

fun isEvent101Char(nickname: String, strict: Boolean = true): Boolean {
    val nickname = nickname.lowercase()
    val actor = dataSyncedActor.lowercase()
    if (!dataSyncedEvent101 || (!strict && event101screenEffectClearTicks > 0)) {
        @Suppress("SimplifyBooleanWithConstants")
        if ((DEV_EVENT_101 && nickname == actor) || (!DEV_EVENT_101 && nickname == EVENT101_NICKNAME)) {
            return true
        }
    }
    return false
}
fun isEvent101Char(strict: Boolean = true): Boolean = isEvent101Char(Minecraft.getInstance().gameProfile.name, strict)
