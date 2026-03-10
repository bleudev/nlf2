package com.bleudev.nlf2.client

import com.bleudev.nlf2.BORDER_BLACK_FOG_KILL_TRANS_DURATION
import com.bleudev.nlf2.DATA_FOG_EVENT_STATE_SYNC_DURATION
import com.bleudev.nlf2.EVENT101_NICKNAME
import com.bleudev.nlf2.MANUAL_UPDATE_EFFECT_DURATION
import com.bleudev.nlf2.custom.data.saved.FogEventState
import com.bleudev.nlf2.custom.data.saved.FogEventStateFogProperties
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.Range
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.math.max

@JvmField
var canUseMouse: Boolean = true
@JvmField
var canUseKeyboard: Boolean = true
@JvmField
var webcamEnabled: Boolean = false
@JvmField
var compassForceSpin: Boolean = false

const val ADDRESS: String = "localhost:25565"

// Join blackness
var join_blackness_delay_ticks: Int = 10
var join_blackness: Float = 1f
// Border
var noise_tick: Int = 0
var noise_amount: Float = .0f
// Manual
var manualUpdateTicks: Int = 0
val manualUpdateEffectOverlay: Float get() = manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION / 2
// Earthquake
private val earthquakeProp: Float get() =
    max(if (borderBlackFogTicks != -1 && borderBlackFogStarting && borderBlackFogTicks <= BORDER_BLACK_FOG_KILL_TRANS_DURATION)
            (1f - (borderBlackFogTicks) / BORDER_BLACK_FOG_KILL_TRANS_DURATION) * 1.25f
        else noise_amount,
        manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION / 2
    )
val earthquakeStrength: Float get() = earthquakeProp / 8
val earthquakeSpeed: Float get() = earthquakeProp * 2
// Border black fog
var borderBlackFogTicks: Int = -1
var borderBlackFogStarting: Boolean = true
var borderBlackFog: Float = 0f
@JvmField
var deathScreenBlackFogEffect: Boolean = false
@JvmField
var borderLagTicks: Int = 0
val blackFogEffect: Float
    get() {
        if (dataSyncedBorderState != 2) return 0f
        return borderBlackFog
    }
val cloudAlphaDelta: Float
    get() {
        if (dataSyncedBorderState != 2) return 1f
        return 1f - borderBlackFog
    }
// Pause screen cutscene
@JvmField
var pause_screen_red_screen_effect: Boolean = false
@JvmField
var should_not_play_any_sound: Boolean = false
@JvmField
var pause_screen_ticks: Int = 0
// Shader effects
@JvmField
var shaderLostVesselControl: Boolean = false
@JvmField
var shaderBorderLag: Boolean = false
@JvmField
var shaderBorderEnd: Boolean = false
//val shaderEvent101: Boolean get() = true
val shaderEvent101: Boolean get() = !dataSyncedEvent101 && Minecraft.getInstance().gameProfile.name == EVENT101_NICKNAME

// Nine lifes shaders uniforms
@JvmField
var shaderRedMajUniformStrength: Float = 0f
var shaderAnaglyphUniformX: Float = 0f
    get() = max(field, manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION * 0.01f)
var shaderAnaglyphUniformY: Float = 0f
    get() = max(field, manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION * 0.005f)
var shaderEvent101Strength: Float = 1f

@Suppress("ObjectPropertyName")
private val ___path: Path
    get() = FabricLoader.getInstance().configDir.resolve("nlf2-_____.txt")
/**
 * State of ability to disconnect
 *
 * 0 (initial) - Can, but when you press disconnect button, the text "YOU CAN'T" appears and button becomes not enabled
 *
 * 1 - There isn't disconnect button
 */
var disconnectState: @Range(from = 0, to = 1) Int
    get() {
        return try { Files.readString(___path).toInt() }
        catch (_: NumberFormatException) { 0 }
        catch (_: NoSuchFileException) {
            Files.createFile(___path)
            0
        }
    }
    set(value) {
        Files.writeString(___path, value.toString())
    }

// Data
@JvmField
var dataSyncedActor: String = "null"
@JvmField
var dataSyncedActorLoreChar: String = "null"
@JvmField
var dataSyncedBorderState: Int = 0
@JvmField
var dataSyncedFogEventState: FogEventState = FogEventState.BEFORE_MEET_CROCKO
@JvmField
var dataSyncedEvent101: Boolean = false
@JvmField
var dataSyncedManualId: Int = 0

val dataSyncedFogProperties: FogEventStateFogProperties get() = dataSyncedFogEventState.getFogProperties(
    dataFogEventStateSyncPrev, DATA_FOG_EVENT_STATE_SYNC_DURATION - dataFogEventStateSyncTicks.coerceAtLeast(0)).also {
    println("\nfogColor: ${it.fogColor}\nfogStart: ${it.fogStart}\nfogEnd: ${it.fogEnd}")
}

@JvmField
var dataFogEventStateSyncPrev: FogEventState? = null
@JvmField
var dataFogEventStateSyncTicks: Int = -1
