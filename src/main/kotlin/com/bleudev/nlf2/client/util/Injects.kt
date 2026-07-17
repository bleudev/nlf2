package com.bleudev.nlf2.client.util

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nlf2.getIdentifier
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

fun GuiGraphicsExtractor.black(alpha: Float) = overlayWithColor(alpha, 0f, 0f, 0f)
fun GuiGraphicsExtractor.black() = black(1f)

enum class Animation(val id: String) {
    INTRO("intro"), NOISE("noise");

    fun getIdentifier(n: Int): Identifier = getIdentifier("textures/anim/$id/$n.png")
}
fun GuiGraphicsExtractor.blitAnim(animation: Animation, n: Int, x: Int = 0, y: Int = 0, width: Int = -1, height: Int = -1, color: Int = 0xffffffff.toInt()) {
    var w = width; var h = height
    if (w == -1) w = guiWidth()
    if (h == -1) h = guiHeight()
    blit(RenderPipelines.GUI_TEXTURED, animation.getIdentifier(n), x, y, 0f, 0f, w, h, w, h, color)
}

fun PostEffectRegistry.Companion.registerNlf2(info: Pair<String, EmptyPredicate>) = PostEffectRegistry.register(getIdentifier(info.first) to info.second)
