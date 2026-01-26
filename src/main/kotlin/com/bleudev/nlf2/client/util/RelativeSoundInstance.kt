package com.bleudev.nlf2.client.util

import net.minecraft.client.resources.sounds.AbstractSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

class RelativeSoundInstance(soundEvent: SoundEvent, soundSource: SoundSource, randomSource: RandomSource) : AbstractSoundInstance(soundEvent, soundSource, randomSource) {
    init {
        this.relative = true
        this.x = .0
        this.y = .0
        this.z = .0
    }
    constructor(soundEvent: SoundEvent, soundSource: SoundSource) : this(soundEvent, soundSource, RandomSource.create())
    constructor(soundEvent: SoundEvent) : this(soundEvent, SoundSource.AMBIENT)
}