package com.bleudev.nlf2.custom

import com.bleudev.nlf2.util.registerParticleType
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes

object Nlf2ParticleTypes {
    val SLEEP = registerParticleType("sleep", FabricParticleTypes.simple())
    fun initialize() {}
}