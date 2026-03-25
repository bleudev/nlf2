package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft

class MusPlace : BGMusic(0) {
    override val volume: Float
        get() = 1f
    override val mus: String = "place.wav"

    override fun canBePlaying(mc: Minecraft): Boolean = true
}