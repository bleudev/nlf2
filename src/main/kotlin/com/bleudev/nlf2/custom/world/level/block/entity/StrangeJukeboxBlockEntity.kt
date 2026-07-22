package com.bleudev.nlf2.custom.world.level.block.entity

import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.packet.payload.StrangeJukeboxRainbowControl
import com.bleudev.nlf2.custom.packet.payload.unit.StrangeJukeboxMusicStop
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.MUS
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.ON
import com.bleudev.nlf2.util.sendToEveryone
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3


class StrangeJukeboxBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(Nlf2BlockEntities.STRANGE_JUKEBOX_ENTITY, pos, state) {
    override fun preRemoveSideEffects(pos: BlockPos, state: BlockState) {
        level?.let { updatePlayer(it, false, StrangeJukeboxBlock.SelectedMus.NULL) }
    }

    var currOn: Boolean? = null
    var currMus: StrangeJukeboxBlock.SelectedMus? = null
    var currForce: StrangeJukeboxBlock.SelectedMus = StrangeJukeboxBlock.SelectedMus.NULL
    var t: Int = 0
    var duration: Int = 100000

    val rainbowedPlayers: ArrayList<String> = ArrayList()

    internal fun tick(level: Level, pos: BlockPos, state: BlockState) {
        val on = state.getValue(ON)
        var mus = state.getValue(MUS)

        if (on) (level as? ServerLevel)?.let {
            val force = it.server.nlf2data.loreData.forceJukeboxMus

            if (currForce != force) {
                mus = if (force == StrangeJukeboxBlock.SelectedMus.NULL) StrangeJukeboxBlock.SelectedMus.newMus(it.server) else force
                currMus = mus
                currForce = force
                level.setBlockAndUpdate(pos, state.setValue(MUS, mus))
                updatePlayer(level, false, mus)
                updatePlayer(level, true, mus)
            }
        }

        if (currOn != on || currMus != mus) {
            currOn = on
            currMus = mus
            updatePlayer(level, on, mus)
        }
        if (on && t % 20 == 0) spawnMusicParticles(level, pos)
        if (on) t++

        if (t >= duration) {
            if (on && !mus.loop) {
                level.setBlockAndUpdate(pos, state.setValue(ON, false))
                currOn = false
                updatePlayer(level, false, mus)
            } else updatePlayer(level, on, mus)
        }

        if (mus == StrangeJukeboxBlock.SelectedMus.EASTER_EGG) {
            val startTime = ((180.toFloat()/650)*duration).toInt()
            val endTime = ((633.toFloat()/650)*duration).toInt()
            if (t == startTime) rainbowedPlayers.clear()
            if (t in startTime..endTime) {
                level.server?.sendToEveryone {
                    val name = it.gameProfile.name.lowercase()
                    if (it.distanceToSqr(Vec3.atCenterOf(pos)) <= 15*15 && name !in rainbowedPlayers) {
                        rainbowedPlayers.add(name)
                        arrayOf(StrangeJukeboxRainbowControl(true))
                    } else arrayOf()
                }
            }
            if (t == endTime) level.server?.sendToEveryone { arrayOf(StrangeJukeboxRainbowControl(false)) }
        }
    }

    internal fun use(state: BlockState, level: Level, pos: BlockPos, player: Player): InteractionResult {
        if (!player.abilities.mayBuild) return InteractionResult.PASS
        if (state.getValue(ON) && state.getValue(MUS) == StrangeJukeboxBlock.SelectedMus.EASTER_EGG
            && t >= ((180.toFloat()/650)*duration).toInt()) return InteractionResult.TRY_WITH_EMPTY_HAND
        if (!level.isClientSide) {
            var state = state.setValue(ON, !state.getValue(ON))
            (level as? ServerLevel)?.let { if (state.getValue(ON))
                state = state.setValue(MUS, StrangeJukeboxBlock.SelectedMus.newMus(it.server))
            }
            level.setBlockAndUpdate(pos, state)
        }
        return InteractionResult.SUCCESS
    }

    private fun updatePlayer(level: Level, on: Boolean, mus: StrangeJukeboxBlock.SelectedMus) {
        if (on || (t >= duration && mus.loop)) {
            t = 0
            val pitch = mus.pitch
            duration = (mus.duration / pitch).toInt()
            level.playSound(null, blockPos, mus.mus_sound, SoundSource.RECORDS, 1f, pitch)
        } else {
            level.server?.sendToEveryone { arrayOf(
                StrangeJukeboxRainbowControl(false), StrangeJukeboxMusicStop.INSTANCE
            ) }
        }
    }

    private fun spawnMusicParticles(level: LevelAccessor, blockPos: BlockPos) {
        if (level is ServerLevel) {
            val pos = Vec3.atBottomCenterOf(blockPos).add(0.0, 1.2, 0.0)
            val randomColor = level.getRandom().nextInt(4).toFloat() / 24.0f
            level.sendParticles(ParticleTypes.NOTE, pos.x(), pos.y(), pos.z(), 0, randomColor.toDouble(), 0.0, 0.0, 1.0)
        }
    }

    companion object {
        internal fun tick(level: Level, pos: BlockPos, state: BlockState, entity: StrangeJukeboxBlockEntity) =
            entity.tick(level, pos, state) // Wrapper
    }
}