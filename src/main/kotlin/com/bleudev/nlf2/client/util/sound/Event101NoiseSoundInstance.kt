package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.event101_noise_amount

class Event101NoiseSoundInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.PAUSE_CUTSCENE_AMBIENT) {
    override fun vol(): Float = event101_noise_amount * .3f
    override fun pit(): Float = .5f
}