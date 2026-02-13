package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Items
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class EnUsTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "en_us", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Puzzle?")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Sounds
        builder.add("sound.nlf2.broke", "Broke")
        builder.add("sound.nlf2.laugh", "Strange laugh")
        builder.add("sound.nlf2.border_ambient", "Strange noise")
        builder.add("sound.nlf2.pause_cutscene_ambient", "Strange noise")
    }
}

class RuRuTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "ru_ru", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Головоломка?")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Sounds
        builder.add("sound.nlf2.broke", "Удар")
        builder.add("sound.nlf2.laugh", "Странный смех")
        builder.add("sound.nlf2.border_ambient", "Странный шум")
        builder.add("sound.nlf2.pause_cutscene_ambient", "Странный шум")
    }
}