package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.client.noise_amount
import com.bleudev.nlf2.custom.Nlf2Sounds

class BorderAmbientSoundInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.BORDER_AMBIENT) {
    override fun vol(): Float = noise_amount
}