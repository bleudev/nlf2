package com.bleudev.nlf2.client.util

abstract class Ticker() {
    var ticks: Long = 0L
        protected set
    abstract fun tick(): Long
    fun reset() {
        ticks = 0
    }
}

class TimeTicker() : Ticker() {
    private var start: Long = System.currentTimeMillis()

    override fun tick(): Long {
        val curr = System.currentTimeMillis()
        if (curr - start >= 50) {
            start = curr
            ticks++
        }
        return ticks
    }
}

val TICKER_INTRO = TimeTicker()