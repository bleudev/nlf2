package com.bleudev.nlf2.client.custom.key.mapping

import com.bleudev.nlf2.getIdentifier
import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

abstract class Nlf2KeyMapping(name: String, type: InputConstants.Type, value: Int = -1, category: Category = Nlf2KeyMappingCategories.GENERAL, order: Int = 0) : KeyMapping("key.nlf2.$name", type, value, category, order) {
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
        @JvmStatic
        internal fun shouldRender(key: KeyMapping): Boolean {
            for (reg in KEY_MAPPINGS) {
                if (key.same(reg)) {
                    return reg.isVisible(Minecraft.getInstance())
                }
            }
            return true;
        }
        @JvmStatic
        internal fun shouldRenderCategory(category: Category): Boolean {
            val mc = Minecraft.getInstance()
            for (key in mc.options.keyMappings) {
                if (key.category.id == category.id) {
                    var vis = true
                    for (reg in KEY_MAPPINGS) {
                        if (key.same(reg)) {
                            vis = reg.isVisible(mc)
                            break
                        }
                    }
                    if (vis) return true
                }
            }
            return false
        }
    }
}

abstract class Nlf2KeySymKeyMapping(name: String, value: Int = -1, category: Category = Nlf2KeyMappingCategories.GENERAL, order: Int = 0) : Nlf2KeyMapping(name, InputConstants.Type.KEYSYM, value, category, order)

object Nlf2KeyMappingCategories {
    val GENERAL = KeyMapping.Category.register(getIdentifier("general"))

    operator fun iterator(): Iterator<KeyMapping.Category> = listOf(GENERAL).iterator()
}