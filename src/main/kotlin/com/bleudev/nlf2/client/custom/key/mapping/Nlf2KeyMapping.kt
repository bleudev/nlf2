package com.bleudev.nlf2.client.custom.key.mapping

import com.bleudev.nlf2.getIdentifier
import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

abstract class Nlf2KeyMapping(name: String, type: InputConstants.Type, value: Int = -1, category: Category = Nlf2KeyMappingCategories.GENERAL, order: Int = 0) : KeyMapping(name, type, value, category, order) {
    abstract fun onPress(mc: Minecraft)

    protected open fun isVisible(mc: Minecraft): Boolean = true

    companion object {
        private val KEY_MAPPINGS = ArrayList<Nlf2KeyMapping>()
        fun register(keyMapping: Nlf2KeyMapping) {
            KeyMappingHelper.registerKeyMapping(keyMapping)
            KEY_MAPPINGS.add(keyMapping)
        }
        internal fun handle(mc: Minecraft) {
            for (k in KEY_MAPPINGS) {
                while (k.consumeClick()) { k.onPress(mc) }
            }
        }
    }
}

abstract class Nlf2KeySymKeyMapping(name: String, value: Int = -1, category: Category = Nlf2KeyMappingCategories.GENERAL, order: Int = 0) : Nlf2KeyMapping(name, InputConstants.Type.KEYSYM, value, category, order)

object Nlf2KeyMappingCategories {
    val GENERAL = KeyMapping.Category.register(getIdentifier("general"))
}