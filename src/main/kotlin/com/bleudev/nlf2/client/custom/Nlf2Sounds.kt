package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.util.registerSound

object Nlf2Sounds {
    val BROKE = registerSound("broke")
    val LAUGH = registerSound("laugh")
    val BORDER_AMBIENT = registerSound("border_ambient")
    val PAUSE_CUTSCENE_AMBIENT = registerSound("pause_cutscene_ambient")
    val MUS_JUKEBOX = registerSound("mus_jukebox")
    val MUS_EASTER_EGG = registerSound("mus_easter_egg")

    fun initialize() {}
}