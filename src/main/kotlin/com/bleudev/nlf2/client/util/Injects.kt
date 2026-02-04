package com.bleudev.nlf2.client.util

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

fun Player.holdsInAnyHand(predicate: (ItemStack) -> Boolean): Boolean = (predicate(this.mainHandItem) || predicate(this.offhandItem))
fun Player.holdsInAnyHand(item: Item): Boolean = this.holdsInAnyHand { it.item == item }