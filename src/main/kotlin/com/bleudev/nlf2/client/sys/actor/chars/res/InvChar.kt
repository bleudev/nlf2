package com.bleudev.nlf2.client.sys.actor.chars.res

import com.bleudev.nlf2.client.sys.actor.chars.ActorChar

class InvChar : ActorChar {
    override fun skin(client: String) = ActorChar.EMPTY_TEXTURE
    override val isInvisible: Boolean get() = true
}