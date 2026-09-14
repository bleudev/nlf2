package com.bleudev.nlf2.sys.actor.chars.res

import com.bleudev.nlf2.client.unknownRainbowEffectColor
import com.bleudev.nlf2.sys.actor.chars.ActorChar

class UnknownChar : ActorChar {
    override fun skin(client: String): String = "entity/player/black"
    override val isForceNameTagVoiceDisconnected: Boolean get() = true
    override val customWaypointColor: Int get() = unknownRainbowEffectColor
}