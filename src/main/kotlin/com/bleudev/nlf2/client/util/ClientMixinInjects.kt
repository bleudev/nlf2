package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.mixin.accessor.SoundManagerAccessor
import net.minecraft.client.sounds.SoundEngine
import net.minecraft.client.sounds.SoundManager

val SoundManager.soundEngine: SoundEngine get() = (this as SoundManagerAccessor).`nlf2$soundEngine`()