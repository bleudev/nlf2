package com.bleudev.nlf2.client.sys.util

import com.bleudev.nlf2.sys.util.TextBoxSequence
import net.minecraft.network.chat.Component

class ComputerTextBoxSequence(lines: List<TextBoxInfo> = listOf()): TextBoxSequence(lines) {
    private var internalTick: Int = -1
    fun tick(new: Int) { internalTick = new }

    fun get(): List<Component> {
        for ((text, speed, start, end, color, full) in lines) {
            if (internalTick in start..<end) {
                val r = (if (full) text.length else (internalTick - start) / speed).coerceIn(0, text.length)
                val res = text.substring(0, r)
                return res.split("&").map { Component.literal(it).withColor(color) }
            }
        }
        return listOf()
    }
}