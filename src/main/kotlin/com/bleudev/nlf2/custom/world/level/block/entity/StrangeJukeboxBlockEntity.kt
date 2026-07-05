package com.bleudev.nlf2.custom.world.level.block.entity

import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.packet.payload.unit.StrangeJukeboxMusicStop
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.MUS
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.ON
import com.bleudev.nlf2.util.sendToEveryone
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult


class StrangeJukeboxBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(Nlf2BlockEntities.STRANGE_JUKEBOX_ENTITY, pos, state) {
    var currOn: Boolean? = null
    var currMus: StrangeJukeboxBlock.SelectedMus? = null
    var t: Int = 0
    var duration: Int = 100000

    internal fun tick(level: Level, pos: BlockPos, state: BlockState) {
        val on = state.getValue(ON)
        val mus = state.getValue(MUS)
        if (currOn != on || currMus != mus) {
            currOn = on
            currMus = mus
            updatePlayer(level, on, mus)
        }
        if (on) t++

        if (t >= duration) {
            if (on && !mus.loop) {
                level.setBlockAndUpdate(pos, state.setValue(ON, false))
                currOn = false
                updatePlayer(level, false, mus)
            } else updatePlayer(level, on, mus)
        }

        println("$t $duration")
    }

    internal fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (level.isClientSide || !player.abilities.mayBuild) return InteractionResult.PASS
        var state = state.setValue(ON, !state.getValue(ON))
        if (state.getValue(ON)) state = state.setValue(MUS, StrangeJukeboxBlock.SelectedMus.newMus())
        level.setBlockAndUpdate(pos, state)
        println("USE ${state.getValue(ON)} ${state.getValue(MUS)}")
        return InteractionResult.SUCCESS
    }

    private fun updatePlayer(level: Level, on: Boolean, mus: StrangeJukeboxBlock.SelectedMus) {
        if (on || (t >= duration && mus.loop)) {
            t = 0
            val pitch = mus.pitch
            duration = (mus.duration / pitch).toInt()
            level.playSound(null, blockPos, mus.mus_sound, SoundSource.RECORDS, 1f, pitch)
        } else {
            level.server?.sendToEveryone { arrayOf(StrangeJukeboxMusicStop.INSTANCE) }
        }
    }

    companion object {
        internal fun tick(level: Level, pos: BlockPos, state: BlockState, entity: StrangeJukeboxBlockEntity) =
            entity.tick(level, pos, state) // Wrapper
    }
}