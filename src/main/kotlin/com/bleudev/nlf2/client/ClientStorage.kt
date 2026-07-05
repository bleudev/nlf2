package com.bleudev.nlf2.client

import com.bleudev.nlf2.*
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
// Event 101
var event101screenEffectClearTicks: Int = 0
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
// Rainbow effect
var rainbowTicks: Int = -1
// Shader effects
@JvmField
var shaderLostVesselControl: Boolean = false
@JvmField
var shaderBorderLag: Boolean = false
@JvmField
var shaderBorderEnd: Boolean = false
//val shaderEvent101: Boolean get() = true
val shaderEvent101: Boolean get() = (!dataSyncedEvent101 || event101screenEffectClearTicks > 0) && Minecraft.getInstance().gameProfile.name == EVENT101_NICKNAME
val shaderRainbow: Boolean get() = rainbowTicks != -1

// Nine lifes shaders uniforms
@JvmField
var shaderRedMajUniformStrength: Float = 0f
var shaderAnaglyphUniformX: Float = 0f
    get() = max(field, manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION * 0.01f)
var shaderAnaglyphUniformY: Float = 0f
    get() = max(field, manualUpdateTicks.toFloat() / MANUAL_UPDATE_EFFECT_DURATION * 0.005f)
val shaderEvent101Strength: Float get() = if (event101screenEffectClearTicks > 0) event101screenEffectClearTicks.toFloat() / EVENT101_SCREEN_EFFECT_CLEAR_DURATION else 1f
val shaderRainbowStrength: Float get() = (rainbowTicks.toFloat() / RAINBOW_START_TICKS).coerceAtMost(1f)/4
val shaderRainbowHue: Float get() = (rainbowTicks % RAINBOW_HUE_TICKS).toFloat() / RAINBOW_HUE_TICKS

private val theirPath: Path
    get() = FabricLoader.getInstance().configDir.parent.resolve(".their")

class TheirData {
    private data class TheirProperty(val default: Int, val validator: (Int) -> Boolean = {true})

    private val data: Array<Int>
    private val properties: Array<TheirProperty>
    private val size: Int
    @Throws(NumberFormatException::class)
    private constructor(data: List<String>, size: Int) {
        this.properties = arrayOf(
            TheirProperty(0) { it in 0..1 },
            TheirProperty(0) { it >= 0 },
            TheirProperty(0) { it >= 0},
        )
        this.data = Array(size) {this.properties[it].default}
        data.forEachIndexed { index, string -> this.data[index] = string.toInt() }
        this.size = size
    }

    @Throws(NumberFormatException::class)
    private fun causeCheckScreen(): Int {
        println("CAUSE CHECK SCREEN")
        throw NumberFormatException("Validator failed")
    }

    @Throws(NumberFormatException::class)
    internal operator fun get(index: Int): Int {
        val v = this.data[index]
        return if (this.properties[index].validator(v)) v
        else causeCheckScreen()
    }
    @Throws(NumberFormatException::class)
    internal operator fun set(index: Int, v: Int): TheirData {
        if (this.properties[index].validator(v)) this.data[index] = v
        else causeCheckScreen()
        return this
    }
    @Throws(NumberFormatException::class)
    internal fun validateAll() {
        for ((index, property) in this.properties.withIndex()) {
            if (!property.validator(this.data[index])) {
                causeCheckScreen()
                break
            }
        }
    }

    override fun toString(): String = data.joinToString("\n")

    companion object {
        fun fromLines(s: List<String>): TheirData = TheirData(s, 3)
        fun default(): TheirData = fromLines(listOf())
    }
}

var their: TheirData
    get() {
        try {
            return TheirData.fromLines(Files.readAllLines(theirPath))
        }
        catch (_: NumberFormatException) {
            return TheirData.default()
        }
        catch (_: NoSuchFileException) {
            Files.createFile(theirPath)
            their = TheirData.default()
            return TheirData.fromLines(Files.readAllLines(theirPath))
        }
    }
    set(v) {
        Files.writeString(theirPath, v.toString())
    }

/**
 * State of ability to disconnect
 *
 * 0 (initial) - Can, but when you press disconnect button, the text "YOU CAN'T" appears and button becomes not enabled
 *
 * 1 - There isn't disconnect button
 */
var disconnectState: @Range(from = 0, to = 1) Int
    get() = their[0]
    set(value) {their = their.set(0, value)}

/**
 * Number of times client was stopped
 * */
var closedTimes: @Range(from = 0, to = Long.MAX_VALUE) Int
    get() = their[1]
    set(value) {their = their.set(1, value)}

/**
 * Number of times client was respawned (or killed)
 */
var killedTimes: @Range(from = 0, to = Long.MAX_VALUE) Int
    get() = their[2]
    set(value) {their = their.set(2, value)}

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
//    println("\nfogColor: ${it.fogColor}\nfogStart: ${it.fogStart}\nfogEnd: ${it.fogEnd}")
}

@JvmField
var dataFogEventStateSyncPrev: FogEventState? = null
@JvmField
var dataFogEventStateSyncTicks: Int = -1
