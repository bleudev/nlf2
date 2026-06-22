package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft

class MusBattle(mc: Minecraft) : BGMusic("battle.wav", 1, mc) {
    override val volume: Float
        get() = 1f
    override val pitch: Float
        get() = .99f
    override val shouldPlay: Boolean
        get() = false
}