package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.client.dataSyncedActor
import com.bleudev.nlf2.client.dataSyncedCompassCrazy
import com.bleudev.nlf2.client.noise_amount
import net.minecraft.client.Minecraft
import net.minecraft.world.item.Items

val isCompassCrazy: Boolean get() = noise_amount > .6f || dataSyncedCompassCrazy

val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS) || !isCompassCrazy
}

val canOpenChat: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.gameProfile.name.equals(dataSyncedActor, true)
}
