package com.bleudev.nlf2.client.sys.actor.chars.res

import com.bleudev.nlf2.client.sys.actor.chars.ActorChar
import com.bleudev.nlf2.client.sys.actor.chars.VisibleOnlyForChar

class RougeEyesBleugame : VisibleOnlyForChar("bleugame") {
    override val skinOverride: String get() = ActorChar.EMPTY_TEXTURE
    override val eyesOverride: String get() = "entity/player/eyes/rouge/bleugame"
    override val isInvisible: Boolean get() = true
}