package com.bleudev.nlf2.sys.actor.chars

import com.bleudev.nlf2.SLIM_SKINS
import net.minecraft.world.entity.player.PlayerModelType

open class VesselLikeChar(protected val char: String?) : ActorChar {
    override fun skin(client: String): String? {
        return "entity/player/skin/vessel/$client"
    }
    override fun skinModel(client: String): PlayerModelType =
        if (client in SLIM_SKINS) PlayerModelType.SLIM else PlayerModelType.WIDE
    override fun eyes(client: String): String {
        if (char == null) return super.eyes(client)
        return "entity/player/eyes/$char/$client"
    }
}