package com.bleudev.nlf2.custom

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

    fun initialize() {}
}