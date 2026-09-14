package com.bleudev.nlf2.sys.actor.chars

import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.PlayerModelType

open class VisibleOnlyForChar(val player: String, val reverse: Boolean = false) : ActorChar {
    override fun skin(client: String): String? {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else skinOverride
        else if (val1) skinOverride else ActorChar.EMPTY_TEXTURE
    }
    override fun skinModel(client: String): PlayerModelType {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) PlayerModelType.WIDE else skinModelOverride
        else if (val1) skinModelOverride else PlayerModelType.WIDE
    }
    override fun eyes(client: String): String {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else eyesOverride
        else if (val1) eyesOverride else ActorChar.EMPTY_TEXTURE
    }
    override fun overlay(client: String): String {
        val val1 = Minecraft.getInstance().gameProfile.name.equals(player, true)
        return if (reverse) if (val1) ActorChar.EMPTY_TEXTURE else overlayOverride
        else if (val1) overlayOverride else ActorChar.EMPTY_TEXTURE
    }

    open val skinOverride: String? get() = null
    open val skinModelOverride: PlayerModelType = PlayerModelType.WIDE
    open val eyesOverride: String get() = ActorChar.EMPTY_TEXTURE
    open val overlayOverride: String get() = ActorChar.EMPTY_TEXTURE
    override val isInvisible: Boolean get() = Minecraft.getInstance().gameProfile.name.equals(player, true)
        .let { if (reverse) it else !it }
}