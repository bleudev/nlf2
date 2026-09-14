package com.bleudev.nlf2.sys.actor.chars

import com.bleudev.nlf2.SLIM_SKINS
import net.minecraft.world.entity.player.PlayerModelType

class InvisibleForOnePlayerMask(name: String) : VisibleOnlyForChar(name, true) {
    override val skinOverride: String get() = "entity/player/skin/normal/$player"
    override val skinModelOverride: PlayerModelType =
        if (player in SLIM_SKINS) PlayerModelType.SLIM else PlayerModelType.WIDE
}