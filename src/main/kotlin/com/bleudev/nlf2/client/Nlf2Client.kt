package com.bleudev.nlf2.client

import com.bleudev.nlf2.client.custom.Nlf2Sounds
import net.fabricmc.api.ClientModInitializer

class Nlf2Client : ClientModInitializer {

    override fun onInitializeClient() {
        Nlf2Sounds.initialize()
    }
}
