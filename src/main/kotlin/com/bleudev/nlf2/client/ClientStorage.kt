package com.bleudev.nlf2.client

import net.fabricmc.loader.api.FabricLoader
import org.jetbrains.annotations.Range
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path

@JvmField
var canUseMouse: Boolean = true
@JvmField
var canUseKeyboard: Boolean = true
@JvmField
var webcamEnabled: Boolean = false
@JvmField
var compassForceSpin: Boolean = false

const val ADDRESS: String = "localhost:25565"

var join_blackness_delay_ticks: Int = 10
var join_blackness: Float = 1f
var noise_tick: Int = 0
var noise_amount: Float = .1f

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
