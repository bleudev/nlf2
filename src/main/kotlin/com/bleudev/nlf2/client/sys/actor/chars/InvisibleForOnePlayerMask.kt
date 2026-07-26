package com.bleudev.nlf2.client.sys.actor.chars

class InvisibleForOnePlayerMask(name: String) : VisibleOnlyForChar(name, true) {
    override val skinTexture: String get() = "entity/player/skin/normal/$player"
}