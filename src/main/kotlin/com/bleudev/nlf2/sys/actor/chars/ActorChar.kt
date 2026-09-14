package com.bleudev.nlf2.sys.actor.chars

import net.minecraft.world.entity.player.PlayerModelType

interface ActorChar {
    fun tick() {}
    fun skin(client: String): String? = null
    fun skinModel(client: String): PlayerModelType = PlayerModelType.WIDE
    fun eyes(client: String): String = EMPTY_TEXTURE
    fun overlay(client: String): String = EMPTY_TEXTURE

    val isInvisible: Boolean get() = false
    val isForceVisible: Boolean get() = false
    val hideArmor: Boolean get() = true
    val hideHandItems: Boolean get() = false
    val renderHitboxes: Boolean get() = false
    val isForceNameTagVoiceDisconnected: Boolean get() = false
    val customWaypointColor: Int? get() = 0xffff0000.toInt()

    // Instanceof based booleans
    val isMask: Boolean get() = this is PlayerMaskChar || this is InvisibleForOnePlayerMask

    companion object {
        const val EMPTY_TEXTURE = "entity/player/empty"
    }
}