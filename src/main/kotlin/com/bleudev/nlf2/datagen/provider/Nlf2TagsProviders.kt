package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.util.resourceKey
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import java.util.concurrent.CompletableFuture

class Nlf2BlockTagsProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagsProvider.BlockTagsProvider(output, registriesFuture) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(Registries.BLOCK.resourceKey("strange_jukebox"))
    }
}