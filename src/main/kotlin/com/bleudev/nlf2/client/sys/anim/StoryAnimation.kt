package com.bleudev.nlf2.client.sys.anim

import com.bleudev.nlf2.client.shouldRenderStory
import com.bleudev.nlf2.client.util.TICKER_STORY
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.util.ARGB
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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
        val appear1 = seq.start
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

        var rouge = 1f
        var bleu = 1f
        var vert = 1f
        var rougeOutline = 1f
        var bleuOutline = 1f
        var vertOutline = 1f

        if (t <= appear1 + 80) {
            rouge = ((t - appear1).toFloat() / 40).coerceIn(0f, 1f)
            rougeOutline = rouge
            bleu = ((t - appear1 - 20).toFloat() / 40).coerceIn(0f, 1f)
            bleuOutline = bleu
            vert = ((t - appear1 - 40).toFloat() / 40).coerceIn(0f, 1f)
            vertOutline = vert
        }

        val rougeAngle = ((t % 60).toFloat() / 60) * 2 * PI
        val bleuAngle = (((t + 10) % 60).toFloat() / 60) * 2 * PI
        val vertAngle = (((t + 15) % 60).toFloat() / 60) * 2 * PI

        graphics.pose().pushMatrix()
        graphics.pose().translate(120f, 60f)
        graphics.pose().scale(3.5f)

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 0), t.circley(60, 0))
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, rougeOutline, 0xff0000, 0, 0)
        graphics.anim(Sprite.VESSEL_UNI, rouge, 0xff0000, 0, 0)
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 10), t.circley(60, 10))
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, bleuOutline, 0x3622ff, 20, -5)
        graphics.anim(Sprite.VESSEL_UNI, bleu, 0x3622ff, 20, -5)
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 20), t.circley(60, 20))
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, vertOutline, 0x00c100, 40, 0)
        graphics.anim(Sprite.VESSEL_UNI, vert, 0x00c100, 40, 0)
        graphics.pose().popMatrix()

        graphics.pose().popMatrix()

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


    private fun Int.circlex(duration: Int, offset: Int): Float {
        val angle = (((this + offset) % duration).toFloat() / duration) * 2 * PI
        return cos(angle).toFloat() * .5f
    }
    private fun Int.circley(duration: Int, offset: Int): Float {
        val angle = (((this + offset) % duration).toFloat() / duration) * 2 * PI
        return sin(angle).toFloat() * .5f
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

    private fun GuiGraphicsExtractor.anim(sprite: Sprite, alpha: Float, rgb: Int, x: Int, y: Int, size: Float = 1f) {
        val w = (sprite.width * size).toInt()
        val h = (sprite.height * size).toInt()
        blit(
            RenderPipelines.GUI_TEXTURED,
            getIdentifier("textures/anim/story/${sprite.filename}.png"),
            x, y, 0f, 0f,
            w, h, w, h,
            ARGB.color(alpha, rgb),
        )
    }

    private enum class Sprite(val filename: String, val width: Int, val height: Int) {
        SOUL_UNI("soul_uni", 9, 9),
        VESSEL_OUTLINE_UNI("vessel_outline_uni", 11, 20),
        VESSEL_UNI("vessel_uni", 11, 20),
    }
}