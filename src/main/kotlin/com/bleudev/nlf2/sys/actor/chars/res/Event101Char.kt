package com.bleudev.nlf2.sys.actor.chars.res

import com.bleudev.nlf2.sys.actor.chars.ActorChar

class Event101Char : ActorChar {
    private val overlays: List<String> = listOf(
        "entity/player/overlay/event101/head",
        "entity/player/overlay/event101/left_arm",
        "entity/player/overlay/event101/left_leg",
        "entity/player/overlay/event101/right_arm",
        "entity/player/overlay/event101/right_leg",
        "entity/player/overlay/event101/torso",
    )

    private var ticks: Int = 0
    private var maxTicks: Int = -1
    private var overlay: String? = null

    override fun tick() {
        if (ticks >= maxTicks) {
            overlay = if (overlay == null) overlays.random() else null
            ticks = 0
            maxTicks = 1
//            maxTicks = if (overlay == null) Random.nextInt(1, 3) else Random.nextInt(1, 5)
        }
        ticks++
    }
    override fun eyes(client: String): String =
        if (overlay == overlays[0]) "entity/player/eyes/event101" else "entity/player/empty"
    override fun overlay(client: String): String = overlay ?: "entity/player/empty"
    override val hideArmor: Boolean get() = false
    override val renderHitboxes: Boolean get() = true
}