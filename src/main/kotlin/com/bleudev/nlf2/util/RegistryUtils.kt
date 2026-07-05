package com.bleudev.nlf2.util

import com.bleudev.nlf2.getIdentifier
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.*


fun <T : Any> ResourceKey<Registry<T>>.resourceKey(name: String) =
    ResourceKey.create(this, getIdentifier(name))
fun <V : Any, T : V> Registry<V>.register(name: String, element: T) =
    Registry.register(this, getIdentifier(name), element)

fun registerCreativeModeTab(name: String, builderTransformer: CreativeModeTab.Builder.() -> CreativeModeTab.Builder): CreativeModeTab =
    BuiltInRegistries.CREATIVE_MODE_TAB.register(name, builderTransformer(FabricCreativeModeTab.builder()).build())
fun <T : Item> registerItem(name: String, itemFactory: (Item.Properties) -> T, properties: Item.Properties = Item.Properties()): T = BuiltInRegistries.ITEM
    .register(name, itemFactory(properties.setId(Registries.ITEM.resourceKey(name))))
fun registerBlock(
    name: String,
    blockFactory: (BlockBehaviour.Properties) -> Block,
    properties: BlockBehaviour.Properties,
    shouldRegisterItem: Boolean
): Block {
    val blockKey = Registries.BLOCK.resourceKey(name)
    val block = blockFactory(properties.setId(blockKey))
    if (shouldRegisterItem) {
        val itemKey = Registries.ITEM.resourceKey(name)
        val blockItem = BlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
        BuiltInRegistries.ITEM.register(name, blockItem)
    }
    return BuiltInRegistries.BLOCK.register(name, block)
}
fun <T : BlockEntity> registerBlockEntity(
    name: String, entityFactory: FabricBlockEntityTypeBuilder.Factory<out T>, vararg blocks: Block
): BlockEntityType<T> = Registry.register<BlockEntityType<*>, BlockEntityType<T>>(
    BuiltInRegistries.BLOCK_ENTITY_TYPE, getIdentifier(name),
    FabricBlockEntityTypeBuilder.create(entityFactory, *blocks).build()
)
// Client
fun registerSound(name: String, range: Float? = null): SoundEvent = BuiltInRegistries.SOUND_EVENT
    .register(name, SoundEvent(getIdentifier(name), Optional.ofNullable(range)))
