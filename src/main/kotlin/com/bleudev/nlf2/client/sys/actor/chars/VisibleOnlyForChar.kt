package com.bleudev.nlf2.client.sys.actor.chars

import net.minecraft.client.Minecraft

open class VisibleOnlyForChar(val player: String, val reverse: Boolean = false) : ActorChar {
    override fun skin(client: String): String? {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else skinTexture
        else if (val1) skinTexture else ActorChar.EMPTY_TEXTURE
    }
    override fun eyes(client: String): String {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else eyesTexture
        else if (val1) eyesTexture else ActorChar.EMPTY_TEXTURE
    }
    override fun overlay(client: String): String {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else overlayTexture
        else if (val1) overlayTexture else ActorChar.EMPTY_TEXTURE
    }

    open val skinTexture: String? get() = null
    open val eyesTexture: String get() = ActorChar.EMPTY_TEXTURE
    open val overlayTexture: String get() = ActorChar.EMPTY_TEXTURE
    override val isInvisible: Boolean get() = Minecraft.getInstance().gameProfile.name.equals(player, true)
        .let { if (reverse) it else !it }
}