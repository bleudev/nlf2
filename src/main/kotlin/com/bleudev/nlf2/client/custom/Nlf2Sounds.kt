package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.util.registerSound
import net.minecraft.sounds.SoundEvent

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
    val MUS_STORY = registerSound("mus_story")
    val MUS_MAN_STORY = registerSound("mus_man_story")
    val NOISE = registerSound("noise")
    val MUS_MAN_BEFORE = registerSound("mus_man_before")
    val MUS_MAN_WEIRD = registerSound("mus_man_weird")
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
    fun allWithSubtitle(): List<SoundEvent> = listOf(
        BROKE,
        LAUGH,
        BORDER_AMBIENT,
        PAUSE_CUTSCENE_AMBIENT,
        MUS_STRANGE_NOISE,
        MUS_MILKICH_APPEARS,
        MUS_JUKEBOX,
        MUS_EASTER_EGG,
        MUS_STORY,
        MUS_MAN_BEFORE,
        MUS_MAN_WEIRD,
        MUS_QUAKE,
    )
    fun allWithoutSubtitle(): List<SoundEvent> = listOf(
        MUS_JUKEBOX_PUZZLE,
        MUS_MAN_STORY,
        NOISE,
    )

    fun initialize() {}
}