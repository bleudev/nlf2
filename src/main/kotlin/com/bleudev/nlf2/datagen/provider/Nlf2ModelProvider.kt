package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.world.level.block.ComputerBlock
import com.bleudev.nlf2.getIdentifier
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.*
import net.minecraft.core.Direction

class Nlf2ModelProvider(packOutput: FabricPackOutput) : FabricModelProvider(packOutput) {
    override fun generateBlockStateModels(generators: BlockModelGenerators) {
        generators.createTrivialBlock(Nlf2Blocks.STRANGE_JUKEBOX, TexturedModel.CUBE_TOP)
        generators.createCrossBlockWithDefaultItem(Nlf2Blocks.AMETHYST_OXEYE_DAISY, BlockModelGenerators.PlantType.NOT_TINTED)

        // Computer model (WTF HOW IT BIG)
        fun createComputerModel(facing: Direction, state: ComputerBlock.State): MultiVariant {
            val frontTexture = TextureMapping.getBlockTexture(Nlf2Blocks.COMPUTER, "_" + state.frontTextureSuffix)
            val sideTexture = TextureMapping.getBlockTexture(Nlf2Blocks.COMPUTER, "_" + state.sideTextureSuffix)
            val modelSuffix = "_${state.serializedName}_${facing.serializedName}"

            val slotMap = mapOf(
                Direction.DOWN to TextureSlot.DOWN,
                Direction.UP to TextureSlot.UP,
                Direction.NORTH to TextureSlot.NORTH,
                Direction.SOUTH to TextureSlot.SOUTH,
                Direction.WEST to TextureSlot.WEST,
                Direction.EAST to TextureSlot.EAST,
            )

            return BlockModelGenerators.plainVariant(TexturedModel.createDefault(TextureMapping::cube, ModelTemplates.CUBE).updateTexture {
                for (d in Direction.entries) {
                    slotMap[d]?.let { slot ->
                        it.put(slot, if (d == facing) frontTexture else sideTexture)
                    }
                }
                it.put(TextureSlot.PARTICLE, sideTexture)
            }.createWithSuffix(Nlf2Blocks.COMPUTER, modelSuffix, generators.modelOutput))
        }

        generators.blockStateOutput.accept(MultiVariantGenerator
            .dispatch(Nlf2Blocks.COMPUTER)
            .with(PropertyDispatch
                .initial(ComputerBlock.FACING, ComputerBlock.STATE)
                .generate(::createComputerModel)
            ))
        generators.itemModelOutput.accept(
            Nlf2Blocks.COMPUTER.asItem(),
            ItemModelUtils.plainModel(getIdentifier("block/computer_empty_east"))
        )
        // End)
    }

    override fun generateItemModels(generators: ItemModelGenerators) {
        generators.generateFlatItem(Nlf2Items.WD_PUZZLE, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.MANUAL, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.CARD, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.CHARGED_MANUAL, ModelTemplates.FLAT_ITEM)
    }

}