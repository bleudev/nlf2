package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class Nlf2BlockLootTableProvider(
    dataOutput: FabricPackOutput, registryLookup: CompletableFuture<HolderLookup.Provider>
) : FabricBlockLootSubProvider(dataOutput, registryLookup) {
    override fun generate() {
        dropSelf(Nlf2Blocks.STRANGE_JUKEBOX)
        dropSelf(Nlf2Blocks.AMETHYST_OXEYE_DAISY)
    }
}