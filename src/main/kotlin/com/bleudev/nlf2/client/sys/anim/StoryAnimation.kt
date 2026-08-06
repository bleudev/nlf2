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
        seq.add("Давным-Давно...", 20)
        seq.add("В Мире жили&ТРИ различных Личности", 10)
        seq.add("ТРИ различных Образа&ТРИ различных Характера", 20)
        seq.speed(5)
        seq.add("ТРИ...   &СОСУДА", 20)
        seq.wait(10)
        seq.speed(2)
        seq.add("Они жили в Мире,&В Гармонии.", 20)
        seq.add("И с годами&Меняясь местами...", 20)
        seq.add("ДУША переходила   &Из одного Сосуда&В другой", 30)
        seq.add("Так продолжалось&До Одного Момента...", 20)
        seq.add("Один из Сосудов&Покинул это Место", 20)
        seq.add("Навстречу Приключениям&Навстречу Новому Миру&Навстречу УСПЕХУ", 20)
        seq.add("Оставив Остальных &Навечно Вдвоём...", 20)
        seq.add("Но Один Сосуд&Не пал Духом", 20)
        seq.speed(5)
        seq.add("Нет...", 20)
        seq.speed(2)
        seq.add("Он стал&Властителем Всего Мира", 30)
        seq.add("И в порыве&Зависти и Гнева", 20)
        seq.add("Лишь Одна Цель&Его волновала...", 30)
        seq.speed(4)
        seq.add("МЕСТЬ", 30)
        seq.add("Ведь...", 30)
        seq.speed(2)
        seq.add("Почему&Именно&ОН?", 80)
        val vanya = seq.start - 80
        graphics.black(when (t) {
            in vanya..<vanya+20 -> .25f
            in vanya+20..<vanya+40 -> .5f
            in vanya+60..<vanya+80 -> .75f
            else -> 0f
        })
    }

    private class TextBoxSequence {
        private var internalTick: Int = -1
        private var internalExtractor: GuiGraphicsExtractor? = null
        private var internalSpeed: Int = 2
        fun tick(new: Int) { internalTick = new }
        fun extractor(new: GuiGraphicsExtractor) { internalExtractor = new }
        fun speed(new: Int) { internalSpeed = new }

        var start: Int = 0
            private set

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