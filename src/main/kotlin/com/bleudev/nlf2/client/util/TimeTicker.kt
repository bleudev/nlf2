package com.bleudev.nlf2.client.util

abstract class Ticker {
    var ticks: Long = 0L
        protected set
    abstract fun tick(): Long
    fun reset() {
        ticks = 0
    }
}

class TimeTicker : Ticker() {
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

class TimeTimer {
    private var start: Long = System.currentTimeMillis()
    private var prev: Long = System.currentTimeMillis()
    private var end: Long = System.currentTimeMillis()

    val time: Long get() = end - start
    val timeDelta: Long get() = end - prev

    fun tick(): Long {
        prev = end
        end = System.currentTimeMillis()
        return time
    }

    fun reset() {
        start = System.currentTimeMillis()
    }
}

val TIMER_MASK = TimeTimer()
