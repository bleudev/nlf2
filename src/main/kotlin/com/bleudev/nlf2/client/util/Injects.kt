package com.bleudev.nlf2.client.util

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

fun Player.holdsInAnyHand(predicate: (ItemStack) -> Boolean): Boolean = (predicate(this.mainHandItem) || predicate(this.offhandItem))
fun Player.holdsInAnyHand(item: Item): Boolean = this.holdsInAnyHand { it.item == item }

fun GuiGraphics.overlay(color: Int) = fill(0, 0, guiWidth(), guiHeight(), color)
fun GuiGraphics.black(alpha: Float) = overlay(ARGB.colorFromFloat(alpha, 0f, 0f, 0f))
fun GuiGraphics.black() = black(1f)

enum class Animation(val id: String) {
    INTRO("intro"), NOISE("noise");

    fun getIdentifier(n: Int): Identifier = com.bleudev.nlf2.getIdentifier("textures/anim/$id/$n.png")
}
fun GuiGraphics.blitAnim(animation: Animation, n: Int, x: Int = 0, y: Int = 0, width: Int = -1, height: Int = -1, color: Int = 0xffffffff.toInt()) {
    var w = width; var h = height
    if (w == -1) w = guiWidth()
    if (h == -1) h = guiHeight()
    blit(RenderPipelines.GUI_TEXTURED, animation.getIdentifier(n), x, y, 0f, 0f, w, h, w, h, color)
}