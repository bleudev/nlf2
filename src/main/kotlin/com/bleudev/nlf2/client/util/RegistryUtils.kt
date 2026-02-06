package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.getIdentifier
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent

fun registerSound(name: String): SoundEvent {
    val id = getIdentifier(name)
    return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id))
}