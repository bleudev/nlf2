package com.bleudev.nlf2.client.custom.gui.screens

import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class ManualScreen(val manualId: Int) : Screen(Component.literal("Manunal!")) {
    val bookLocation: Identifier = getIdentifier("textures/gui/two_book.png")

    private val bgLeft: Int get() = (this.width - 132) / 2
    private val bgTop: Int get() = 2
    private val controlsTop: Int get() = bgTop + 194

    override fun init() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE) { _ -> this.onClose() }
            .pos((this.width - 200) / 2, controlsTop).width(200)
            .build()
        )
    }

    override fun renderBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, a: Float) {
        super.renderBackground(graphics, mouseX, mouseY, a)
        graphics.blit(RenderPipelines.GUI_TEXTURED, bookLocation, bgLeft, bgTop, 0.0f, 0.0f, 192, 192, 256, 256)
    }
}