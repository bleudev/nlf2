@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client.sys.util

import com.bleudev.nlf2.ANIM_Y_REF
import com.bleudev.nlf2.sys.util.TextBoxSequence
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.util.ARGB
import kotlin.math.min

class GuiTextBoxSequence(lines: List<TextBoxInfo> = listOf()) : TextBoxSequence(lines) {
    constructor(sequence: TextBoxSequence) : this(sequence.extractLines())

    private var internalTick: Int = -1
    fun tick(new: Int) { internalTick = new }

    fun extractRenderState(guiGraphicsExtractor: GuiGraphicsExtractor, alpha: Float = 1f) {
        for ((text, speed, start, end, color, full) in lines) {
            guiGraphicsExtractor.textbox(internalTick, text, speed, start, end, ARGB.color(alpha, color), full)
        }
    }
}

private fun GuiGraphicsExtractor.textbox(curr: Int, text: String, speed: Int, start: Int, end: Int, color: Int, full: Boolean) {
    if (curr >= end) return
    if (curr < start) return
    pose().pushMatrix()
    pose().scale(guiHeight().toFloat() / ANIM_Y_REF)
    pose().translate(90f, 180f)
    pose().scale(1.7f)

    var remain = if (full) text.replace("&", "").length else (curr - start) / speed
    val font = Minecraft.getInstance().font
    val split = text.split("&")
    for (i in split.indices) {
        val line = split[i]
        if (remain > 0) {
            val render = line.substring(0, min(remain, line.length))
            text(font, render, 0, 10 * i, color, false)
            remain -= render.length
        } else break
    }

    pose().popMatrix()
}
