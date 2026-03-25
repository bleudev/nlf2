package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft

abstract class BGMusic(val importance: Int) {
    abstract val volume: Float
    abstract val pitch: Float
    abstract val mus: String
    abstract fun canBePlaying(mc: Minecraft): Boolean
    override fun toString(): String = "bg {$volume, $pitch, $mus}"
}