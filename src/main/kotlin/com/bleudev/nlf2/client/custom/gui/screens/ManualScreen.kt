package com.bleudev.nlf2.client.custom.gui.screens

import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.PageButton
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ManualScreen(val manualId: ManualId, val playTurnSound: Boolean = true) : Screen(Component.literal("Manunal!")) {
    constructor(manualId: Int, playTurnSound: Boolean = true) : this(ManualId.fromId(manualId), playTurnSound)

    private val bookLocation = getIdentifier("textures/gui/two_book.png")
    private val maxPageId = manualId.pages.size - 1

    private val bgLeft: Int get() = (this.width - 132) / 2 - 66
    private val bgTop: Int get() = 2
    private val controlsTop: Int get() = bgTop + 194

    private var currentPageId: Int = 0
    private val currentPage: Int get() = manualId.pages[currentPageId]

    private lateinit var forwardButton: PageButton
    private lateinit var backButton: PageButton

    override fun init() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE) { _ -> this.onClose() }
            .pos((this.width - 200) / 2, controlsTop).width(200)
            .build()
        )

        this.forwardButton = this.addRenderableWidget(PageButton(
            bgLeft + 222, bgTop + 157, true,
            { this.pageForward() }, this.playTurnSound))
        this.backButton = this.addRenderableWidget(PageButton(
            bgLeft + 18, bgTop + 157, false,
            { this.pageBack() }, this.playTurnSound))
        updateButtonVisibility()
    }

    override fun renderBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, a: Float) {
        super.renderBackground(graphics, mouseX, mouseY, a)
        graphics.blit(RenderPipelines.GUI_TEXTURED, bookLocation, bgLeft, bgTop, 0.0f, 0.0f, 264, 256, 264, 256)
        graphics.blit(RenderPipelines.GUI_TEXTURED, getIdentifier("textures/gui/manual/$manualLanguageCode/manual_$currentPage.png"), bgLeft, bgTop, 0.0f, 0.0f, 264, 256, 264, 256)
    }

    private fun pageForward() {
        if (this.currentPageId < maxPageId)
            this.currentPageId++
        updateButtonVisibility()
    }
    private fun pageBack() {
        (this.currentPageId > 0)
            this.currentPageId--
        updateButtonVisibility()
    }
    private fun updateButtonVisibility() {
        this.forwardButton.visible = this.currentPageId < maxPageId
        this.backButton.visible = this.currentPageId > 0
    }

    private val manualLanguageCode: String get() {
        val lang = this.minecraft.options.languageCode
        val supported = listOf("ru_ru")
        return if (lang in supported) lang else "default"
    }
}

enum class ManualId(val id: Int, val pages: List<Int>) {
    BEFORE_STICK(0, listOf(0, 1)),
    AFTER_DEBUG_STICK(1, listOf(0, 1, 2)),
    AFTER_AMETHYST_STICK(2, listOf(0, 1, 3));

    companion object {
        fun fromId(id: Int): ManualId = when (id) {
            0 -> BEFORE_STICK
            1 -> AFTER_DEBUG_STICK
            2 -> AFTER_AMETHYST_STICK
            else -> throw MatchException(null, null)
        }
    }
}