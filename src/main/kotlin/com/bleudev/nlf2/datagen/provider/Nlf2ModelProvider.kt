package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TexturedModel

class Nlf2ModelProvider(packOutput: FabricPackOutput) : FabricModelProvider(packOutput) {
    override fun generateBlockStateModels(generators: BlockModelGenerators) {
        generators.createTrivialBlock(Nlf2Blocks.STRANGE_JUKEBOX, TexturedModel.CUBE_TOP)
        generators.createCrossBlockWithDefaultItem(Nlf2Blocks.AMETHYST_OXEYE_DAISY, BlockModelGenerators.PlantType.NOT_TINTED)
    }

    override fun generateItemModels(generators: ItemModelGenerators) {
        generators.generateFlatItem(Nlf2Items.WD_PUZZLE, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.MANUAL, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.CARD, ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Nlf2Items.CHARGED_MANUAL, ModelTemplates.FLAT_ITEM)
    }

}