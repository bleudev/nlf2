package com.bleudev.nlf2.client.sys.actor.chars

interface ActorChar {
    fun tick() {}
    fun skin(client: String): String? = null
    fun eyes(client: String): String = EMPTY_TEXTURE
    fun overlay(client: String): String = EMPTY_TEXTURE

    val isInvisible: Boolean get() = false

    companion object {
        const val EMPTY_TEXTURE = "entity/player/empty"
    }
}