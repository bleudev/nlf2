package com.bleudev.nlf2.client.music.bg

import net.minecraft.client.Minecraft
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class MusLab(mc: Minecraft) : BGMusic("lab.wav", 1, mc) {
    override val volume: Float
        get() = 1f
    override val shouldPlay: Boolean
        get() {
            val pl = mc.player ?: return false
            val pos1 = Vec3(-4045012.0, 81.00, -16169920.0)
            val pos2 = Vec3(-4045003.0, 110.0, -16169913.0)
            return AABB(pos1, pos2).contains(pl.position())
        }
}