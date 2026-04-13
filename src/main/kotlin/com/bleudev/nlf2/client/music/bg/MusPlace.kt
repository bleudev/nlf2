package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft

class MusPlace(mc: Minecraft) : BGMusic("place.wav", 0, mc) {
    override val volume: Float
        get() = 1f
    override val shouldPlay: Boolean
        get() = mc.player != null
}