package com.bleudev.nlf2.client.custom.gui.screens

import com.bleudev.nlf2.custom.packet.payload.PacketPuzzle
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class PuzzleScreen(puzzle: PacketPuzzle) : Screen(Component.empty()) {
    val bookLocation: Identifier = getIdentifier(when (puzzle) {
        PacketPuzzle.WD -> "textures/gui/puzzle/wd.png"
    })

    private val bgLeft: Int get() = (this.width - 192) / 2
    private val bgTop: Int get() = 2
    private val controlsTop: Int get() = bgTop + 194

    override fun init() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE) { _ -> this.onClose() }
            .pos((this.width - 200) / 2, controlsTop).width(200)
            .build()
        )
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractBackground(graphics, mouseX, mouseY, a)
        graphics.blit(RenderPipelines.GUI_TEXTURED, bookLocation, bgLeft, bgTop, 0.0f, 0.0f, 192, 192, 256, 256)
    }
}