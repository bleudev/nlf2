package com.bleudev.nlf2

import net.minecraft.world.phys.Vec3

const val SERVER_ADDRESS: String = "pink.fnode.me:19164"

@JvmField
val SUPPORTED_NICKNAMES = listOf("bleugame", "crockomd", "samivarence")
val TELEPORT_LOCATIONS = mapOf(
    "bleugame" to Vec3(-522.0, 66.0, 90.0),
    "crockomd" to Vec3(-543.0, 73.0, 235.0),
)
const val EVENT101_NICKNAME = "Samivarence"

const val BORDER_BLACK_FOG_DURATION = 60
const val BORDER_BLACK_FOG_KILL_TRANS_DELAY = 40
const val BORDER_BLACK_FOG_KILL_TRANS_DURATION = 30

const val MANUAL_UPDATE_EFFECT_DURATION = 15

const val BEFORE_MEET_EFFECT_BEAT_DURATION = 200
const val BEFORE_MEET_TRANS_DURATION = 100

const val LAB_OVERLAY_EFFECT_BEAT_DURATION = 150
const val LAB_OVERLAY_TRANS_DURATION = 100

const val EVENT101_NOISE_MAX_DISTANCE = 5
const val EVENT101_SCREEN_EFFECT_CLEAR_DURATION = 40

const val RAINBOW_START_TICKS = 60
const val RAINBOW_HUE_TICKS = 100

const val INTRO_ENABLED = false

const val DEV_EVENT_101 = true
