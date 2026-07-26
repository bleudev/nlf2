package com.bleudev.nlf2.client.sys.actor.chars.res

import com.bleudev.nlf2.client.sys.actor.chars.ActorChar
import com.bleudev.nlf2.client.sys.actor.chars.VisibleOnlyForChar

class RougeEyesBleugame : VisibleOnlyForChar("bleugame") {
    override val skinTexture: String get() = ActorChar.EMPTY_TEXTURE
    override val eyesTexture: String get() = "entity/player/eyes/rouge/bleugame"
    override val isInvisible: Boolean get() = true
}