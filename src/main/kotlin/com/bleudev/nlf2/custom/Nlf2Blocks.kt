package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.level.block.AmethystOxeyeDaisy
import com.bleudev.nlf2.custom.world.level.block.ArenaBlock
import com.bleudev.nlf2.custom.world.level.block.ComputerBlock
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.util.registerBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour


object Nlf2Blocks {
    val STRANGE_JUKEBOX = registerBlock(
        "strange_jukebox",
        ::StrangeJukeboxBlock,
        BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX),
        true
    )
    val AMETHYST_OXEYE_DAISY = registerBlock(
        "amethyst_oxeye_daisy",
        ::AmethystOxeyeDaisy,
        BlockBehaviour.Properties.ofFullCopy(Blocks.OXEYE_DAISY),
        true
    )
    val ARENA_BLOCK = registerBlock(
        "arena_block",
        ::ArenaBlock,
        BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK),
        true
    )
    val COMPUTER = registerBlock(
        "computer",
        ::ComputerBlock,
        BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK),
        true
    )

    fun initialize() {}
}