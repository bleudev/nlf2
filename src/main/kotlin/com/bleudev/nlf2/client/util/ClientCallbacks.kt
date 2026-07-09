package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.client.dataSyncedActor
import net.minecraft.client.Minecraft
import net.minecraft.world.item.Items

val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS)
}

val canOpenChat: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.gameProfile.name.equals(dataSyncedActor, true)
}
