package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.unit.*
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val BEFORE_MEET_WATER_FLUID = getIdentifier("packet/before_meet_water_fluid")
    val CLIENT_NLF2_DATA_SYNC = getIdentifier("packet/client_nlf2_data_sync")
    val DISTANCE_UPDATE = getIdentifier("packet/distance_update")
    val JOIN_DISCONNECT_VERSION = getIdentifier("packet/join_disconnect_version")
    val MANUAL_UPDATE_EFFECT = getIdentifier("packet/manual_update_effect")
    val OPEN_MANUAL = getIdentifier("packet/open_manual")
    val OPEN_PUZZLE = getIdentifier("packet/open_puzzle")
    val OPEN_TEXT_BOX = getIdentifier("packet/open_text_box")
    val SKY_COLOR_EFFECT = getIdentifier("packet/sky_color_effect")
    val STRANGE_JUKEBOX_RAINBOW_CONTROL = getIdentifier("packet/strange_jukebox_rainbow_control")

    val UNIT_BORDER_BLACK_FOG_START = getIdentifier("packet/unit/border_black_fog_start")
    val UNIT_CANCEL_BORDER_BLACK_FOG = getIdentifier("packet/unit/cancel_border_black_fog")
    val UNIT_CRASH_CLIENT = getIdentifier("packet/unit/crash_client")
    val UNIT_EVENT101_SCREEN_EFFECT_CLEAR = getIdentifier("packet/unit/event101_screen_effect_clear")
    val UNIT_STORY_START = getIdentifier("packet/unit/story_start")
    val UNIT_STRANGE_JUKEBOX_MUSIC_STOP = getIdentifier("packet/unit/strange_jukebox_music_stop")

    val SB_JOIN_SEND_VERSION = getIdentifier("packet/sb/join_send_version")

    val SB_UNIT_KILL_BORDER_BLACK_FOG = getIdentifier("packet/sb/unit/kill_border_black_fog")

    fun initialize() {
        // Client bound
        val s2c = PayloadTypeRegistry.clientboundPlay()
        // Normal
        s2c.register(BeforeMeetWaterFluid)
        s2c.register(ClientNlf2DataSync)
        s2c.register(DistanceUpdate)
        s2c.register(JoinDisconnectVersion)
        s2c.register(ManualUpdateEffect)
        s2c.register(OpenManual)
        s2c.register(OpenPuzzle)
        s2c.register(OpenTextBox)
        s2c.register(SkyColorEffect)
        s2c.register(StrangeJukeboxRainbowControl)
        // Unit
        s2c.register(BorderBlackFogStart)
        s2c.register(CancelBorderBlackFog)
        s2c.register(CrashClientPayload)
        s2c.register(Event101ScreenEffectClear)
        s2c.register(StoryStart)
        s2c.register(StrangeJukeboxMusicStop)

        // Server bound
        val c2s = PayloadTypeRegistry.serverboundPlay()
        // Normal
        c2s.register(JoinSendVersion)
        // Unit
        c2s.register(KillBorderBlackFog)
    }
}