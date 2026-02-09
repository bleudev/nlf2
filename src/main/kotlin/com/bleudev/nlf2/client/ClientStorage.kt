package com.bleudev.nlf2.client

@JvmField
var canUseMouse: Boolean = true
@JvmField
var canUseKeyboard: Boolean = true
var webcamEnabled: Boolean = false

const val ADDRESS: String = "localhost:25565"

var join_blackness_delay_ticks = 10
var join_blackness: Float = 1f