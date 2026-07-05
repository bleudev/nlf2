package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class EnUsTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "en_us", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Server version disconnect
        builder.add("nlf2.server.disconnect.version.please.update", "Please update the mod")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Puzzle?")
        builder.add(Nlf2Items.MANUAL, "Manual")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Strange jukebox")
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
        // Server version disconnect
        builder.add("nlf2.server.disconnect.version.please.update", "Пожалуйста, обновите мод")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Головоломка?")
        builder.add(Nlf2Items.MANUAL, "Руководство")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Странный проигрыватель")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Sounds
        builder.add("sound.nlf2.broke", "Удар")
        builder.add("sound.nlf2.laugh", "Странный смех")
        builder.add("sound.nlf2.border_ambient", "Странный шум")
        builder.add("sound.nlf2.pause_cutscene_ambient", "Странный шум")
    }
}