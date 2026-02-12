package com.bleudev.nlf2.util

import com.bleudev.nlf2.getIdentifier
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import java.util.*


private fun <T : Any> ResourceKey<Registry<T>>.resourceKey(name: String) =
    ResourceKey.create(this, getIdentifier(name))
private fun <V : Any, T : V> Registry<V>.register(name: String, element: T) =
    Registry.register<V, T>(this, getIdentifier(name), element)

fun registerCreativeModeTab(name: String, builderTransformer: CreativeModeTab.Builder.() -> CreativeModeTab.Builder): CreativeModeTab =
    BuiltInRegistries.CREATIVE_MODE_TAB.register(name, builderTransformer(FabricCreativeModeTab.builder()).build())
fun <T : Item> registerItem(name: String, itemFactory: (Item.Properties) -> T, properties: Item.Properties = Item.Properties()): T = BuiltInRegistries.ITEM
    .register(name, itemFactory(properties.setId(Registries.ITEM.resourceKey(name))))

// Client
fun registerSound(name: String, range: Float? = null): SoundEvent = BuiltInRegistries.SOUND_EVENT
    .register(name, SoundEvent(getIdentifier(name), Optional.ofNullable(range)))
