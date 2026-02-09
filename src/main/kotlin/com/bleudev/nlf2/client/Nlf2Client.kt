package com.bleudev.nlf2.client

import com.bleudev.nlf2.MODID
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.util.black
import com.bleudev.nlf2.getIdentifier
import com.terraformersmc.modmenu.config.ModMenuConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.resources.Identifier

class Nlf2Client : ClientModInitializer {

    override fun onInitializeClient() {
        Nlf2Sounds.initialize()
        ModMenuConfig.HIDDEN_MODS.value.let {
            if (MODID !in it) {
                it.add(MODID)
                ModMenuConfig.HIDDEN_MODS.value = it
            }
        }

        ClientTickEvents.END_CLIENT_TICK.register {
            it.window.setTitle(if (webcamEnabled) "EYES ARE WATCHING" else "\"Minecraft\" 26.1")
        }
        ClientTickEvents.END_LEVEL_TICK.register {
            join_blackness_delay_ticks = (join_blackness_delay_ticks - 1).coerceAtLeast(0)
            if (join_blackness_delay_ticks == 0)
                join_blackness = (join_blackness - .05f).coerceAtLeast(0f)
        }

        HudElementRegistry.addLast(Layer.OVERLAY) { graphics, _ ->
            graphics.black(join_blackness)
        }
    }

    object Layer {
        val OVERLAY: Identifier = getIdentifier("layer/overlay")
    }
}
