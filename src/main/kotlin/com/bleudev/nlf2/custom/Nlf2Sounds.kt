package com.bleudev.nlf2.custom

import com.bleudev.nlf2.util.registerSound
import com.bleudev.nlf2.util.with
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
    val MUS_MAN_WEIRD = registerSound("mus_man_weird")
    val MUS_QUAKE = registerSound("mus_quake")
    val MUS_PART4_BYE_VERT = registerSound("mus_part4_bye_vert")
    val MUS_PART4_BYE = registerSound("mus_part4_bye")
    val AUDIO_HOLY_SHEEP = registerSound("audio_holy_sheep")
    val MUS_PART4_UNKNOWN_APPEARS = registerSound("mus_part4_unknown_appears")
    val MUS_JUKEBOX_SLEEP = registerSound("mus_jukebox_sleep")
    val MUS_PART5_BATTLE = registerSound("mus_part5_battle")
    val SCARY_VOICE_GIVE_IT = registerSound("scary_voice_give_it")
    val SCARY_VOICE_GIVE_UP = registerSound("scary_voice_give_up")
    val SCARY_VOICE_MY_WORLD = registerSound("scary_voice_my_world")
    val SCARY_VOICE_WHY_YOU = registerSound("scary_voice_why_you")
    val MUS_MANUAL = registerSound("mus_manual")

    fun allJukeboxMusic() = listOf(
        MUS_STRANGE_NOISE,
        MUS_MILKICH_APPEARS,
        MUS_JUKEBOX_PUZZLE,
        MUS_JUKEBOX,
        MUS_EASTER_EGG,
        MUS_JUKEBOX_SLEEP,
    )
    fun storySounds() = listOf(
        MUS_STORY, MUS_MAN_STORY, NOISE
    )
    fun unknownMeetSounds() = listOf(AUDIO_HOLY_SHEEP, MUS_PART4_UNKNOWN_APPEARS)
    fun scaryVoiceSounds() = listOf(
        SCARY_VOICE_GIVE_IT,
        SCARY_VOICE_GIVE_UP,
        SCARY_VOICE_MY_WORLD,
        SCARY_VOICE_WHY_YOU,
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
        MUS_MAN_WEIRD,
        MUS_QUAKE,
        MUS_PART4_BYE_VERT,
        MUS_PART4_BYE,
        MUS_PART4_UNKNOWN_APPEARS,
        MUS_JUKEBOX_SLEEP,
        MUS_PART5_BATTLE,
        MUS_MANUAL,
    ).with(*scaryVoiceSounds().toTypedArray())
    fun allWithoutSubtitle(): List<SoundEvent> = listOf(
        MUS_JUKEBOX_PUZZLE,
        MUS_MAN_STORY,
        NOISE,
        AUDIO_HOLY_SHEEP
    )

    fun initialize() {}
}