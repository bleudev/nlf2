package com.bleudev.nlf2.custom.world.level.block

import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty

class ComputerBlock(properties: Properties) : Block(properties) {
    init {
        registerDefaultState(defaultBlockState()
            .setValue(FACING, Direction.NORTH)
            .setValue(STATE, State.EMPTY)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, STATE)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState()
            .setValue(FACING, context.horizontalDirection.opposite)
    }

    companion object {
        val FACING: EnumProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING
        val STATE: EnumProperty<State> = EnumProperty.create("state", State::class.java)
    }

    enum class State(val frontTextureSuffix: String, val sideTextureSuffix: String = "side") : StringRepresentable {
        EMPTY("empty"),
        DANGER("danger"),
        POINTS("points"),
        QUESTION("question"),
        UPDATE("update");

        override fun getSerializedName(): String = name.lowercase()
    }
}