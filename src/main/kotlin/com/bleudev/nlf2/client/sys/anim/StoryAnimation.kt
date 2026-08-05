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

        val seq1 = TextBoxSequence()
        seq1.tick(t)
        seq1.extractor(graphics)
        seq1.speed(4)
        seq1.add("Давным-Давно...", 20)
        seq1.speed(3)
        seq1.add("В Мире жили", 20)
        seq1.add("ТРИ различных Личности", 20)
        seq1.add("ТРИ различных Образа", 20)
        seq1.add("ТРИ различных Характера", 20)
        seq1.speed(7)
        val seq2 = seq1.sub(42)
        seq1.add("ТРИ...", 62)
        seq2.add("&СОСУДА", 20)
    }

    private class TextBoxSequence {
        private var internalTick: Int = -1
        private var internalExtractor: GuiGraphicsExtractor? = null
        private var internalSpeed: Int = 2
        fun tick(new: Int) { internalTick = new }
        fun extractor(new: GuiGraphicsExtractor) { internalExtractor = new }
        fun speed(new: Int) { internalSpeed = new }

        private var start: Int = 0

        fun add(text: String, wait: Int) {
            val end = start + text.replace("&", "").length * internalSpeed + wait
            internalExtractor!!.textbox(internalTick, text, internalSpeed, start, end)
            start = end
        }
        fun wait(duration: Int) {
            start += duration
        }
        fun sub(delay: Int): TextBoxSequence {
            val mega = TextBoxSequence()
            mega.tick(internalTick)
            mega.extractor(internalExtractor!!)
            mega.speed(internalSpeed)
            mega.wait(start + delay)
            return mega
        }
    }

    // Example: Hello, &world!
    private fun GuiGraphicsExtractor.textbox(curr: Int, text: String, speed: Int, start: Int, end: Int) {
        if (curr >= end) return
        pose().pushMatrix()
        pose().translate(90f, 200f)
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