package com.bleudev.nlf2

import com.bleudev.nlf2.custom.Nlf2Blocks
import com.bleudev.nlf2.custom.Nlf2Items
import com.bleudev.nlf2.custom.Nlf2Sounds
import com.bleudev.nlf2.util.isActor
import com.bleudev.nlf2.util.server
import com.mojang.authlib.GameProfile
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.ItemLike
import net.minecraft.world.phys.Vec3
import java.util.*

// Rendering
const val ANIM_X_REF = 384 // Unused?
const val ANIM_Y_REF = 240

// Window
const val WINDOW_WIDTH_DEFAULT = 854
const val WINDOW_HEIGHT_DEFAULT = 480
const val MONITOR_WIDTH = 1920
const val MONITOR_HEIGHT = 1080

// Address
// blue.fnode.me:19147
private const val FAITHNODE_NODE = "blue"
private const val FAITHNODE_PORT = 19147
private const val SERVER_OUT_ADDRESS: String = "$FAITHNODE_NODE.fnode.me:$FAITHNODE_PORT"
private const val SERVER_IN_ADDRESS: String = "$FAITHNODE_NODE.in.fnode.me:$FAITHNODE_PORT"
private val IN_ADDRESS_NICKNAMES = listOf(
    "crockomd",
)
val SERVER_ADDRESS: String get() = Minecraft.getInstance().gameProfile.name.lowercase()
    .let { if (it in IN_ADDRESS_NICKNAMES) SERVER_IN_ADDRESS else SERVER_OUT_ADDRESS }

// Identifiers
/// Modificators
val MOD_BORDER_LOSE_HEARTS = getIdentifier("border_lose_hearts_modify")
/// Boss bar events
val BATTLE_BOSS_BAR = getIdentifier("battle_boss_bar")

// Nicknames
@JvmField
val SUPPORTED_NICKNAMES = listOf(
    "bleugame",
    "crockomd",
    "samivarence",
    "milkich22397",
    "waflla_kek",
    "ync27",
)
val SLIM_SKINS = listOf(
    "samivarence",
    "waflla_kek",
    "ync27",
)
val TELEPORT_LOCATIONS: Map<String, Vec3> = mapOf(
//    "bleugame" to Vec3(587.0, 65.0, 706.0),
//    "crockomd" to Vec3(601.0, 65.0, 818.0),
//    "samivarence" to Vec3(564.0, 70.0, 575.0),
//    "milkich22397" to Vec3(530.0, 127.0, 865.0),
)
const val EVENT112_NICKNAME = "Samivarence"
const val EVENT101_NICKNAME = "Samivarence"
const val EVENT85_NICKNAME = "Milkich22397"
const val EVENT888_NICKNAME = "bleugame"

private const val IMPORTANT_NICKNAME = "bleugame"
/**
 * Returns `true` if player is important for sent serverbound packets.
 * Only packets by them will be approved.
 *
 * Only one player can be important (at the server right in specific moment).
 * */
fun isImportantPlayer(player: ServerPlayer): Boolean {
    if (FabricLoader.getInstance().isDevelopmentEnvironment) return true
    else if (player.gameProfile.name.equals(IMPORTANT_NICKNAME, true)) return true
    else {
        val has = player.server.playerList.getPlayer(IMPORTANT_NICKNAME) != null
        return !has && player.isActor
    }
}

//val BATTLE_ARENA_LOCATION = Vec3(-4045056.0, 125.0 + 2, -16169914.0) // Singleplayer test
//val BATTLE_ARENA_LOCATION = Vec3(22.0, 72.0 + 2, -98.0) // Server test
val BATTLE_ARENA_LOCATION = Vec3(535.0, 211.0, 903.0) // Actual server test

// Unknown
@JvmField
val UNKNOWN_GAME_PROFILE = GameProfile(UUID.randomUUID(), "???")

// Ticks / durations
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

const val RIP_SANYA_EVENT_WHITE_FADE = 25
const val RIP_SANYA_EVENT_SANYA_TAKE = 850
const val RIP_SANYA_EVENT_SANYA_TAKE_FADE_OUT = 40
const val RIP_SANYA_EVENT_WINDOW_SHAKE_MAX_DXY = 20

const val UNKNOWN_RAINBOW_NAME_TAG_DURATION = 100

const val PART4_LAST_MEET_MASK = "mask_ync"

const val BATTLE_BOSSBAR_FADE_IN_DURATION = 60

const val SCARY_SOUND_FADE_IN = 10
val SCARY_SOUND_DURATION_FACTORY: (SoundEvent) -> Int = {
    when (it) {
        Nlf2Sounds.SCARY_VOICE_GIVE_IT -> 60
        Nlf2Sounds.SCARY_VOICE_GIVE_UP -> 72
        Nlf2Sounds.SCARY_VOICE_MY_WORLD -> 144
        Nlf2Sounds.SCARY_VOICE_WHY_YOU -> 102
        else -> throw MatchException("Sound isn't scary(", null)
    }
}

// Rainbow tooltip + highlight items
@JvmField
val RAINBOW_ITEMS_CHECKER: (ItemLike) -> Boolean = { it.asItem() in listOf(
    Nlf2Blocks.STRANGE_JUKEBOX.asItem(),
    Nlf2Blocks.ARENA_BLOCK.asItem(),
    Nlf2Blocks.COMPUTER.asItem(),
    Nlf2Items.CARD,
)}

// Booleans
const val INTRO_ENABLED = true
const val CARD_NEW_DIALOGUE = true

/// DEV
const val DEV_EVENT_101 = false
const val DEV_EVENT_85 = false
const val DEV_STORY_ACTOR = false
const val DEV_BAN_EVENT_112 = true
const val DEV_EVENT_888 = false

// Custom dev arguments
@Suppress("RedundantNullableReturnType", "RedundantSuppression")
@JvmField
//val DEV_ARG_USERNAME: String? = "PlayerDev"
val DEV_ARG_USERNAME: String? = null
