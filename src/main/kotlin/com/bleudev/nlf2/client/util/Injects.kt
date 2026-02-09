package com.bleudev.nlf2.client.util

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.ARGB
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

fun Player.holdsInAnyHand(predicate: (ItemStack) -> Boolean): Boolean = (predicate(this.mainHandItem) || predicate(this.offhandItem))
fun Player.holdsInAnyHand(item: Item): Boolean = this.holdsInAnyHand { it.item == item }

fun GuiGraphics.overlay(color: Int) = fill(0, 0, guiWidth(), guiHeight(), color)
fun GuiGraphics.black(alpha: Float) = overlay(ARGB.colorFromFloat(alpha, 0f, 0f, 0f))
fun GuiGraphics.black() = black(1f)