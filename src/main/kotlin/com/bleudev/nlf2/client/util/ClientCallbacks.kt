package com.bleudev.nlf2.client.util

import net.minecraft.client.Minecraft
import net.minecraft.world.item.Items

val isHoldingCompass: Boolean get() {
    val pl = Minecraft.getInstance().player ?: return false
    return !(pl.gameMode()?.isSurvival ?: false) || pl.isHolding(Items.COMPASS)
}
