package com.bleudev.nlf2.client.util

import com.bleudev.nlf2.client.sys.anim.StoryAnimation
import com.bleudev.nlf2.util.reverseDelta
import kotlin.math.min

abstract class Ticker {
    var ticks: Long = 0L
        protected set
    abstract fun tick(): Long
    fun reset() {
        ticks = 0
    }

    fun delta(start: Long, duration: Long): Float = ((ticks - start).toFloat() / duration).coerceIn(0f, 1f)
    fun delta(start: Int, duration: Int): Float = delta(start.toLong(), duration.toLong())
    fun delta(start: Long, end: Long, duration: Long): Float = min(delta(start, duration), delta(end - duration, duration).reverseDelta())
    fun delta(start: Int, end: Int, duration: Int): Float = delta(start.toLong(), end.toLong(), duration.toLong())

    operator fun compareTo(other: Int) = ticks.compareTo(other)
    operator fun compareTo(other: Long) = ticks.compareTo(other)
    operator fun plus(other: Int): Long = ticks.plus(other)
    operator fun plus(other: Long): Long = ticks.plus(other)
    operator fun minus(other: Int): Long = ticks.minus(other)
    operator fun minus(other: Long): Long = ticks.minus(other)

    override operator fun equals(other: Any?): Boolean = ticks == other
    override fun toString(): String = ticks.toString()
    override fun hashCode(): Int = ticks.hashCode()
}

open class TimeTicker : Ticker() {
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
val TICKER_STORY = object : TimeTicker() {
    override fun tick(): Long {
        if (ticks >= StoryAnimation.endTick + 5) return ticks
        return super.tick()
    }
}
val TICKER_TEXT_BOX = TimeTicker()
val TICKER_RIP_VERT = TimeTicker()

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
