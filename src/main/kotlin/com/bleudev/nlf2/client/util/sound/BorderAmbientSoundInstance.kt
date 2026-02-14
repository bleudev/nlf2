package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.noise_amount

class BorderAmbientSoundInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.BORDER_AMBIENT) {
    override fun getNewVolume(): Float = noise_amount
}