package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.MODID
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent

fun registerSound(name: String): SoundEvent {
    val id = Identifier.fromNamespaceAndPath(MODID, name)
    return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id))
}