package com.bleudev.nlf2.client.sys.actor.chars

open class PlayerMaskChar(private val player: String) : ActorChar {
    override fun skin(client: String): String? {
        return "entity/player/skin/normal/$player"
    }
}