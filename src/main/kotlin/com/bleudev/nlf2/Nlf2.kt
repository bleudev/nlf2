package com.bleudev.nlf2

import com.bleudev.nlf2.custom.Nlf2Packets
import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData
import com.bleudev.nlf2.custom.packet.payload.WorldBorderNoisePayload
import com.bleudev.nlf2.util.sendToEveryone
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MODID = "nlf2"
fun getIdentifier(path: String) = Identifier.fromNamespaceAndPath(MODID, path)
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

class Nlf2 : ModInitializer {
    override fun onInitialize() {
        Nlf2Packets.initialize()

        ServerPlayerEvents.JOIN.register { player ->
            Nlf2PlayersData.get(player.level().server).trySetDefault(player.gameProfile.name)
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.sendToEveryone { player -> arrayOf(
                WorldBorderNoisePayload(player.level().worldBorder.getDistanceToBorder(player))
            ) }
        }
    }
}
