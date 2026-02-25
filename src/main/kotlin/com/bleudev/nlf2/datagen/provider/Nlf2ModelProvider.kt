package com.bleudev.nlf2.datagen.provider

import com.bleudev.nlf2.custom.Nlf2Items
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates

class Nlf2ModelProvider(packOutput: FabricPackOutput) : FabricModelProvider(packOutput) {
    override fun generateBlockStateModels(blockModelGenerators: BlockModelGenerators) {
    }

    override fun generateItemModels(generator: ItemModelGenerators) {
        generator.generateFlatItem(Nlf2Items.WD_PUZZLE, ModelTemplates.FLAT_ITEM)
        generator.generateFlatItem(Nlf2Items.AMETHYST_STICK, ModelTemplates.FLAT_HANDHELD_ITEM)
    }

}