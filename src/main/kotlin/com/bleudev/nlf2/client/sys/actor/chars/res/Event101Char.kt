package com.bleudev.nlf2.client.sys.actor.chars.res

import com.bleudev.nlf2.client.sys.actor.chars.ActorChar

class Event101Char : ActorChar {
    override fun eyes(client: String): String = "entity/player/eyes/event101"
    override fun overlay(client: String): String = "entity/player/event101"
}