package com.bleudev.nlf2.datagen

import com.bleudev.nlf2.datagen.provider.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class Nlf2DataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()
        // Translations
        pack.addProvider(::EnUsTranslationProvider)
        pack.addProvider(::RuRuTranslationProvider)
        // Tags
        pack.addProvider(::Nlf2BlockTagsProvider)
        // Loot tables
        pack.addProvider(::Nlf2BlockLootTableProvider)
        // Other
        pack.addProvider(::Nlf2ModelProvider)
        pack.addProvider(::Nlf2RecipesProvider)
        pack.addProvider(::Nlf2SoundsProvider)
        generator.run()
    }
}