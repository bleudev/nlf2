package com.bleudev.nlf2.custom.world.level.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult

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

    private var interaction: Int = 0

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val maxCount = 8
        level.setBlockAndUpdate(pos, state.setValue(STATE, when (interaction % maxCount) {
            // Start
            0 -> State.RED
            // Nooo!!!! This can't be!! I almost... I just wanted to be... strong... And after your success... I... became jealous... Why did you get all this? Why you, why not me??...
            1 -> State.RED_SAD
            // No, no, no... No need to apologize. The only one who should do this is me... Me...
            2 -> State.RED
            // a jealous vessel without any humanity, just wanted to get him by force??! I... almost ruined your entire career! I'm stupid!
            3 -> State.RED_SAD
            // Perhaps... But... before we say goodbye... Can I ask you a favor?.. Can you make my dream come true, can you unite with me?...
            4 -> State.RED
            // Of course, of course... You have a real human soul... Why would you need a dumb vessel?
            5 -> State.RED_SAD
            // Really???
            6 -> State.RED_SMILE
            // You're the best person I've ever met!
            7 -> State.RED_STAR
            else -> State.EMPTY
        }))
        interaction++
        return InteractionResult.PASS
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
        UPDATE("update"),
        RED("red"),
        RED_SAD("red_sad"),
        RED_SMILE("red_smile"),
        RED_STAR("red_star");

        override fun getSerializedName(): String = name.lowercase()
    }
}