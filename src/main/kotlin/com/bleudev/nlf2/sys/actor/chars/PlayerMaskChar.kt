package com.bleudev.nlf2.sys.actor.chars

import com.bleudev.nlf2.SLIM_SKINS
import com.bleudev.nlf2.client.util.TIMER_MASK
import net.minecraft.world.entity.player.PlayerModelType
import kotlin.random.Random

open class PlayerMaskChar(private val player: String) : ActorChar {
    private var masked: Boolean = true

    override fun skin(client: String): String? {
        val t = TIMER_MASK.tick()
        val dur = Random.nextInt(4000, 30000)
        masked = t % dur > 30
        return if (masked) "entity/player/skin/normal/$player" else "entity/player/skin/vessel/$player"
    }
    override fun skinModel(client: String): PlayerModelType =
        if (player in SLIM_SKINS) PlayerModelType.SLIM else PlayerModelType.WIDE
    override val hideArmor: Boolean get() = !masked
    override val renderHitboxes: Boolean get() = masked
}