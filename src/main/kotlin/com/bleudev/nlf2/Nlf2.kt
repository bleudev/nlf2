package com.bleudev.nlf2

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MODID = "nlf2"
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    override fun onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register { s ->
//            s.playerList.broadcastSystemMessage()
        }
    }
}
