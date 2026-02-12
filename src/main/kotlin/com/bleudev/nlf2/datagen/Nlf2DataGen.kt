package com.bleudev.nlf2.datagen

import com.bleudev.nlf2.datagen.provider.EnUsTranslationProvider
import com.bleudev.nlf2.datagen.provider.RuRuTranslationProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class Nlf2DataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()
        // Translations
        pack.addProvider(::EnUsTranslationProvider)
        pack.addProvider(::RuRuTranslationProvider)
        generator.run()
    }
}