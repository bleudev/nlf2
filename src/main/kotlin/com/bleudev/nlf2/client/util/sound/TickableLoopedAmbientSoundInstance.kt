package com.bleudev.nlf2.client.util.sound

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

abstract class TickableLoopedAmbientSoundInstance(soundEvent: SoundEvent, soundSource: SoundSource = SoundSource.AMBIENT, randomSource: RandomSource = RandomSource.create()) : AbstractTickableSoundInstance(soundEvent, soundSource, randomSource) {
    init {
        this.x = .0
        this.y = .0
        this.z = .0
        this.relative = true
        this.delay = 0
        this.looping = true
        this.volume = 1f
        this.pitch = 1f
    }

    override fun tick() {
        this.volume = vol()
        this.pitch = pit()
    }

    open fun vol(): Float = 1f
    open fun pit(): Float = 1f
}