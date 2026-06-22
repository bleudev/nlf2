package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource

abstract class BGMusic(val mus: String, val importance: Int, protected val mc: Minecraft): Comparable<BGMusic> {
    open val dependOnMusicVolume: Boolean = true
    abstract val volume: Float
    internal val finalVolume: Float = volume *
        (if (dependOnMusicVolume) mc.options.getFinalSoundSourceVolume(SoundSource.MUSIC) else 1f)
    abstract val pitch: Float
    abstract val shouldPlay: Boolean

    override fun toString(): String = "bg {$mus, $finalVolume, $pitch}"
    override operator fun compareTo(other: BGMusic): Int = importance.compareTo(other.importance)
}