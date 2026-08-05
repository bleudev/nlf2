package com.bleudev.nlf2.client.sys.anim

import com.bleudev.nlf2.client.shouldRenderStory
import com.bleudev.nlf2.client.util.TICKER_STORY
import com.bleudev.nlf2.client.util.black
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.min

object StoryAnimation {
    fun render(graphics: GuiGraphicsExtractor) {
        // DEBUG
        if (!shouldRenderStory) return
        val t = TICKER_STORY.tick().toInt()
        graphics.black()

        val seq = TextBoxSequence()
        seq.tick(t)
        seq.extractor(graphics)
        seq.speed(2)
        seq.add("Давным-давно в мире жили", 100)
        seq.add("Три различных личности", 95)
        seq.add("Три различных образа", 90)
        seq.add("Три различных характеров", 100)
        seq.speed(6)
        seq.add("Три СОСУДА....", 150)
    }

    private class TextBoxSequence {
        private var internalTick: Int = -1
        private var internalExtractor: GuiGraphicsExtractor? = null
        private var internalSpeed: Int = 2
        fun tick(new: Int) { internalTick = new }
        fun extractor(new: GuiGraphicsExtractor) { internalExtractor = new }
        fun speed(new: Int) { internalSpeed = new }

        private var start: Int = 0

        fun add(text: String, duration: Int) {
            internalExtractor!!.textbox(internalTick, text, internalSpeed, start, start + duration)
            start += duration
        }
        fun wait(duration: Int) {
            start += duration
        }
    }

    // Example: Hello, &world!
    private fun GuiGraphicsExtractor.textbox(curr: Int, text: String, speed: Int, start: Int, end: Int) {
        if (curr >= end) return
        pose().pushMatrix()
        pose().translate(80f, 200f)
        pose().scale(1.7f)

        var remain = (curr - start) / speed
        val font = Minecraft.getInstance().font
        val split = text.split("&")
        for (i in split.indices) {
            val line = split[i]
            if (remain > 0) {
                val render = line.substring(0, min(remain, line.length))
                text(font, render, 0, 10 * i, 0xffffffff.toInt(), false)
                remain -= render.length
            } else break
        }

        pose().popMatrix()
    }
}