package com.bleudev.nlf2.client.sys.anim

import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nlf2.client.shouldRenderStory
import com.bleudev.nlf2.client.util.TICKER_STORY
import com.bleudev.nlf2.client.util.black
import net.minecraft.client.gui.GuiGraphicsExtractor

object StoryAnimation {
    fun render(graphics: GuiGraphicsExtractor) {
        // DEBUG
        if (!shouldRenderStory) return
        val t = TICKER_STORY.tick()
        graphics.black()
        if (t < 100) graphics.overlayWithColor(.5f, 1f, 0f, 0f)
    }
}