package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.util.registerSound

object Nlf2Sounds {
    val BROKE = registerSound("broke")
    val LAUGH = registerSound("laugh")
    val BORDER_AMBIENT = registerSound("border_ambient")
    val PAUSE_CUTSCENE_AMBIENT = registerSound("pause_cutscene_ambient")
    val MUS_STRANGE_NOISE = registerSound("mus_strange_noise")
    val MUS_MILKICH_APPEARS = registerSound("mus_milkich_appears")
    val MUS_JUKEBOX_PUZZLE = registerSound("mus_jukebox_puzzle")
    val MUS_JUKEBOX = registerSound("mus_jukebox")
    val MUS_EASTER_EGG = registerSound("mus_easter_egg")

    fun allJukeboxMusic() = listOf(
        MUS_STRANGE_NOISE, MUS_MILKICH_APPEARS, MUS_JUKEBOX_PUZZLE, MUS_JUKEBOX, MUS_EASTER_EGG
    )

    fun initialize() {}
}