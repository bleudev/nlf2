package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.util.registerSound

object Nlf2Sounds {
    @JvmField
    val BROKE = registerSound("broke")
    @JvmField
    val LAUGH = registerSound("laugh")
    @JvmField
    val BORDER_AMBIENT = registerSound("border_ambient")
    @JvmField
    val PAUSE_CUTSCENE_AMBIENT = registerSound("pause_cutscene_ambient")
    @JvmField
    val MUS_STRANGE_NOISE = registerSound("mus_strange_noise")
    @JvmField
    val MUS_MILKICH_APPEARS = registerSound("mus_milkich_appears")
    @JvmField
    val MUS_JUKEBOX_PUZZLE = registerSound("mus_jukebox_puzzle")
    @JvmField
    val MUS_JUKEBOX = registerSound("mus_jukebox")
    @JvmField
    val MUS_EASTER_EGG = registerSound("mus_easter_egg")
    @JvmField
    val MUS_STORY = registerSound("mus_story")
    @JvmField
    val MUS_MAN_STORY = registerSound("mus_man_story")
    @JvmField
    val NOISE = registerSound("noise")
    @JvmField
    val MUS_MAN_BEFORE = registerSound("mus_man_before")
    @JvmField
    val MUS_MAN_WEIRD = registerSound("mus_man_weird")
    @JvmField
    val MUS_QUAKE = registerSound("mus_quake")

    fun allJukeboxMusic() = listOf(
        MUS_STRANGE_NOISE, MUS_MILKICH_APPEARS, MUS_JUKEBOX_PUZZLE, MUS_JUKEBOX, MUS_EASTER_EGG
    )
    @JvmStatic
    fun storySounds() = listOf(
        MUS_STORY, MUS_MAN_STORY, NOISE
    )
    @JvmStatic
    fun ripVertSounds() = listOf(
        MUS_MAN_BEFORE
    )

    fun initialize() {}
}