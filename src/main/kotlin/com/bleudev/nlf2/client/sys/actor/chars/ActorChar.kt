package com.bleudev.nlf2.client.sys.actor.chars

interface ActorChar {
    fun tick() {}
    fun skin(client: String): String? = null
    fun eyes(client: String): String = "entity/player/empty"
    fun overlay(client: String): String = "entity/player/empty"

    val isInvisible: Boolean get() = false
}