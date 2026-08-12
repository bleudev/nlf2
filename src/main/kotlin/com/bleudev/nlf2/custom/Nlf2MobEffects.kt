package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.effect.GlidingEffect
import com.bleudev.nlf2.util.registerMobEffect

object Nlf2MobEffects {
    @JvmField
    val GLIDING = registerMobEffect("gliding", GlidingEffect())

    fun initialize() {}
}