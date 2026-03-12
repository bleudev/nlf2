package com.bleudev.nlf2.client.custom.key.mapping

import com.bleudev.nlf2.CAN_USE_SECRET_EVENT_KEY_MAPPING_NICKNAME
import com.bleudev.nlf2.custom.packet.payload.serverbound.CauseEventWithKey
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft

class SecretEventKeyMapping : Nlf2KeySymKeyMapping("secret_event_key_mapping") {
    override fun onPress(mc: Minecraft) {
        ClientPlayNetworking.send(CauseEventWithKey(101))
    }

    override fun isVisible(mc: Minecraft): Boolean = mc.gameProfile.name == CAN_USE_SECRET_EVENT_KEY_MAPPING_NICKNAME
}