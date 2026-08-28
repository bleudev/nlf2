package com.bleudev.nlf2.client.custom.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource

class SleepParticle(level: ClientLevel, x: Double, y: Double, z: Double, sprite: TextureAtlasSprite) : SingleQuadParticle(level, x, y, z, .0, .0, .0, sprite) {
    init {
        this.friction = 0.66f
        this.speedUpWhenYMotionIsBlocked = true
        this.xd *= 0.01
        this.yd *= 0.01
        this.zd *= 0.01
        this.yd += 0.2
        this.quadSize *= 1.5f
        this.lifetime = 6
    }
    override fun getLayer(): Layer = Layer.OPAQUE

    override fun getQuadSize(a: Float): Float {
        return this.quadSize * Mth.clamp((this.age.toFloat() + a) / this.lifetime.toFloat() * 32.0f, 0.0f, 1.0f)
    }

    class Provider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            options: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xAux: Double,
            yAux: Double,
            zAux: Double,
            random: RandomSource
        ): Particle = SleepParticle(level, x, y, z, this.sprite.get(random))
    }
}