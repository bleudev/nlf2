package com.bleudev.nlf2.custom.world.level.block

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty

class ArenaBlock(properties: Properties) : Block(properties) {
    init {
        registerDefaultState(defaultBlockState()
            .setValue(GLITCH, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(GLITCH)
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        level.setBlockAndUpdate(pos, state.setValue(GLITCH, random.nextFloat() <= .2f))
    }

    companion object {
        val GLITCH = BooleanProperty.create("glitch")
    }
}