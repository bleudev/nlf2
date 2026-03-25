package com.bleudev.nlf2.client.music

import com.bleudev.nlf2.client.music.bg.BGMusic
import com.bleudev.nlf2.client.music.bg.MusManual
import com.bleudev.nlf2.client.music.bg.MusPlace
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

class MusicSystem(private val mc: Minecraft) {
    private val bg: ArrayList<BGMusic> = ArrayList()
    init {
        bg.add(MusManual())
        bg.add(MusPlace())
    }

    private var curr: BGMusic? = null
    fun tick() {
        if (curr?.canBePlaying(mc) == false) curr = null
        refreshPlay()
        bg.sortedByDescending(BGMusic::importance).firstOrNull { it.canBePlaying(mc) }?.let {
            if (curr == null) {
                curr = it
                refreshPlay()
            } else if (curr!!.importance < it.importance) {
                curr = null
                refreshPlay()
                curr = it
                refreshPlay()
            }
        }
    }

    private var playbackJob: Job? = null
    private var isPlaying = false
    private fun refreshPlay() {
        if (curr == null) musStop()
        else if (!isPlaying) musPlay("/music/${curr!!.mus}")
    }
    private fun musPlay(resourcePath: String) {
        musStop()
        isPlaying = true
        playbackJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = MusicSystem::class.java.getResourceAsStream(resourcePath)
                if (inputStream == null) {
                    println("MusicSystem: $resourcePath was not found!")
                    return@launch
                }

                val bufferedIn = BufferedInputStream(inputStream)
                AudioSystem.getAudioInputStream(bufferedIn).use { audioStream ->
                    val format = audioStream.format
                    val info = DataLine.Info(SourceDataLine::class.java, format)
                    (AudioSystem.getLine(info) as SourceDataLine).use { line ->
                        line.open(format)
                        line.start()

                        val buffer = ByteArray(4096)
                        var bytesRead = audioStream.read(buffer, 0, buffer.size)

                        while (bytesRead != -1 && isPlaying && isActive) {
                            line.write(buffer, 0, bytesRead)
                            bytesRead = audioStream.read(buffer, 0, buffer.size)
                        }

                        line.drain()
                    }
                }
            } catch (e: Exception) {
                println("Error while playing music")
                e.printStackTrace()
            } finally {
                isPlaying = false
            }
        }
    }

    private fun musStop() {
        isPlaying = false
        playbackJob?.cancel()
    }

}