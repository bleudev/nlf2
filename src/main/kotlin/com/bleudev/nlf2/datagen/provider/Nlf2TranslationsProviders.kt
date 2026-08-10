package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.world.Nlf2MobEffects
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class EnUsTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "en_us", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Server version disconnect
        builder.add("nlf2.server.disconnect.version.please.update", "Please update the mod")
        builder.add("nlf2.message.item.card.check", "(You have deja vu.)")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Puzzle?")
        builder.add(Nlf2Items.MANUAL, "Manual")
        builder.add(Nlf2Items.CARD, "Card")
        builder.add(Nlf2Items.CHARGED_MANUAL, "Charged manual")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Strange jukebox")
        builder.add(Nlf2Blocks.AMETHYST_OXEYE_DAISY, "Amethyst oxeye daisy")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Mob effects
        builder.add(Nlf2MobEffects.GLIDING.value(), "Gliding")
        // Sounds
        builder.add("sound.nlf2.broke", "Broke")
        builder.add("sound.nlf2.laugh", "Strange laugh")
        builder.add("sound.nlf2.border_ambient", "Strange noise")
        builder.add("sound.nlf2.pause_cutscene_ambient", "Strange noise")
        builder.add("sound.nlf2.mus_strange_noise", "Strange noise")
        builder.add("sound.nlf2.mus_milkich_appears", "10 (Not) lactose-free")
        builder.add("sound.nlf2.mus_jukebox_puzzle", "")
        builder.add("sound.nlf2.mus_story", "")
        builder.add("sound.nlf2.mus_jukebox", "11 The Abandoned World")
        builder.add("sound.nlf2.mus_easter_egg", "12 Jukeboxing To The Fullest!")
    }
}

class RuRuTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "ru_ru", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Server version disconnect
        builder.add("nlf2.server.disconnect.version.please.update", "Пожалуйста, обновите мод")
        builder.add("nlf2.message.item.card.check", "(У вас дежавю.)")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Головоломка?")
        builder.add(Nlf2Items.MANUAL, "Руководство")
        builder.add(Nlf2Items.CARD, "Перфокарта")
        builder.add(Nlf2Items.CHARGED_MANUAL, "Заряженное руководство")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Странный проигрыватель")
        builder.add(Nlf2Blocks.AMETHYST_OXEYE_DAISY, "Аметистовая ромашка")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Mob effects
        builder.add(Nlf2MobEffects.GLIDING.value(), "Планирование")
        // Sounds
        builder.add("sound.nlf2.broke", "Удар")
        builder.add("sound.nlf2.laugh", "Странный смех")
        builder.add("sound.nlf2.border_ambient", "Странный шум")
        builder.add("sound.nlf2.pause_cutscene_ambient", "Странный шум")
        builder.add("sound.nlf2.mus_strange_noise", "Странный шум")
    }
}