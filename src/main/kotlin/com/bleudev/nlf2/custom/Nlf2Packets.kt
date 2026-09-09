package com.bleudev.nlf2.custom

import com.bleudev.nlf2.custom.packet.payload.*
import com.bleudev.nlf2.custom.packet.payload.serverbound.JoinSendVersion
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.KillBorderBlackFog
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipSanyaTakeStart
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.RipVertEnd
import com.bleudev.nlf2.custom.packet.payload.serverbound.unit.StoryEnd
import com.bleudev.nlf2.custom.packet.payload.unit.*
import com.bleudev.nlf2.getIdentifier
import com.bleudev.nlf2.util.register
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object Nlf2Packets {
    val BEFORE_MEET_WATER_FLUID = getIdentifier("packet/before_meet_water_fluid")
    val CLIENT_NLF2_DATA_SYNC = getIdentifier("packet/client_nlf2_data_sync")
    val COMPUTER_MESSAGE_SYNC = getIdentifier("packet/computer_message_sync")
    val DISTANCE_UPDATE = getIdentifier("packet/distance_update")
    val JOIN_DISCONNECT_VERSION = getIdentifier("packet/join_disconnect_version")
    val MANUAL_UPDATE_EFFECT = getIdentifier("packet/manual_update_effect")
    val OPEN_MANUAL = getIdentifier("packet/open_manual")
    val OPEN_PUZZLE = getIdentifier("packet/open_puzzle")
    val OPEN_TEXT_BOX = getIdentifier("packet/open_text_box")
    val RIP_SANYA_EVENT = getIdentifier("packet/rip_sanya_event")
    val RIP_VERT_EVENT = getIdentifier("packet/rip_vert_event")
    val SKY_COLOR_EFFECT = getIdentifier("packet/sky_color_effect")
    val START_WHITENESS_SCREEN_ADVANCED = getIdentifier("packet/start_whiteness_screen_advanced")
    val STRANGE_JUKEBOX_RAINBOW_CONTROL = getIdentifier("packet/strange_jukebox_rainbow_control")
    val UNKNOWN_MEET = getIdentifier("packet/unknown_meet")

    val UNIT_BATTLE_SECOND_START = getIdentifier("packet/unit/battle_second_start")
    val UNIT_BORDER_BLACK_FOG_START = getIdentifier("packet/unit/border_black_fog_start")
    val UNIT_CANCEL_BORDER_BLACK_FOG = getIdentifier("packet/unit/cancel_border_black_fog")
    val UNIT_CRASH_CLIENT = getIdentifier("packet/unit/crash_client")
    val UNIT_END_BATTLE = getIdentifier("packet/unit/end_battle")
    val UNIT_EVENT101_SCREEN_EFFECT_CLEAR = getIdentifier("packet/unit/event101_screen_effect_clear")
    val UNIT_SCARY_VOICE_PHRASE = getIdentifier("packet/unit/scary_voice_phrase")
    val UNIT_START_BATTLE = getIdentifier("packet/unit/start_battle")
    val UNIT_STORY_START = getIdentifier("packet/unit/story_start")
    val UNIT_STRANGE_JUKEBOX_MUSIC_STOP = getIdentifier("packet/unit/strange_jukebox_music_stop")

    val SB_JOIN_SEND_VERSION = getIdentifier("packet/sb/join_send_version")

    val SB_UNIT_KILL_BORDER_BLACK_FOG = getIdentifier("packet/sb/unit/kill_border_black_fog")
    val SB_UNIT_RIP_SANYA_TAKE_START = getIdentifier("packet/sb/unit/rip_sanya_take_start")
    val SB_UNIT_RIP_VERT_END = getIdentifier("packet/sb/unit/rip_vert_end")
    val SB_UNIT_STORY_END = getIdentifier("packet/sb/unit/story_end")

    fun initialize() {
        // Client bound
        val s2c = PayloadTypeRegistry.clientboundPlay()
        // Normal
        s2c.register(BeforeMeetWaterFluid)
        s2c.register(ClientNlf2DataSync)
        s2c.register(ComputerMessageSync)
        s2c.register(DistanceUpdate)
        s2c.register(JoinDisconnectVersion)
        s2c.register(ManualUpdateEffect)
        s2c.register(OpenManual)
        s2c.register(OpenPuzzle)
        s2c.register(OpenTextBox)
        s2c.register(RIPSanyaEvent)
        s2c.register(RIPVertEvent)
        s2c.register(SkyColorEffect)
        s2c.register(StartWhitenessScreenAdvanced)
        s2c.register(StrangeJukeboxRainbowControl)
        s2c.register(UnknownMeet)
        // Unit
        s2c.register(BattleSecondStart)
        s2c.register(BorderBlackFogStart)
        s2c.register(CancelBorderBlackFog)
        s2c.register(CrashClient)
        s2c.register(EndBattle)
        s2c.register(Event101ScreenEffectClear)
        s2c.register(ScaryVoicePhrase)
        s2c.register(StartBattle)
        s2c.register(StoryStart)
        s2c.register(StrangeJukeboxMusicStop)

        // Server bound
        val c2s = PayloadTypeRegistry.serverboundPlay()
        // Normal
        c2s.register(JoinSendVersion)
        // Unit
        c2s.register(KillBorderBlackFog)
        c2s.register(RipSanyaTakeStart)
        c2s.register(RipVertEnd)
        c2s.register(StoryEnd)
    }
}