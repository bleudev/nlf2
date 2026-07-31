package com.bleudev.nlf2.custom.world.level.block

import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.data.saved.nlf2data
import com.bleudev.nlf2.custom.world.level.block.entity.StrangeJukeboxBlockEntity
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.server.MinecraftServer
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import kotlin.random.Random


class StrangeJukeboxBlock(properties: Properties) : BaseEntityBlock(properties) {
    init {
        registerDefaultState(defaultBlockState()
            .setValue(ON, true)
            .setValue(MUS, SelectedMus.JUKEBOX))
    }
    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::StrangeJukeboxBlock)
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = StrangeJukeboxBlockEntity(pos, state)
    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = createTickerHelper(type, Nlf2BlockEntities.STRANGE_JUKEBOX_ENTITY, StrangeJukeboxBlockEntity::tick)
    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ON, MUS)
    }
    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult = (level.getBlockEntity(pos) as? StrangeJukeboxBlockEntity)
        ?.use(state, level, pos, player) ?: InteractionResult.PASS // Wrapper

    companion object {
        val ON = BooleanProperty.create("on")
        val MUS = EnumProperty.create("mus", SelectedMus::class.java)
    }

    enum class SelectedMus(val mus_name: String, val mus_sound: SoundEvent, val duration: Int, private val min_pitch: Float, private val max_pitch: Float, val volume: Float, val loop: Boolean) : StringRepresentable {
        STRANGE_NOISE("strange_noise", Nlf2Sounds.MUS_STRANGE_NOISE, 20, 1f, 1f, .025f, true),
        MILKICH_APPEARS("milkich_appears", Nlf2Sounds.MUS_MILKICH_APPEARS, 790, 1f, 1f, .2f, true),
        JUKEBOX("jukebox", Nlf2Sounds.MUS_JUKEBOX, 1660, 1f, 1f, .7f, true),
        EASTER_EGG("easter_egg", Nlf2Sounds.MUS_EASTER_EGG, 650, 0.7f, 1.1f, .5f, false),
        NULL("null_cludge", Nlf2Sounds.MUS_JUKEBOX, 0, 1f, 1f, 0f, false);

        override fun getSerializedName(): String = mus_name

        val pitch: Float get() = if (min_pitch == max_pitch) min_pitch else Random.nextDouble(min_pitch.toDouble(), max_pitch.toDouble()).toFloat()

        companion object {
            fun newMus(server: MinecraftServer): SelectedMus {
                val force = server.nlf2data.loreData.forceJukeboxMus
                if (force != NULL) return force
                val rnd = Random.nextFloat()
                if (rnd <= .2f) return EASTER_EGG
                return JUKEBOX
            }

            fun fromName(name: String): SelectedMus = SelectedMus.entries.first { it.mus_name == name }

            val CODEC: Codec<SelectedMus> = RecordCodecBuilder.create { it.group(
                Codec.STRING.fieldOf("name").forGetter(SelectedMus::mus_name),
            ).apply(it, SelectedMus::fromName) }
        }
    }
}