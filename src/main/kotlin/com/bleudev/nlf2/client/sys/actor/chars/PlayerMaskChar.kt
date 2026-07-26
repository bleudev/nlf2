package com.bleudev.nlf2.client.sys.actor.chars

import com.bleudev.nlf2.SLIM_SKINS
import com.bleudev.nlf2.client.util.TIMER_MASK
import net.minecraft.world.entity.player.PlayerModelType
import kotlin.random.Random

open class PlayerMaskChar(private val player: String) : ActorChar {
    override fun skin(client: String): String? {
        val t = TIMER_MASK.tick()
        val dur = Random.nextInt(4000, 30000)
        if (t % dur <= 30) return "entity/player/skin/vessel/$player"
        return "entity/player/skin/normal/$player"
    }
    override fun skinModel(client: String): PlayerModelType =
        if (player in SLIM_SKINS) PlayerModelType.SLIM else PlayerModelType.WIDE
}