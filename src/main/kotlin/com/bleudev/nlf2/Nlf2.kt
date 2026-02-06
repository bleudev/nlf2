package com.bleudev.nlf2

import com.bleudev.nlf2.custom.data.saved.Nlf2PlayersData
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
        ServerPlayerEvents.JOIN.register { player ->
            Nlf2PlayersData.get(player.level().server).trySetDefault(player.gameProfile.name)
        }
        ServerTickEvents.END_SERVER_TICK.register { s ->
//            s.playerList.broadcastSystemMessage()
        }
    }
}
