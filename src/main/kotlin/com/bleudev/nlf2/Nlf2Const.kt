package com.bleudev.nlf2

import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec3

private const val SERVER_OUT_ADDRESS: String = "pink.fnode.me:19164"
private const val SERVER_IN_ADDRESS: String = "pink.in.fnode.me:19164"
private val IN_ADDRESS_NICKNAMES = listOf(
    "crockomd",
)
val SERVER_ADDRESS: String get() = Minecraft.getInstance().gameProfile.name.lowercase()
    .let { if (it in IN_ADDRESS_NICKNAMES) SERVER_IN_ADDRESS else SERVER_OUT_ADDRESS }

// Identifiers
/// Modificators
val MOD_BORDER_LOSE_HEARTS = getIdentifier("border_lose_hearts_modify")

// Other
@JvmField
val SUPPORTED_NICKNAMES = listOf(
    "bleugame",
    "crockomd",
    "samivarence",
    "milkich22397",
    "waflla_kek",
)
val SLIM_SKINS = listOf(
    "samivarence",
    "waflla_kek",
)
val TELEPORT_LOCATIONS = mapOf(
    "bleugame" to Vec3(587.0, 65.0, 706.0),
    "crockomd" to Vec3(601.0, 65.0, 818.0),
    "samivarence" to Vec3(564.0, 70.0, 575.0),
    "milkich22397" to Vec3(530.0, 127.0, 865.0),
)
const val EVENT101_NICKNAME = "Samivarence"
const val EVENT85_NICKNAME = "Milkich22397"

const val BORDER_LOSE_HEARTS_BEFORE_DURATION = 200
const val BORDER_LOSE_HEARTS_AFTER_DURATION = 80
const val BORDER_LOSE_HEARTS_DISTANCE = 10

const val BORDER_BLACK_FOG_DURATION = 60
const val BORDER_BLACK_FOG_KILL_TRANS_DELAY = 40
const val BORDER_BLACK_FOG_KILL_TRANS_DURATION = 30

const val MANUAL_UPDATE_EFFECT_DURATION = 15

const val BEFORE_MEET_EFFECT_BEAT_DURATION = 200
const val BEFORE_MEET_TRANS_DURATION = 100

const val LAB_OVERLAY_EFFECT_BEAT_DURATION = 150
const val LAB_OVERLAY_TRANS_DURATION = 100

const val EVENT101_NOISE_MAX_DISTANCE = 5
const val EVENT101_COMPASS_CRAZY_DISTANCE = 3
const val EVENT101_SCREEN_EFFECT_CLEAR_DURATION = 40

const val RAINBOW_START_TICKS = 60
const val RAINBOW_HUE_TICKS = 100

const val BEFORE_MEET_WATER_FLUID_TICKS = 60

const val AMETHYST_DAISY_DISTANCE = 3

const val RIP_VERT_EVENT_LAG_FADE_IN = 50
const val RIP_VERT_EVENT_ERASE_DURATION = 80

const val INTRO_ENABLED = false

const val DEV_EVENT_101 = false
const val DEV_EVENT_85 = false
const val DEV_STORY_ACTOR = true
