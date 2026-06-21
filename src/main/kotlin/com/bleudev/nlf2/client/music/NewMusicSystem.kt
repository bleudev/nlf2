package com.bleudev.nlf2.client.music

import com.bleudev.nlf2.LOGGER
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioSystem


fun main() {
    val inputStream = MusicSystem::class.java.getResourceAsStream("/music/manual.wav")
    if (inputStream == null) {
        LOGGER.error("MusicSystem: was not found!")
        return
    }

    val audioStream = AudioSystem.getAudioInputStream(inputStream)
    val format = audioStream.format

    if (format.sampleSizeInBits != 16) {
        System.err.println("Error: Only 16-bit WAV files are supported by this basic loader. This file is ${format.sampleSizeInBits}-bit.")
        audioStream.close()
        return
    }

    val alFormat = when (format.channels) {
        1 -> AL10.AL_FORMAT_MONO16
        2 -> AL10.AL_FORMAT_STEREO16
        else -> {
            System.err.println("Error: Unsupported channel count: ${format.channels}")
            audioStream.close()
            return
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
    check(device != 0L) { "Failed to open the default OpenAL device." }

    val deviceCaps = ALC.createCapabilities(device)
    val context = ALC10.alcCreateContext(device, null as IntArray?)
    if (context == 0L) {
        ALC10.alcCloseDevice(device)
        throw IllegalStateException("Failed to create OpenAL context.")
    }

    ALC10.alcMakeContextCurrent(context)
    AL.createCapabilities(deviceCaps)

    // 7. Load Data into OpenAL Buffer
    val bufferId = AL10.alGenBuffers()
    AL10.alBufferData(bufferId, alFormat, alBuf, sampleRate)
    MemoryUtil.memFree(alBuf) // Safely free native memory immediately after upload

    // 8. Setup Source and Play
    val sourceId = AL10.alGenSources()
    AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId)
    AL10.alSourcef(sourceId, AL10.AL_GAIN, 0.5f)
    AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.0f)

    AL10.alSourcePlay(sourceId)

    var i = 0
    while (AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
        try {
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
}
