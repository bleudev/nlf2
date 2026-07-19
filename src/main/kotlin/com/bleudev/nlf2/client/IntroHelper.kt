@file:Environment(EnvType.CLIENT)

package com.bleudev.nlf2.client

import com.bleudev.nlf2.INTRO_ENABLED
import com.bleudev.nlf2.SERVER_ADDRESS
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.util.Animation
import com.bleudev.nlf2.client.util.TICKER_INTRO
import com.bleudev.nlf2.client.util.blitAnim
import com.bleudev.nlf2.client.util.sound.RelativeSoundInstance
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.TransferState
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.sounds.SoundSource
import kotlin.math.min

object IntroHelper {
    fun render(graphics: GuiGraphicsExtractor) {
        if (!INTRO_ENABLED) return

        // Ticker
        val t = TICKER_INTRO.tick()
        // Connect to server
        if (t == 400L) {
            val mc = Minecraft.getInstance()
            join_blackness_delay_ticks = 10
            join_blackness = 1f
            ConnectScreen.startConnecting(
                mc.gui.screen() ?: TitleScreen(),
                mc,
                ServerAddress.parseString(SERVER_ADDRESS),
                ServerData("???", SERVER_ADDRESS, ServerData.Type.OTHER),
                false,
                null as TransferState?
            )
            canUseMouse = true
            canUseKeyboard = true
            return
        }

        canUseMouse = false
        canUseKeyboard = false
        webcamEnabled = t in 190..380

        val mc = Minecraft.getInstance()

        // Window features
        if (mc.window.isFullscreen) mc.window.toggleFullScreen()
        // NO MUSIC!
        mc.options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(.0)

        val w = graphics.guiWidth()
        val h = graphics.guiHeight()
        // Background
        graphics.fill(0, 0, w, h, -0x1000000)
        // ___ frame
        val s = min(w, h) / 4
        graphics.pose().pushMatrix()
        graphics.pose().translate((w-s) / 2f, (h-s) / 2f)

        val intro = { i: Int -> graphics.blitAnim(Animation.INTRO, i, width = s, height = s * 27 / 28) }

        // Eyes appear
        if (t <= 20) intro(1)
        else if (t <= 40) intro(2)
        else if (t <= 60) intro(3)
        else if (t <= 80) intro(4)
        else if (t <= 120) intro(5)
        // Mouth appear
        else if (t <= 140) intro(6)
        else if (t <= 145) intro(7)
        else if (t <= 150) intro(8)
        else if (t <= 180) intro(9)
        // Laugh
        else if (t <= 185) intro(10)
        else if (t <= 190) intro(11)
        else if (t <= 195) intro(10)
        else if (t <= 215) intro(11)
        else if (t <= 220) intro(10)
        else if (t <= 225) intro(11)
        else if (t <= 230) intro(10)
        else if (t <= 235) intro(11)
        else if (t <= 240) intro(10)
        else if (t <= 245) intro(11)
        else if (t <= 250) intro(10)
        else if (t <= 255) intro(11)
        else if (t <= 260) intro(10)
        else if (t <= 265) intro(11)
        // End laugh delay
        else if (t <= 300) intro(9)
        // Disappear
        else if (t <= 320) intro(12)
        else if (t <= 340) intro(13)
        else if (t <= 360) intro(14)
        else if (t <= 380) intro(15)
        else if (t <= 400) intro(16)
        graphics.pose().popMatrix()

        // Laugh sound
        if (t == 180L) Minecraft.getInstance().soundManager.play(RelativeSoundInstance(Nlf2Sounds.LAUGH))
    }
}