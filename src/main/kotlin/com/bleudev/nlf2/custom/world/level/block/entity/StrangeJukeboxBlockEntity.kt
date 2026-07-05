package com.bleudev.nlf2.custom.world.level.block.entity

import com.bleudev.nlf2.custom.Nlf2BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState


class StrangeJukeboxBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(Nlf2BlockEntities.STRANGE_JUKEBOX_ENTITY, pos, state) {
    internal fun tick(level: Level, pos: BlockPos, state: BlockState) {
    }

    companion object {
        internal fun tick(level: Level, pos: BlockPos, state: BlockState, entity: StrangeJukeboxBlockEntity) =
            entity.tick(level, pos, state) // Wrapper
    }
}