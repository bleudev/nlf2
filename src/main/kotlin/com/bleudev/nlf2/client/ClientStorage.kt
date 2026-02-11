package com.bleudev.nlf2.client

@JvmField
var canUseMouse: Boolean = true
@JvmField
var canUseKeyboard: Boolean = true
var webcamEnabled: Boolean = false
@JvmField
var compassForceSpin: Boolean = false

const val ADDRESS: String = "localhost:25565"

var join_blackness_delay_ticks: Int = 10
var join_blackness: Float = 1f
var noise_tick: Int = 0
var noise_amount: Float = .1f

// Data
@JvmField
var dataSyncedActor: String = "null"
@JvmField
var dataSyncedActorLoreChar: String = "null"
