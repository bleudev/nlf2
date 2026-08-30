package com.bleudev.nlf2.client.sys.actor.chars.res

import com.bleudev.nlf2.client.sys.actor.chars.VesselLikeChar

class RougeBattleChar : VesselLikeChar("rouge") {
    override val hideHandItems: Boolean get() = true
}