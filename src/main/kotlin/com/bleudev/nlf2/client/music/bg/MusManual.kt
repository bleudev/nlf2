package com.bleudev.nlf2.client.music.bg

import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import net.minecraft.client.Minecraft

class MusManual : BGMusic(1) {
    override val volume: Float
        get() = 1f
    override val pitch: Float
        get() = 1f
    override val mus: String = "manual.wav"

    override fun canBePlaying(mc: Minecraft): Boolean = mc.screen is ManualScreen
}