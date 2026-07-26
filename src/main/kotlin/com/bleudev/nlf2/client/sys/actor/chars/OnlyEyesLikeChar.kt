package com.bleudev.nlf2.client.sys.actor.chars

open class OnlyEyesLikeChar(protected val char: String?) : ActorChar {
    override fun skin(client: String): String? = ActorChar.EMPTY_TEXTURE
    override fun eyes(client: String): String {
        if (char == null) return super.eyes(client)
        return "entity/player/eyes/$char/$client"
    }
}