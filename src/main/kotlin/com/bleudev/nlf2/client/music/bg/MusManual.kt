package com.bleudev.nlf2.client.music.bg

import com.bleudev.nlf2.client.custom.gui.screens.ManualScreen
import net.minecraft.client.Minecraft

class MusManual(mc: Minecraft) : BGMusic("manual.wav", 2, mc) {
    override val volume: Float
        get() = 1f
    override val shouldPlay: Boolean
        get() = mc.gui.screen() is ManualScreen
}