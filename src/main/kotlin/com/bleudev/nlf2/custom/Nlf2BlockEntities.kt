package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.level.block.entity.ComputerBlockEntity
import com.bleudev.nlf2.custom.world.level.block.entity.StrangeJukeboxBlockEntity
import com.bleudev.nlf2.util.registerBlockEntity

object Nlf2BlockEntities {
    val STRANGE_JUKEBOX = registerBlockEntity("strange_jukebox_entity", ::StrangeJukeboxBlockEntity, Nlf2Blocks.STRANGE_JUKEBOX)
    val COMPUTER = registerBlockEntity("computer_entity", ::ComputerBlockEntity, Nlf2Blocks.COMPUTER)

    fun initialize() {}
}