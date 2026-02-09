package com.bleudev.nlf2.client.util

import net.minecraft.client.resources.sounds.AbstractSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

class RelativeSoundInstance(soundEvent: SoundEvent, soundSource: SoundSource = SoundSource.AMBIENT, randomSource: RandomSource = RandomSource.create()) : AbstractSoundInstance(soundEvent, soundSource, randomSource) {
    init {
        this.relative = true
        this.x = .0
        this.y = .0
        this.z = .0
    }
}