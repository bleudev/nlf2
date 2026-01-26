package com.bleudev.nlf2.client

import com.bleudev.nlf2.MODID
import com.bleudev.nlf2.client.custom.Nlf2Sounds
import com.bleudev.nlf2.client.util.RelativeSoundInstance
import com.github.sarxos.webcam.Webcam
import com.mojang.blaze3d.platform.cursor.CursorTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.TransferState
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.math.min

object IntroHelper {
    private var start = System.currentTimeMillis()

    private var t = 0

    fun render(guiGraphics: GuiGraphics) {
        return // temp off
        // Ticker
        val curr = System.currentTimeMillis()
        if (curr - start >= 50) {
            start = curr
            t++
        }
        // Connect to server
        if (t == 400) {
            val mc = Minecraft.getInstance()
            ConnectScreen.startConnecting(
                mc.screen ?: TitleScreen(),
                mc,
                ServerAddress.parseString(address),
                ServerData("???", address, ServerData.Type.OTHER),
                false,
                null as TransferState?
            )
            canUseMouse = true
            canUseKeyboard = true
            return
        }

        canUseMouse = false
        canUseKeyboard = false
        Minecraft.getInstance().window.setTitle("ÿ__")
        Minecraft.getInstance().window.selectCursor(CursorTypes.NOT_ALLOWED)

        val w = guiGraphics.guiWidth()
        val h = guiGraphics.guiHeight()
        // Background
        guiGraphics.fill(0, 0, w, h, -0x1000000)
        // ___ frame
        val s = min(w, h) / 4
        guiGraphics.pose().pushMatrix()
        guiGraphics.pose().translate((w-s) / 2f, (h-s) / 2f)

        // Eyes appear
        if (t <= 20)
            renderIntroFrame(guiGraphics, 1, s)
        else if (t <= 40)
            renderIntroFrame(guiGraphics, 2, s)
        else if (t <= 60)
            renderIntroFrame(guiGraphics, 3, s)
        else if (t <= 80)
            renderIntroFrame(guiGraphics, 4, s)
        else if (t <= 120)
            renderIntroFrame(guiGraphics, 5, s)
        // Mouth appear
        else if (t <= 140)
            renderIntroFrame(guiGraphics, 6, s)
        else if (t <= 145)
            renderIntroFrame(guiGraphics, 7, s)
        else if (t <= 150)
            renderIntroFrame(guiGraphics, 8, s)
        else if (t <= 180)
            renderIntroFrame(guiGraphics, 9, s)
        // Laugh
        else if (t <= 185)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 190)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 195)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 215)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 220)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 225)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 230)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 235)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 240)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 245)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 250)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 255)
            renderIntroFrame(guiGraphics, 11, s)
        else if (t <= 260)
            renderIntroFrame(guiGraphics, 10, s)
        else if (t <= 265)
            renderIntroFrame(guiGraphics, 11, s)
        // End laugh delay
        else if (t <= 300)
            renderIntroFrame(guiGraphics, 9, s)
        // Disappear
        else if (t <= 320)
            renderIntroFrame(guiGraphics, 12, s)
        else if (t <= 340)
            renderIntroFrame(guiGraphics, 13, s)
        else if (t <= 360)
            renderIntroFrame(guiGraphics, 14, s)
        else if (t <= 380)
            renderIntroFrame(guiGraphics, 15, s)
        else if (t <= 400)
            renderIntroFrame(guiGraphics, 16, s)
        guiGraphics.pose().popMatrix()

        // Laugh sound
        if (t == 180) Minecraft.getInstance().soundManager.play(RelativeSoundInstance(Nlf2Sounds.LAUGH))

        // Webcam
        webcamEnabled = t in 190..380

        if (Webcam.getDefault().isOpen) {
            if (!webcamEnabled) Webcam.getDefault().close()
        } else if (webcamEnabled) Webcam.getDefault().open()
    }

    private fun renderIntroFrame(guiGraphics: GuiGraphics, index: Int, size: Int) {
        val resource = Identifier.fromNamespaceAndPath(MODID, "textures/anim/intro/$index.png")
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED, resource, 0, 0, 0f, 0f, size, size * 27 / 28, size, size * 27 / 28,
            0xffffffff.toInt()
        )
    }
}