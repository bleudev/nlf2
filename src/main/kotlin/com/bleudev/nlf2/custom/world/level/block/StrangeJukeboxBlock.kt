package com.bleudev.nlf2.custom.world.level.block

import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.world.level.block.entity.StrangeJukeboxBlockEntity
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class StrangeJukeboxBlock(properties: Properties) : BaseEntityBlock(properties) {
    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::StrangeJukeboxBlock)
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = StrangeJukeboxBlockEntity(pos, state)
    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = createTickerHelper(type, Nlf2BlockEntities.STRANGE_JUKEBOX_ENTITY, StrangeJukeboxBlockEntity::tick)
}