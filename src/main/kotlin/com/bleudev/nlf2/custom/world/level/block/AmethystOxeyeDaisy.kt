package com.bleudev.nlf2.custom.world.level.block

import com.bleudev.nlf2.custom.world.Nlf2MobEffects
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState

class AmethystOxeyeDaisy(properties: Properties) : FlowerBlock(Nlf2MobEffects.GLIDING, 30f, properties) {
    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        return super.mayPlaceOn(state, level, pos) ||
                state.`is`(Blocks.AMETHYST_BLOCK) ||
                state.`is`(Blocks.BUDDING_AMETHYST)
    }
}