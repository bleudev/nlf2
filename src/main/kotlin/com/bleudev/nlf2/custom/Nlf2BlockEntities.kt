package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.world.level.block.entity.StrangeJukeboxBlockEntity
import com.bleudev.nlf2.util.registerBlockEntity

object Nlf2BlockEntities {
    val STRANGE_JUKEBOX_ENTITY = registerBlockEntity("strange_jukebox_entity", ::StrangeJukeboxBlockEntity, Nlf2Blocks.STRANGE_JUKEBOX)

    fun initialize() {}
}