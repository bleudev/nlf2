package com.bleudev.nlf2.custom

import com.bleudev.nlf2.getIdentifier
import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.renderer.RenderPipelines

object Nlf2RenderPipelines {
    val GUI_TEXTURED_ANAGLYPH = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
        .withLocation("pipeline/gui_textured_anaglyph.fsh")
        .withFragmentShader(getIdentifier("pipeline/gui_textured_anaglyph"))
        .build()

    fun initialize(initializer: (RenderPipeline) -> Unit) {
        initializer(GUI_TEXTURED_ANAGLYPH)
    }
}