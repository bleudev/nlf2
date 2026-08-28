package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.client.custom.particle.SleepParticle
import com.bleudev.nlf2.client.util.registerParticleProvider
import com.bleudev.nlf2.custom.Nlf2ParticleTypes

object Nlf2ParticleProviders {
    val SLEEP = registerParticleProvider(Nlf2ParticleTypes.SLEEP, SleepParticle::Provider)
    fun initialize() {}
}