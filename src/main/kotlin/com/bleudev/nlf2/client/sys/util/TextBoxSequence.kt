package com.bleudev.nlf2.client.sys.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.min

class TextBoxSequence {
    private data class TextBoxInfo(val curr: Int, val text: String, val speed: Int, val start: Int, val end: Int)

    private val lines: ArrayList<TextBoxInfo> = ArrayList()

    private var internalTick: Int = -1
    private var internalSpeed: Int = 2
    fun tick(new: Int) { internalTick = new }
    fun speed(new: Int) { internalSpeed = new }

    var start: Int = 0
        private set

    fun add(text: String, wait: Int) {
        val end = start + text.replace("&", "").length * internalSpeed + wait
        lines.add(TextBoxInfo(internalTick, text, internalSpeed, start, end))
        start = end
    }
    fun wait(duration: Int) {
        start += duration
    }
    fun sub(delay: Int): TextBoxSequence {
        val mega = TextBoxSequence()
        mega.tick(internalTick)
        mega.speed(internalSpeed)
        mega.wait(start + delay)
        return mega
    }

    fun extractRenderState(guiGraphicsExtractor: GuiGraphicsExtractor) {
        for ((curr, text, speed, start, end) in lines) {
            guiGraphicsExtractor.textbox(curr, text, speed, start, end)
        }
    }
}

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
