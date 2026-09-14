package com.bleudev.nlf2.sys.actor.chars.res

import com.bleudev.nlf2.sys.actor.chars.VesselLikeChar

class GhostChar : VesselLikeChar(null) {
    override val isInvisible: Boolean get() = true
}