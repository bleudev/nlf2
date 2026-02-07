package com.bleudev.nlf2.client

import com.bleudev.nlf2.MODID
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.terraformersmc.modmenu.config.ModMenuConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

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
    }
}
