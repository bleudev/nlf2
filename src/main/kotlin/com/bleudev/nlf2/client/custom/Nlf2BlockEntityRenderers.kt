package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.client.custom.renderer.blockentity.ComputerRenderer
import com.bleudev.nlf2.custom.Nlf2BlockEntities
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers

object Nlf2BlockEntityRenderers {
    fun initialize() {
        BlockEntityRenderers.register(Nlf2BlockEntities.COMPUTER, ::ComputerRenderer)
    }
}