package com.bleudev.nlf2.client.custom

import com.bleudev.nlf2.client.custom.key.mapping.Nlf2KeyMapping
import com.bleudev.nlf2.client.custom.key.mapping.SecretEventKeyMapping
import net.minecraft.client.Minecraft

object Nlf2KeyMappings {
    internal fun initialize() {
        Nlf2KeyMapping.register(SecretEventKeyMapping())
    }
    internal fun handle(mc: Minecraft) {
        Nlf2KeyMapping.handle(mc)
    }
}