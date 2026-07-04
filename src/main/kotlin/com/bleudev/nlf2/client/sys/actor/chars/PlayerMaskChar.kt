package com.bleudev.nlf2.client.sys.actor.chars

import com.bleudev.nlf2.client.util.TIMER_MASK
import kotlin.random.Random

open class PlayerMaskChar(private val player: String) : ActorChar {
    override fun skin(client: String): String? {
        val t = TIMER_MASK.tick()
        println(t)
        val dur = Random.nextInt(4000, 30000)
        if (t % dur <= 30) return "entity/player/skin/vessel/$player"
        return "entity/player/skin/normal/$player"
    }
}