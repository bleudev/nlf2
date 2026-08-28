package com.bleudev.nlf2.client.util

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

fun <T : ParticleOptions> registerParticleProvider(particleType: ParticleType<T>, providerFactory: (SpriteSet) -> ParticleProvider<T>) {
    ParticleProviderRegistry.getInstance().register(particleType, providerFactory)
}