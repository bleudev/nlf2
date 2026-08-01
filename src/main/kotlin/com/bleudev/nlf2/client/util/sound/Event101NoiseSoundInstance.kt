package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.client.amethyst_daisy_amount
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.event101_noise_amount
import com.google.common.primitives.Floats.max

class Event101NoiseSoundInstance : TickableLoopedAmbientSoundInstance(Nlf2Sounds.PAUSE_CUTSCENE_AMBIENT) {
    override fun vol(): Float = max(event101_noise_amount * .3f, amethyst_daisy_amount * .25f)
    override fun pit(): Float = .5f
}