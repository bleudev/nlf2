package com.bleudev.nlf2.client.music

import com.bleudev.nlf2.LOGGER
import com.bleudev.nlf2.client.music.bg.BGMusic
import com.bleudev.nlf2.client.music.bg.MusBattle
import com.bleudev.nlf2.client.music.bg.MusManual
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioSystem

// TODO: FIX Volume not changing during music playing (with music depends on music volume ingame)
class MusicSystem(mc: Minecraft) {
    private val bg: List<BGMusic> = listOf(
        MusManual(mc),
        MusBattle(mc)
    )

    private var curr: BGMusic? = null
        set(v) {field = v; refreshPlay()}
    fun tick() {
        val m = bg.filter { it.shouldPlay }.maxOrNull()
        if (curr?.shouldPlay == false) curr = null
        if (curr == null || m == null) curr = m
        else if (curr!! < m) {
            curr = null
            curr = m
        }
        if (curr != null) refreshPlay()
        curr?.run {
            musVolume = finalVolume
            musPitch = pitch
        }
    }

    private var playbackJob: Job? = null
    private var isPlaying = false

    private var musVolume: Float = 1f
    private var musPitch: Float = 1f

    private fun refreshPlay() {
        if (curr == null) musStop()
        else if (!isPlaying) musPlay("/music/${curr!!.mus}")
    }
    private fun musPlay(resourcePath: String) {
        musStop()
        isPlaying = true
        playbackJob = CoroutineScope(Dispatchers.IO).launch {
            val inputStream = MusicSystem::class.java.getResourceAsStream(resourcePath)
            if (inputStream == null) {
                LOGGER.error("MusicSystem: $resourcePath was not found!")
                return@launch
            }

            val audioStream = AudioSystem.getAudioInputStream(inputStream)
            val format = audioStream.format

            if (format.sampleSizeInBits != 16) {
                System.err.println("MusicSystem: Only 16-bit WAV files are supported by this basic loader. This file is ${format.sampleSizeInBits}-bit.")
                audioStream.close()
                return@launch
            }

            val alFormat = when (format.channels) {
                1 -> AL10.AL_FORMAT_MONO16
                2 -> AL10.AL_FORMAT_STEREO16
                else -> {
                    System.err.println("MusicSystem: Unsupported channel count: ${format.channels}")
                    audioStream.close()
                    return@launch
                }
            }

            val sampleRate = format.sampleRate.toInt()

            val rawBytes = audioStream.readAllBytes()
            audioStream.close()

            val totalShorts = rawBytes.size / 2
            val alBuf = MemoryUtil.memAllocShort(totalShorts)

            val byteBuffer = ByteBuffer.wrap(rawBytes).order(ByteOrder.LITTLE_ENDIAN)
            while (byteBuffer.remaining() >= 2) {
                alBuf.put(byteBuffer.getShort())
            }
            alBuf.flip()

            val device: Long = ALC10.alcOpenDevice(null as ByteBuffer?)
            check(device != 0L) { "MusicSystem: Failed to open the default OpenAL device." }

            val deviceCaps = ALC.createCapabilities(device)
            val context = ALC10.alcCreateContext(device, null as IntArray?)
            if (context == 0L) {
                ALC10.alcCloseDevice(device)
                throw IllegalStateException("MusicSystem: Failed to create OpenAL context.")
            }

            ALC10.alcMakeContextCurrent(context)
            AL.createCapabilities(deviceCaps)

            val bufferId = AL10.alGenBuffers()
            AL10.alBufferData(bufferId, alFormat, alBuf, sampleRate)
            MemoryUtil.memFree(alBuf)

            val sourceId = AL10.alGenSources()
            AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId)
            AL10.alSourcef(sourceId, AL10.AL_GAIN, musVolume)
            AL10.alSourcef(sourceId, AL10.AL_PITCH, musPitch)

            AL10.alSourcePlay(sourceId)

            while (isPlaying && AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
                try {
                    AL10.alSourcef(sourceId, AL10.AL_GAIN, musVolume)
                    AL10.alSourcef(sourceId, AL10.AL_PITCH, musPitch)
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            AL10.alDeleteSources(sourceId)
            AL10.alDeleteBuffers(bufferId)

            ALC10.alcMakeContextCurrent(0)
            ALC10.alcDestroyContext(context)
            ALC10.alcCloseDevice(device)

            isPlaying = false
        }
    }
    private fun musStop() {
        isPlaying = false
//        playbackJob?.cancel()
    }
}