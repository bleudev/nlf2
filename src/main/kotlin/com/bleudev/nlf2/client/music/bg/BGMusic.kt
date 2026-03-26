package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft

abstract class BGMusic(val mus: String, val importance: Int, protected val mc: Minecraft): Comparable<BGMusic> {
    abstract val volume: Float
    abstract val shouldPlay: Boolean
    override fun toString(): String = "bg {$mus, $volume}"
    override operator fun compareTo(other: BGMusic): Int = importance.compareTo(other.importance)
}