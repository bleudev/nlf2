package com.bleudev.nlf2.util

import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

fun breakVertBlockIfNeed(level: Level, pos: BlockPos, state: BlockState? = null): Boolean {
    val state = state ?: level.getBlockState(pos)
    val server = level.server
    if (server != null && server.nlf2data.loreData.noVert) {
        if (VERT_ITEMS.any { i -> (i as? Block)?.let { b -> state.`is`(b) } ?: false }) {
            if (state.hasBlockEntity()) {
                level.removeBlockEntity(pos)
            }
            return level.removeBlock(pos, false)
        }
    }
    return false
}