package com.bleudev.nlf2.custom.world.level.block

import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.world.level.block.entity.ComputerBlockEntity
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult

class ComputerBlock(properties: Properties) : BaseEntityBlock(properties) {
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

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult = (level.getBlockEntity(pos) as? ComputerBlockEntity)?.use(state, level, pos) ?: InteractionResult.FAIL

    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::ComputerBlock)
    override fun newBlockEntity(worldPosition: BlockPos, blockState: BlockState): BlockEntity = ComputerBlockEntity(worldPosition, blockState)
    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = createTickerHelper(type, Nlf2BlockEntities.COMPUTER, ComputerBlockEntity::tick)

    companion object {
        val FACING: EnumProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING
        val STATE: EnumProperty<State> = EnumProperty.create("state", State::class.java)
    }

    enum class State(val frontTextureSuffix: String, val sideTextureSuffix: String = "side") : StringRepresentable {
        EMPTY("empty"),
        DANGER("danger"),
        POINTS("points"),
        QUESTION("question"),
        UPDATE("update"),
        RED("red"),
        RED_SAD("red_sad"),
        RED_SMILE("red_smile"),
        RED_STAR("red_star");

        override fun getSerializedName(): String = name.lowercase()
    }
}