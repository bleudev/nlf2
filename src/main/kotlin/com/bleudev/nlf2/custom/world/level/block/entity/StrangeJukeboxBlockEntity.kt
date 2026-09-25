package com.bleudev.nlf2.custom.world.level.block.entity

import com.bleudev.nlf2.client.util.sound
import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.Nlf2ParticleTypes
import com.bleudev.nlf2.custom.packet.payload.StrangeJukeboxRainbowControl
import com.bleudev.nlf2.custom.packet.payload.unit.StrangeJukeboxMusicStop
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.MUS
import com.bleudev.nlf2.custom.world.level.block.StrangeJukeboxBlock.Companion.ON
import com.bleudev.nlf2.custom.world.level.saveddata.nlf2data
import com.bleudev.nlf2.util.breakVertBlockIfNeed
import com.bleudev.nlf2.util.openRange
import com.bleudev.nlf2.util.sendToEveryone
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import java.util.*


class StrangeJukeboxBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(Nlf2BlockEntities.STRANGE_JUKEBOX, pos, state) {
    override fun preRemoveSideEffects(pos: BlockPos, state: BlockState) {
        level?.let { updatePlayer(it, false, StrangeJukeboxBlock.SelectedMus.NULL) }
    }

    private var currOn: Boolean? = null
    private var currMus: StrangeJukeboxBlock.SelectedMus? = null
    private var currForce: StrangeJukeboxBlock.SelectedMus = StrangeJukeboxBlock.SelectedMus.NULL
    private var t: Int = 0
    private var duration: Int = 100000
    var cardConsumed: Boolean = false
    private var cardOwner: UUID? = null
    private var sinceLastTurningOn: Int = 0
    private var sinceLastCardFail: Int = -1
    private var sleepTryCount: Int = -1

    val rainbowedPlayers: ArrayList<String> = ArrayList()

    override fun loadAdditional(input: ValueInput) {
        val card = input.childOrEmpty("card")
        this.cardConsumed = card.getBooleanOr("consumed", false)
        card.getStringOr("owner", "null").let {
            this.cardOwner = if (it == "null") null else UUID.fromString(it)
        }

        this.sleepTryCount = input.getIntOr("sleep_try_count", -1)
    }

    override fun saveAdditional(output: ValueOutput) {
        val card = output.child("card")
        card.putBoolean("consumed", this.cardConsumed)
        card.putString("owner", this.cardOwner.toString())

        output.putInt("sleep_try_count", this.sleepTryCount)
    }

    internal fun tick(level: Level, pos: BlockPos, state: BlockState) {
        println("DEBUG CARD FAIL $sinceLastCardFail")

        if (breakVertBlockIfNeed(level, pos, state)) {
            return
        }
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
        if (on) {
            t++
            sinceLastTurningOn++
        } else {
            sinceLastTurningOn = 0
        }
        if (sinceLastCardFail >= 0) sinceLastCardFail++

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
        if (on) {
            if (mus == StrangeJukeboxBlock.SelectedMus.JUKEBOX_SLEEP) {
                if (sinceLastTurningOn == 1) {
                    sleepTryCount++
                }

                fun send(c: Component) {
                    for (player in level.players()) {
                        if (player.distanceToSqr(Vec3.atCenterOf(pos)) <= 10*10) {
                            player.sendOverlayMessage(c)
                        }
                    }
                }
                var message: Component? = null
                when (sleepTryCount) {
                    0 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.0.0")
                        }
                        if (sinceLastTurningOn == 80) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.0.1")
                        }
                    }
                    1 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.1.0")
                        }
                        if (sinceLastTurningOn == 80) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.1.1")
                        }
                        if (sinceLastTurningOn == 140) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.1.2")
                        }
                    }
                    2 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.2.0")
                        }
                    }
                    3 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.3.0")
                        }
                    }
                    4 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.4.0")
                        }
                    }
                    5 -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.5.0")
                        }
                    }
                    in 6.openRange() -> {
                        if (sinceLastTurningOn == 20) {
                            message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.6.0")
                        }
                    }
                }
                when (sinceLastCardFail) {
                    1 -> {
                        message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.card.0")
                        sinceLastTurningOn = 999
                    }
                    60 -> {
                        message = Component.translatable("nlf2.message.block.strange_jukebox.sleep.card.1")
                        sinceLastTurningOn = 999
                    }
                }

                message?.also(::send)
            }
            else {
                sleepTryCount = -1
            }
        }
    }

    internal fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, card: Boolean = false): InteractionResult {
        if (!player.abilities.mayBuild) return InteractionResult.PASS
        if (state.getValue(ON) && state.getValue(MUS) == StrangeJukeboxBlock.SelectedMus.EASTER_EGG
            && t >= ((180.toFloat()/650)*duration).toInt()) return InteractionResult.TRY_WITH_EMPTY_HAND
        if (!level.isClientSide) {
            var state = state.setValue(ON, !state.getValue(ON))
            (level as? ServerLevel)?.let {
                if (state.getValue(ON)) {
                    state = state.setValue(MUS, StrangeJukeboxBlock.SelectedMus.newMus(it.server, card))
                    if (card) {
                        cardConsumed = true
                        cardOwner = player.uuid
                    }
                } else {
                    tryReturnCard(level, player)
                }
            }
            level.setBlockAndUpdate(pos, state)
        }
        return InteractionResult.SUCCESS
    }

    internal fun broadcastCardFail() {
        sinceLastCardFail = 0
    }

    private fun updatePlayer(level: Level, on: Boolean, mus: StrangeJukeboxBlock.SelectedMus) {
        if (on || (t >= duration && mus.loop)) {
            t = 0
            val pitch = mus.pitch
            duration = (mus.duration / pitch).toInt()
            level.playSound(null, blockPos, mus.mus_sound, SoundSource.RECORDS, mus.volume, pitch)
        } else {
            level.server?.sendToEveryone { arrayOf(
                StrangeJukeboxRainbowControl(false), StrangeJukeboxMusicStop.INSTANCE
            ) }
            tryReturnCard(level)
        }
    }

    private fun tryReturnCard(level: Level, player: Player? = null) {
        val player = player ?: cardOwner?.let { level.getPlayerByUUID(it) }
        if (cardConsumed) {
            cardConsumed = false
            if (player == null) return
            if (player.gameMode()?.isSurvival ?: true) {
                player.sound(SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
                    0.2f,
                    ((player.random.nextFloat() - player.random.nextFloat()) * 0.7f + 1.0f) * 2.0f)
                player.inventory.addAndPickItem(ItemStack(Nlf2Items.CARD))
            }
        }
    }

    private fun spawnMusicParticles(level: LevelAccessor, blockPos: BlockPos) {
        if (level is ServerLevel) {
            val bl = blockState.getValue(MUS) == StrangeJukeboxBlock.SelectedMus.JUKEBOX_SLEEP

            val pos = Vec3.atBottomCenterOf(blockPos).add(0.0, 1.2, 0.0)
            val xDist = level.getRandom().nextInt(4).toFloat() / 24.0f
            level.sendParticles(if (bl) Nlf2ParticleTypes.SLEEP else ParticleTypes.NOTE, pos.x(), pos.y(), pos.z(), 0, xDist.toDouble(), 0.0, 0.0, 1.0)
        }
    }

    companion object {
        internal fun tick(level: Level, pos: BlockPos, state: BlockState, entity: StrangeJukeboxBlockEntity) =
            entity.tick(level, pos, state) // Wrapper
    }
}