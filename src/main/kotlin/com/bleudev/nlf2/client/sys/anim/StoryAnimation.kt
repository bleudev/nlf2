package com.bleudev.nlf2.client.sys.anim

import com.bleudev.nlf2.client.shouldRenderStory
import com.bleudev.nlf2.client.util.TICKER_STORY
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.reverseDelta
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
        val soul1 = seq.start
        seq.add("И с годами&Меняясь местами...", 20)
        val soul2 = seq.start
        seq.add("ДУША переходила   &Из одного Сосуда&В другой", 30)
        seq.add("Так продолжалось&До Одного Момента...", 20)
        val soul3 = seq.start
        seq.add("Один из Сосудов&Покинул это Место", 20)
        val soul4 = seq.start
        seq.add("Навстречу Приключениям&Навстречу Новому Миру&Навстречу УСПЕХУ", 20)
        val soul5 = seq.start
        seq.add("Оставив Остальных &Навечно Вдвоём...", 20)
        val soul6 = seq.start
        seq.add("Но Один Сосуд&Не пал Духом", 20)
        seq.speed(5)
        seq.add("Нет...", 20)
        seq.speed(2)
        val ruler1 = seq.start
        seq.add("Он стал&Властителем Всего Мира", 30)
        val ruler2 = seq.start
        seq.add("И в порыве&Зависти и Гнева", 10)
        seq.add("Лишь Одна Цель&Его волновала...", 20)
        seq.speed(3)
        seq.add("МЕСТЬ", 30)
        seq.add("Ведь...", 10)
        seq.speed(2)
        seq.add("Почему&Именно&ОН?", 60)
        val end = seq.start - 60

        var rouge = 1f
        var bleu = 1f
        var vert = 1f
        var rougeOutline = 1f
        var bleuOutline = 1f
        var vertOutline = 1f
        var rougeSoul = 0f
        var bleuSoul = 0f
        var vertSoul = 0f
        var rougeInCenter = false
        var rougeCrown = 1f
        var rougeGradient = 0f

        if (t <= appear1 + 80) {
            rouge = ((t - appear1).toFloat() / 40).coerceIn(0f, 1f)
            rougeOutline = rouge
            bleu = ((t - appear1 - 20).toFloat() / 40).coerceIn(0f, 1f)
            bleuOutline = bleu
            vert = ((t - appear1 - 40).toFloat() / 40).coerceIn(0f, 1f)
            vertOutline = vert
        }
        if (t in soul1..soul2) {
            rouge = ((t - soul1).toFloat() / 20).coerceIn(0f, 1f).reverseDelta() / 2 + .5f
            bleu = rouge
            vert = rouge
        }
        val soulDeltas = ((t - soul2).toFloat() / 60).hueToTriple()
        val preSoulDelta = when (t) {
            in soul2..soul2+30 -> ((t - soul2).toFloat() / 30).coerceIn(0f, 1f)
            in soul3-30..soul3 -> ((t - soul3 + 30).toFloat() / 30).coerceIn(0f, 1f).reverseDelta()
            else -> 1f
        }
        if (t in soul2..soul3) {
            rouge = .5f
            rougeSoul = soulDeltas.first * preSoulDelta
            bleu = .5f
            bleuSoul = soulDeltas.second * preSoulDelta
            vert = .5f
            vertSoul = soulDeltas.third * preSoulDelta

            if (t in soul3-30..soul3) {
                rouge = preSoulDelta.reverseDelta() / 2 + .5f
                bleu = rouge
                vert = rouge
            }
        }
        if (t in soul3..soul4) {
            bleuSoul = 1f
            bleu = ((t - soul3).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
            bleuOutline = bleu
        }
        if (t in soul4..soul5) {
            bleuSoul = ((t - soul4).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
        }
        if (t >= soul4) {
            bleu = 0f
            bleuOutline = 0f
        }
        if (t in soul5..soul6) {
            rouge = ((t - soul5).toFloat() / 60).coerceIn(0f, 1f).reverseDelta()
            rougeOutline = rouge
            vert = rouge
            vertOutline = rouge
        }
        if (t >= soul6) {
            // TODO: Sound on rouge appear
            rougeInCenter = true
            rouge = 1f
            rougeOutline = 1f
            vert = 0f
            vertOutline = 0f
        }
        if (t <= ruler1) {
            rougeCrown = 0f
        }
        if (t in ruler1..ruler2) {
            rougeCrown = ((t - ruler1).toFloat() / 60).coerceIn(0f, 1f)
        }
        if (t >= ruler2) {
            rougeGradient = ((t - ruler2).toFloat() / 100).coerceIn(0f, 1f)
        }

        graphics.pose().pushMatrix()
        graphics.pose().translate(120f, 60f)
        graphics.pose().scale(3.5f)

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 0), t.circley(60, 0))
        if (!rougeInCenter) {
            graphics.anim(Sprite.VESSEL_OUTLINE_UNI, rougeOutline, 0xff0000, 0f, 0f)
            graphics.anim(Sprite.VESSEL_UNI, rouge, 0xff0000, 0f, 0f)
            graphics.anim(Sprite.SOUL_UNI, rougeSoul, 0xff0000, 3.5f, 6.5f, .5f)
        }
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        if (!rougeInCenter) {
            graphics.pose().translate(t.circlex(60, 10), t.circley(60, 10))
        }
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, bleuOutline, 0x3622ff, 20f, -5f)
        graphics.anim(Sprite.VESSEL_UNI, bleu, 0x3622ff, 20f, -5f)
        graphics.anim(Sprite.SOUL_UNI, bleuSoul, 0x3622ff, 23.5f, 1.5f, .5f)
        if (rougeInCenter) {
            graphics.anim(Sprite.CROWN_UNI, rougeCrown, 0xff0000, 20f, -8.25f, .5f)
            graphics.anim(Sprite.VESSEL_OUTLINE_UNI, rougeOutline, 0xff0000, 20f, -5f)
            graphics.anim(Sprite.VESSEL_UNI, rouge, 0xff0000, 20f, -5f)
            graphics.anim(Sprite.SOUL_UNI, rougeSoul, 0xff0000, 23.5f, 1.5f, .5f)
        }
        graphics.pose().popMatrix()

        graphics.pose().pushMatrix()
        graphics.pose().translate(t.circlex(60, 20), t.circley(60, 20))
        graphics.anim(Sprite.VESSEL_OUTLINE_UNI, vertOutline, 0x00c100, 40f, 0f)
        graphics.anim(Sprite.VESSEL_UNI, vert, 0x00c100, 40f, 0f)
        graphics.anim(Sprite.SOUL_UNI, vertSoul, 0x00c100, 43.5f, 6.5f, .5f)
        graphics.pose().popMatrix()

        graphics.pose().popMatrix()

        graphics.fillGradient(0, 0, graphics.guiWidth(), graphics.guiHeight(), 0, ARGB.color(rougeGradient * .2f, 0xff0000))

        graphics.black(when (t) {
            in end..<end+20 -> .25f
            in end+20..<end+40 -> .5f
            in end+40..<end+60 -> .75f
            in end+60..end+8000 -> 1f // idk how to do that normal way
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
        return cos(angle).toFloat() * .3f
    }
    private fun Int.circley(duration: Int, offset: Int): Float {
        val angle = (((this + offset) % duration).toFloat() / duration) * 2 * PI
        return sin(angle).toFloat() * .3f
    }

    fun Float.hueToTriple(): Triple<Float, Float, Float> {
        var h = this % 1f
        if (h < 0) h += 1f

        val rgb = FloatArray(3)
        val offsets = floatArrayOf(0f, 4f, 2f)

        for (i in offsets.indices) {
            val value = (h * 6f + offsets[i]) % 6f
            val result = kotlin.math.abs(value - 3f) - 1f
            rgb[i] = result.coerceIn(0f, 1f)
        }

        return Triple(rgb[0], rgb[1], rgb[2])
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

    private fun GuiGraphicsExtractor.anim(sprite: Sprite, alpha: Float, rgb: Int, x: Float, y: Float, size: Float = 1f) {
        pose().pushMatrix()
        pose().translate(x, y)
        pose().scale(size)
        blit(
            RenderPipelines.GUI_TEXTURED,
            getIdentifier("textures/anim/story/${sprite.filename}.png"),
            0, 0, 0f, 0f,
            sprite.width, sprite.height, sprite.width, sprite.height,
            ARGB.color(alpha, rgb),
        )
        pose().popMatrix()
    }

    private enum class Sprite(val filename: String, val width: Int, val height: Int) {
        CROWN_UNI("crown_uni", 16, 6),
        SOUL_UNI("soul_uni", 9, 9),
        VESSEL_OUTLINE_UNI("vessel_outline_uni", 11, 20),
        VESSEL_UNI("vessel_uni", 11, 20),
    }
}