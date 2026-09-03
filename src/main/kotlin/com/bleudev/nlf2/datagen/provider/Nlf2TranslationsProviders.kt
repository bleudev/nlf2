package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.Nlf2MobEffects
import com.bleudev.nlf2.custom.Nlf2Sounds
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class EnUsTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "en_us", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Message
        builder.add("nlf2.server.disconnect.version.please.update", "Please update the mod")
        builder.add("nlf2.message.item.card.check", "(You have deja vu.)")
        builder.add("nlf2.message.block.strange_jukebox.sleep.0", "(In sleep mode.)")
        builder.add("nlf2.message.block.strange_jukebox.sleep.1", "(...It sleeps.)")
        builder.add("nlf2.message.event.112.disconnect", $$"%1$s was erased")
        builder.add("multiplayer.player.joined.unknown", "??? joined the game")
        builder.add("multiplayer.player.left.unknown", "??? left the game")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Puzzle?")
        builder.add(Nlf2Items.MANUAL, "Manual")
        builder.add(Nlf2Items.CARD, "Card")
        builder.add(Nlf2Items.CHARGED_MANUAL, "Charged manual")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Strange jukebox")
        builder.add(Nlf2Blocks.AMETHYST_OXEYE_DAISY, "Amethyst oxeye daisy")
        builder.add(Nlf2Blocks.ARENA_BLOCK, "Arena block")
        builder.add(Nlf2Blocks.COMPUTER, "Computer")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Mob effects
        builder.add(Nlf2MobEffects.GLIDING.value(), "Gliding")
        // Sounds
        builder.add(Nlf2Sounds.BROKE, "Broke")
        builder.add(Nlf2Sounds.LAUGH, "Strange laugh")
        builder.add(Nlf2Sounds.BORDER_AMBIENT, "Strange noise")
        builder.add(Nlf2Sounds.PAUSE_CUTSCENE_AMBIENT, "Strange noise")
        builder.add(Nlf2Sounds.MUS_STRANGE_NOISE, "Strange noise")
        builder.add(Nlf2Sounds.MUS_MILKICH_APPEARS, "12 (Not) lactose-free")
        builder.add(Nlf2Sounds.MUS_JUKEBOX, "13 The Abandoned World")
        builder.add(Nlf2Sounds.MUS_EASTER_EGG, "14 Jukeboxing To The Fullest!")
        builder.add(Nlf2Sounds.MUS_PART4_UNKNOWN_APPEARS, "16 Familiar intruder")
        builder.add(Nlf2Sounds.MUS_JUKEBOX_SLEEP, "17 Juke box")
        builder.add(Nlf2Sounds.MUS_STORY, "18 Long time ago")
        builder.add(Nlf2Sounds.MUS_PART4_BYE_VERT, "Strange music")
        builder.add(Nlf2Sounds.MUS_MAN_WEIRD, "LOUD MUSIC")
        builder.add(Nlf2Sounds.MUS_QUAKE, "LOUD EARTHQUAKE")
        builder.add(Nlf2Sounds.MUS_PART4_BYE, "19 GOODBYE")
        builder.add(Nlf2Sounds.MUS_PART5_BATTLE, "20 Last chance")
    }
}

class RuRuTranslationProvider(packOutput: FabricPackOutput, reg: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(packOutput, "ru_ru", reg) {
    override fun generateTranslations(reg: HolderLookup.Provider, builder: TranslationBuilder) {
        // Server version disconnect
        builder.add("nlf2.server.disconnect.version.please.update", "Пожалуйста, обновите мод")
        builder.add("nlf2.message.item.card.check", "(У вас дежавю.)")
        builder.add("nlf2.message.block.strange_jukebox.sleep.0", "(В спящем режиме.)")
        builder.add("nlf2.message.block.strange_jukebox.sleep.1", "(...Он спит.)")
        builder.add("nlf2.message.event.112.disconnect", $$"%1$s был стёрт")
        builder.add("multiplayer.player.joined.unknown", "??? присоединился к игре")
        builder.add("multiplayer.player.left.unknown", "??? покинул игру")
        // Items
        builder.add(Nlf2Items.WD_PUZZLE, "Головоломка?")
        builder.add(Nlf2Items.MANUAL, "Руководство")
        builder.add(Nlf2Items.CARD, "Перфокарта")
        builder.add(Nlf2Items.CHARGED_MANUAL, "Заряженное руководство")
        builder.add(Nlf2Blocks.STRANGE_JUKEBOX, "Странный проигрыватель")
        builder.add(Nlf2Blocks.AMETHYST_OXEYE_DAISY, "Аметистовая ромашка")
        builder.add(Nlf2Blocks.ARENA_BLOCK, "Блок арены")
        builder.add(Nlf2Blocks.COMPUTER, "Компьютер")
        // Creative mode tabs
        builder.add("creativeModeTab.nlf2.general", "Nine Lifes 2")
        // Mob effects
        builder.add(Nlf2MobEffects.GLIDING.value(), "Планирование")
        // Sounds
        builder.add(Nlf2Sounds.BROKE, "Удар")
        builder.add(Nlf2Sounds.LAUGH, "Странный смех")
        builder.add(Nlf2Sounds.BORDER_AMBIENT, "Странный шум")
        builder.add(Nlf2Sounds.PAUSE_CUTSCENE_AMBIENT, "Странный шум")
        builder.add(Nlf2Sounds.MUS_STRANGE_NOISE, "Странный шум")
        builder.add(Nlf2Sounds.MUS_PART4_BYE_VERT, "Странная музыка")
        builder.add(Nlf2Sounds.MUS_MAN_WEIRD, "ГРОМКАЯ МУЗЫКА")
        builder.add(Nlf2Sounds.MUS_QUAKE, "ГРОМКОЕ ЗЕМЛЕТРЯСЕНИЕ")
    }
}