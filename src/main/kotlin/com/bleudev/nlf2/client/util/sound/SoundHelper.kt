package com.bleudev.nlf2.client.util.sound

import com.bleudev.nlf2.LOGGER
import java.io.BufferedInputStream
import java.io.IOException
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException

private class SoundHelper

private var ambientClip: Clip? = null

fun ambientPlay() {
    try {
        val resource = SoundHelper::class.java.getResourceAsStream("/secret_sound.wav")
        if (resource == null) {
            LOGGER.error("Sound was not found")
            return
        }
        ambientClip = AudioSystem.getClip()
        if (ambientClip != null) {
            ambientClip!!.open(AudioSystem.getAudioInputStream(BufferedInputStream(resource)))
            ambientClip!!.start()
            var millis = ambientClip!!.microsecondLength / 1000
            while (millis > 0 && ambientPlaying) {
                Thread.sleep(250)
                millis -= 250
            }
            ambientPlaying = false
            ambientClip!!.stop()
        }
    } catch (e: UnsupportedAudioFileException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: LineUnavailableException) {
        e.printStackTrace()
    }
}

var shouldPlayAmbient: Boolean = false
private var ambientPlaying: Boolean = false

fun ambientTick() {
    if (!ambientPlaying && shouldPlayAmbient) {
        ambientPlaying = true
        Thread(::ambientPlay).start()
    } else if (!shouldPlayAmbient) {
        ambientPlaying = false
    }
}