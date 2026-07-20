package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.EVENT101_NOISE_MAX_DISTANCE
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.event101_distance

class Event101NoiseSoundInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.PAUSE_CUTSCENE_AMBIENT) {
    override fun vol(): Float = (1f - (event101_distance.coerceAtMost(EVENT101_NOISE_MAX_DISTANCE.toFloat()) / EVENT101_NOISE_MAX_DISTANCE)) * .3f
    override fun pit(): Float = .5f
}